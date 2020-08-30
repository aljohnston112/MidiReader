package file;

import java.io.File;
import java.io.FileInputStream;
import java.io.BufferedInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Optional;

import javax.activation.UnsupportedDataTypeException;

import chunks.MidChunk.ChunkType;
import events.MidEventDamp;
import events.MidEventDataEntryLSB;
import events.MidEventDataEntryMSB;
import events.MidEventDeviceName;
import events.MidEventEndOfTrack;
import events.MidEventInstrumentName;
import events.MidEvent;
import events.MidEventBankSelect;
import events.MidEventNoteOff;
import events.MidEventNoteOn;
import events.MidEventPanChange;
import events.MidEventPitchBend;
import events.MidEventPitchBendSensitivity;
import events.MidEventProgramChange;
import events.MidEventTrackName;
import events.MidEventVolumeChange;
import midFileBuilder.MidChunkBuilder;
import midFileBuilder.MidFileBuilder;
import midFileBuilder.MidHeaderBuilder;
import midFileBuilder.MidTrackBuilder;
import midFileBuilder.MidHeaderBuilder.Format;
import notes.Note;
import notes.Scale;
import notes.ThreeDimensionalNoteArray;
import notes.TwelveToneEqualTemperament;
import rhythm.Tempo;
import rhythm.TimeSignature;

public class MidReader {

	static File midFile;

	static long lengthToAdd = 0;

	static byte runningStatus;

	static boolean runningStatusInvalid = false;

	static byte bankSelectMSB = -1;

	static byte parameterLSB = -1;

	static byte parameterMSB = -1;

	static TimeSignature ts = null;

	static int clocksPerMetronomeTick = -1;

	static int n32ndNotesPer24Clocks = -1;

	static Scale scale = null;

	static Tempo tempo = null;

	static MidSMTPEOffset startTime = null;

	public static void main(String[] args) {
		System.out.println("MidReader started");
		File folder = new File("C:\\Users\\aljoh\\Downloads\\Undertale MIDI\\Undertale MIDI");
		File[] files = folder.listFiles();
		for(int k = 0; k < files.length; k++) {
			if(file.FileAlorigthms.getExt(files[k]).get().equals(".mid")) {
				bankSelectMSB = -1;
				parameterLSB = -1;
				parameterMSB = -1;
				ts = null;
				clocksPerMetronomeTick = -1;
				n32ndNotesPer24Clocks = -1;
				scale = null;
				tempo = null;
				startTime = null;
				midFile = files[k];
				BufferedInputStream midFileStream = null;
				if(verifyMidFile(midFile)) {
					System.out.print("Mid file: ");
					System.out.print(midFile.getName());
					System.out.println(" exists and can be read");	
					try {
						FileInputStream tempMidFileStream = new FileInputStream(midFile);
						midFileStream = new BufferedInputStream(tempMidFileStream);
						readMidFile(midFileStream);
						midFileStream.close();
					} catch (FileNotFoundException e) {
						System.out.print("Mid file: ");
						System.out.print(midFile.getName());
						System.out.println(" was not found");
						e.printStackTrace();
					} catch (UnsupportedDataTypeException e) {
						System.out.print("Mid file: ");
						System.out.print(midFile.getName());
						System.out.println(" had an unsupported chunk type");
						e.printStackTrace();
					} catch (IOException e) {
						System.out.println("There was an I/O error");
						e.printStackTrace();
					}
				} else {
					System.out.print("Mid file: ");
					System.out.print(midFile.getName());
					System.out.println(" does not exist or can not be read");
				}
			}
		}		

	}

	private static void readMidFile(BufferedInputStream midFileStream) throws IOException {
		MidFileBuilder midBuilder = new MidFileBuilder();
		while(midFileStream.available() > 0) {
			MidChunkBuilder chunkBuilder = getChunk(midFileStream).orElse(null);
			long length = 0;
			if(chunkBuilder instanceof MidHeaderBuilder) {
				((MidHeaderBuilder) chunkBuilder).setFormat(getHeaderFormat(midFileStream));
				length += 2;
				((MidHeaderBuilder) chunkBuilder).setNTracks(getHeaderNTracks(midFileStream));
				length += 2;
				((MidHeaderBuilder) chunkBuilder).setDivision(getHeaderDivision(midFileStream));
				length += 2;
				if(length < chunkBuilder.getLength()) {
					System.out.println("Skipping over the rest of the header");
				}
				while(length < chunkBuilder.getLength()) {
					midFileStream.read();
					length++;
				}
				midBuilder.setHeader(((MidHeaderBuilder) chunkBuilder).build());
			} else if(chunkBuilder instanceof MidTrackBuilder) {
				long runningTicks = 0;
				while(length < chunkBuilder.getLength()) {
					long ticks = getVariableLengthQuantity(midFileStream);
					runningTicks+=ticks;
					length += lengthToAdd;
					System.out.print("Track's next event is ");
					System.out.print(ticks);
					System.out.println(" ticks from the last event");
					MidEvent event = getEvent(midFileStream).orElse(null);
					if(event != null) {
						event.setTicksFromLastEvent(runningTicks);
						runningTicks = 0;
					}
					length += lengthToAdd;
					if(event == null) {

					} else if(event instanceof MidEventEndOfTrack) {
						while(length < chunkBuilder.getLength()) {
							midFileStream.read();
							length++;
						}
					} else {
						((MidTrackBuilder) chunkBuilder).addEvent(event);
					}
				}
				midBuilder.addTrack(((MidTrackBuilder)chunkBuilder).build());
			} else {
				while(length < chunkBuilder.getLength()) {
					midFileStream.read();
					length = length + 1;
				}
			}
		}
		if(tempo != null) {
			if(midBuilder.getTempo() == null) {
				midBuilder.setTempo(tempo);
			}
		} else {
			if(midBuilder.getTempo() == null) {
				midBuilder.setTempo(new Tempo(120));
			}
		}
		if(ts != null) {
			midBuilder.setTimeSignature(ts);
		} else {
			midBuilder.setTimeSignature(new TimeSignature(4, 4));			
		}
		if(startTime != null) {
			midBuilder.setStartTime(startTime);
		}
		if(clocksPerMetronomeTick != -1) {
			midBuilder.setClocksPerMetronomeTick(clocksPerMetronomeTick);			
		}
		if(n32ndNotesPer24Clocks != -1) {
			midBuilder.setN32ndNotesPer24Clocks(n32ndNotesPer24Clocks);			
		}
		MidFile mf = midBuilder.build();
		ThreeDimensionalNoteArray b = mf.tracks.get(1).getTrack(mf.getTicksPerSecond());
		System.out.println("File has been built");
	}

	private static Format getHeaderFormat(BufferedInputStream midFileStream) throws IOException {
		byte[] sizeBytes = new byte[2];
		midFileStream.read(sizeBytes);
		byte[] sizeBytesInt = new byte[4];
		sizeBytesInt[0] = 0x00;
		sizeBytesInt[1] = 0x00;
		sizeBytesInt[2] = sizeBytes[0];
		sizeBytesInt[3] = sizeBytes[1];
		ByteBuffer bb = ByteBuffer.wrap(sizeBytesInt);
		int format = bb.getInt();
		System.out.print("Header format has");
		switch(format) {
		case 0 : 
			System.out.println(" a single track");
			return Format.SINGLE_MULTI_CHANNEL;
		case 1: 
			System.out.println(" synched tracks");
			return Format.SYNCHED_TRACKS;
		case 2: 
			System.out.println(" independent tracks");
			return Format.SEQUENTIALLY_INDEPENDENT_TRACKS;
		default : 
			System.out.println(" an unknown format");
			return Format.UNKNOWN;
		}
	}

	private static int getHeaderNTracks(BufferedInputStream midFileStream) throws IOException {
		byte[] nTracksBytes = new byte[2];
		midFileStream.read(nTracksBytes);
		byte[] nTracksBytesInt = new byte[4];
		nTracksBytesInt[0] = 0x00;
		nTracksBytesInt[1] = 0x00;
		nTracksBytesInt[2] = nTracksBytes[0];
		nTracksBytesInt[3] = nTracksBytes[1];
		ByteBuffer bb = ByteBuffer.wrap(nTracksBytesInt);
		int nTracks = bb.getInt();
		System.out.print("Header has ");
		System.out.print(nTracks);
		System.out.println(" tracks");
		return nTracks;
	}

	private static int getHeaderDivision(BufferedInputStream midFileStream) throws IOException {
		byte[] divisionBytes = new byte[2];
		midFileStream.read(divisionBytes);
		byte[] divisionBytesInt = new byte[4];
		divisionBytesInt[0] = 0x00;
		divisionBytesInt[1] = 0x00;
		divisionBytesInt[2] = divisionBytes[0];
		divisionBytesInt[3] = divisionBytes[1];
		ByteBuffer bb = ByteBuffer.wrap(divisionBytesInt);		
		return bb.getInt();
	}

	/**         Gets the next chunk from the BufferedInputStream containing the mid file.
	 *  @param  midFileStream as the BufferedInputStream containing the mid file.
	 *  @return The next chunk from the BufferedInputStream containing the mid file.
	 *  @throws IOException if there is a I\O error while reading from the BufferedInputStream containing the mid file.
	 */
	private static Optional<MidChunkBuilder> getChunk(BufferedInputStream midFileStream) throws IOException {
		MidChunkBuilder chunkBuilder = null;
		// Get chunk type
		byte[] chunkTypeBytes = new byte[4];
		midFileStream.read(chunkTypeBytes);
		ChunkType chunkType = getChunkType(chunkTypeBytes).orElse(null);
		if(chunkType != null) {
			if(chunkType == ChunkType.HEADER) {
				chunkBuilder = new MidHeaderBuilder();
				System.out.println("Chunk type is a header");
			} else if(chunkType == ChunkType.TRACK) {
				chunkBuilder = new MidTrackBuilder();
				System.out.println("Chunk type is a track");
			} else {
				chunkBuilder = new MidChunkBuilder();
			}
		} 
		// Get chunk length
		chunkBuilder.setLength(getChunkSize(midFileStream));
		System.out.print("Chunk length is ");
		System.out.print(chunkBuilder.getLength());
		System.out.println(" bytes");
		return Optional.of(chunkBuilder);
	}

	/**        Gets the chunk type from the byte array.
	 * @param  chunkTypeBytes as the byte array containing the chunk type.
	 * @return The chunk type from the byte array.
	 */
	static Optional<ChunkType> getChunkType(byte[] chunkTypeBytes) {
		if(chunkTypeBytes[0] == MidCs.MIDI_TYPE_HEADER[0] && 
				chunkTypeBytes[1] == MidCs.MIDI_TYPE_HEADER[1] &&
				chunkTypeBytes[2] == MidCs.MIDI_TYPE_HEADER[2] && 
				chunkTypeBytes[3] == MidCs.MIDI_TYPE_HEADER[3]) {
			return Optional.of(ChunkType.HEADER);
		} else if(chunkTypeBytes[0] == MidCs.MIDI_TYPE_TRACK[0] && 
				chunkTypeBytes[1] == MidCs.MIDI_TYPE_TRACK[1] &&
				chunkTypeBytes[2] == MidCs.MIDI_TYPE_TRACK[2] && 
				chunkTypeBytes[3] == MidCs.MIDI_TYPE_TRACK[3]) {
			return Optional.of(ChunkType.TRACK);
		} else {
			return Optional.of(ChunkType.UNKNOWN);
		}
	}


	private static Optional<MidEvent> getEvent(BufferedInputStream midFileStream) throws IOException {
		lengthToAdd = 0;
		int readLimit = 255;
		midFileStream.mark(readLimit);
		byte temp = (byte) midFileStream.read();
		lengthToAdd++;
		if(((temp & MidCs.RUNNING_STATUS_MASK) == MidCs.RUNNING_STATUS_MASK) || runningStatusInvalid) {
			runningStatus = temp;
			if(temp != 0xFF && temp != 0xF7 && temp != 0xF0) {
				runningStatusInvalid = false;
			}
		} else {
			midFileStream.reset();
			temp = runningStatus;
		}
		int channel = temp & MidCs.CHANNEL_MASK;
		if(temp >= (byte)0x80 && temp <= (byte)0x8F) {
			return getNoteOffEvent(channel, midFileStream);
		} else if(temp >= (byte)0x90 && temp <= (byte)0x99) {
			return getNoteOnEvent(channel, midFileStream);
		} else if(temp >= (byte)0xD0 && temp <= (byte)0xDF) {
			// TODO poly key pressure
		} else if(temp >= (byte)0xB0 && temp <= (byte)0xBF) {
			return getControlChange(channel, midFileStream);
		} else if(temp >= (byte)0xC0 && temp <= (byte)0xCF) {
			return getProgramChange(channel, midFileStream);
		} else if(temp >= (byte)0xD0 && temp <= (byte)0xDF) {
			// TODO channel pressure
		} else if(temp >= (byte)0xE0 && temp <= (byte)0xEF) {
			return getPitchBendEvent(channel, midFileStream);
		} else if(temp == (byte)0xFF) {
			runningStatusInvalid = true;
			return getMetaEvent(midFileStream);
		}
		midFileStream.reset();
		// TODO delete when done testing or keep for error checking
		System.out.println(midFileStream.read());
		System.out.println(midFileStream.read());
		System.out.println(midFileStream.read());
		return null;
	}

	private static Optional<MidEvent> getPitchBendEvent(int channel, BufferedInputStream midFileStream) throws IOException {
		byte lsb = (byte) midFileStream.read();
		lengthToAdd++;
		byte msb = (byte) midFileStream.read();
		lengthToAdd++;
		int pitchBendData = lsb | (msb << 7);
		System.out.print("Pitch bend changed to ");
		System.out.println(pitchBendData);
		return Optional.of(new MidEventPitchBend(channel, pitchBendData));
	}

	private static Optional<MidEvent> getProgramChange(int channel, BufferedInputStream midFileStream) throws IOException {
		int programNumber = midFileStream.read();
		lengthToAdd++;
		System.out.print("Program number changed to ");
		System.out.println(programNumber);
		return Optional.of(new MidEventProgramChange(channel, programNumber));
	}

	private static Optional<MidEvent> getControlChange(int channel, BufferedInputStream midFileStream) throws IOException {
		byte temp = (byte) midFileStream.read();
		lengthToAdd++;
		if(temp == 0x00) {
			// Bank Select MSB
			bankSelectMSB = (byte) midFileStream.read();
			lengthToAdd++;
			System.out.println("Bank select MSB");
			return Optional.empty();
		} else if(temp == 0x07) {
			// Volume change
			int volumeOutOf127 = midFileStream.read();
			lengthToAdd++;
			System.out.print("Volume changed to ");
			System.out.print(volumeOutOf127);
			System.out.println(" out of 127");
			return Optional.of(new MidEventVolumeChange(channel, volumeOutOf127));
		} else if(temp == 0x0A) { 
			// Pan change
			int panOutOf127 = midFileStream.read();
			lengthToAdd++;
			System.out.print("Pan changed to ");
			System.out.print(panOutOf127);
			System.out.println(" out of 127");
			return Optional.of(new MidEventPanChange(channel, panOutOf127));
		} else if(temp == 0x20) {
			// Bank select LSB
			byte bankSelectLSB = (byte) midFileStream.read();
			lengthToAdd++;
			int bankSelect = (bankSelectMSB << 7) | bankSelectLSB;
			bankSelectMSB = -1;
			System.out.println("Bank select LSB");
			return Optional.of(new MidEventBankSelect(channel, bankSelect));
		} else if(temp >= 0x20 && temp <= 0x3F) {
			// Controller number followed by 
			// data entry LSB
			int controllerNumber = temp & MidCs.CONTROLLER_MASK;
			byte dataEntryLSB = (byte) midFileStream.read();
			lengthToAdd++;
			System.out.print("Controller number ");
			System.out.print(controllerNumber);
			System.out.print(" set to ");
			System.out.println(dataEntryLSB);
			return Optional.of(new MidEventDataEntryLSB(channel, controllerNumber, dataEntryLSB));
		} else if(temp == 0x40) {
			// Damp change
			int dampOutOf127 = midFileStream.read();
			lengthToAdd++;
			System.out.print("Damp changed to ");
			System.out.print(dampOutOf127);
			System.out.println(" out of 127");
			return Optional.of(new MidEventDamp(channel, dampOutOf127));
		} else if(temp == 0x64) {
			// Parameter LSB
			parameterLSB = (byte) midFileStream.read();
			lengthToAdd++;
			System.out.print("Parameter lsb is ");
			System.out.println(parameterLSB);
			if(parameterMSB == -1) {
				return Optional.empty();
			}
			return getRegisteredParameterEvent(channel);
		} else if(temp == 0x65) {
			// Parameter MSB
			parameterMSB = (byte) midFileStream.read();
			lengthToAdd++;
			System.out.print("Parameter msb is ");
			System.out.println(parameterMSB);
			if(parameterLSB == -1) {
				return Optional.empty();
			}
			return getRegisteredParameterEvent(channel);
		} else if(temp == 0x06) {
			byte dataEntryMSB = (byte) midFileStream.read();
			lengthToAdd++;
			System.out.print("Data entry msb is ");
			System.out.println(dataEntryMSB);
			return Optional.of(new MidEventDataEntryMSB(channel, dataEntryMSB));
		}		return null;
	}

	private static Optional<MidEvent> getNoteOnEvent(int channel, BufferedInputStream midFileStream) throws IOException {
		int halfStepsFromMiddleC = (midFileStream.read() - 0x3C);
		int middleCIndex = scale.middleAIndex+3;
		int noteIndex = middleCIndex + halfStepsFromMiddleC;
		Note note = scale.notes.get(noteIndex);
		int velocity = midFileStream.read();
		lengthToAdd+=2;
		System.out.print("Note ");
		System.out.print(note.getName());
		System.out.print(" with velocity ");
		System.out.print(velocity);
		System.out.println(" was turned on");
		return Optional.of(new MidEventNoteOn(channel, note, velocity));
	}

	private static Optional<MidEvent> getNoteOffEvent(int channel, BufferedInputStream midFileStream) throws IOException {
		int halfStepsFromMiddleC = (midFileStream.read() - 0x3C);
		int middleCIndex = scale.middleAIndex+3;
		int noteIndex = middleCIndex + halfStepsFromMiddleC;
		Note note = scale.notes.get(noteIndex);
		int velocity = midFileStream.read();
		lengthToAdd+=2;
		System.out.print("Note ");
		System.out.print(note.getName());
		System.out.print(" was turned off");
		System.out.print(" with velocity ");
		System.out.println(velocity);
		return Optional.of(new MidEventNoteOff(channel, note, velocity));
	}

	private static Optional<MidEvent> getMetaEvent(BufferedInputStream midFileStream) throws IOException {
		// Event type
		byte temp = (byte) midFileStream.read();
		long length = getVariableLengthQuantity(midFileStream);
		long tempLengthToAdd = lengthToAdd;
		lengthToAdd += tempLengthToAdd;
		lengthToAdd++;
		Optional<MidEvent> optional = null;
		if(temp == 0x00) {
			// TODO sequence number
		} else if(temp == 0x01) {
			// TODO text event
		} else if(temp == 0x02) {
			// TODO copyright notice
		} else if(temp == 0x05) {
			// TODO lyric event
		} else if(temp == 0x06) {
			// TODO marker event
		} else if(temp == 0x07) {
			// TODO cue point event
		} else if(temp == 0x03 ||temp == 0x04 || temp == 0x09) {
			// String events
			int tempChar;
			StringBuilder sb = new StringBuilder();
			long tl = length;
			for(int i = 0; i < tl; i++) {
				tempChar = midFileStream.read();
				lengthToAdd++;
				length--;
				sb.append((char)tempChar);
			}
			if(temp == 0x03) {
				System.out.print("Track name is ");
				System.out.println(sb);
				optional = Optional.of(new MidEventTrackName(sb.toString()));
			} else if(temp == 0x04) {
				System.out.print("Instrument name is ");
				System.out.println(sb);
				optional = Optional.of(new MidEventInstrumentName(sb.toString()));
			} else if(temp == 0x09) {
				System.out.print("Device name is ");
				System.out.println(sb);
				optional = Optional.of(new MidEventDeviceName(sb.toString()));
			}
		} else if(temp == 0x20) {
			// TODO channel prefix event
		} else if(temp == 0x2F) {
			System.out.println("End of track");
			optional = Optional.of(new MidEventEndOfTrack());
		} else if(temp == 0x51) {
			// Tempo
			byte[] uSecPerQuarterNoteBytes = new byte[3];
			midFileStream.read(uSecPerQuarterNoteBytes);
			lengthToAdd+=3;
			length-=3;
			byte[] uSecPerQuarterNoteBytesBytesInt = new byte[4];
			uSecPerQuarterNoteBytesBytesInt[0] = 0x00;
			uSecPerQuarterNoteBytesBytesInt[1] = uSecPerQuarterNoteBytes[0];
			uSecPerQuarterNoteBytesBytesInt[2] = uSecPerQuarterNoteBytes[1];
			uSecPerQuarterNoteBytesBytesInt[3] = uSecPerQuarterNoteBytes[2];
			ByteBuffer bb = ByteBuffer.wrap(uSecPerQuarterNoteBytesBytesInt);
			double beatsPerMinute = (60000000.0/((double)(bb.getInt())));
			if(tempo == null) {
				tempo = new Tempo(beatsPerMinute);
			}
			System.out.print("Tempo at ");
			System.out.print(beatsPerMinute);
			System.out.println(" bpm");
			optional = Optional.empty();
		} else if(temp == 0x54) {
			int hours = midFileStream.read();
			int minutes = midFileStream.read();
			int seconds  = midFileStream.read();
			int frames = midFileStream.read();
			int nHundredthFrames = midFileStream.read();
			lengthToAdd+=5;
			length-=5;
			System.out.print("Track starts at ");
			System.out.print(hours);
			System.out.print(":");
			System.out.print(minutes);
			System.out.print(":");
			System.out.print(seconds);
			System.out.print(":");
			System.out.print(frames);
			System.out.print(":");
			System.out.println(nHundredthFrames);
			startTime = new MidSMTPEOffset(hours, minutes, seconds, frames, nHundredthFrames);
			optional = Optional.empty();
		} else if(temp == 0x58) {
			// Time signature
			int beatsPerBar = midFileStream.read();
			double beatUnit = midFileStream.read();
			ts = new TimeSignature(Math.pow(2.0, beatUnit), beatsPerBar);
			clocksPerMetronomeTick = midFileStream.read();
			n32ndNotesPer24Clocks = midFileStream.read();
			lengthToAdd+=4;
			length-=4;
			System.out.print("The time signature is ");
			System.out.println(ts);
			System.out.print("There are ");
			System.out.print(clocksPerMetronomeTick);
			System.out.println(" midi clocks per metronome click");
			System.out.print("There are ");
			System.out.print(n32ndNotesPer24Clocks);
			System.out.println(" 32nd notes per 24 midi clocks");
			optional = Optional.empty();
		} else if(temp == 0x59) {
			byte nSharps = (byte) midFileStream.read();
			byte keyType = (byte) midFileStream.read();
			lengthToAdd+=2;
			length-=2;
			int middleA = 440;
			int octavesBelowMiddleA = 9;
			int maxFrequency = 20000;
			TwelveToneEqualTemperament twelveToneEqualTemperament = new TwelveToneEqualTemperament(
					middleA, octavesBelowMiddleA, maxFrequency);
			scale = twelveToneEqualTemperament;
			System.out.println("Scale created");
			optional = Optional.empty();
		} else if(temp == 0x7F) {
			// TODO Seqeuncer specific
			// See manufacturers published codes
		}
		while(length > 0) {
			System.out.println("Skipping unknown data");
			midFileStream.read();
			length--;
		}
		return optional;
	}

	private static Optional<MidEvent> getRegisteredParameterEvent(int channel) {
		if(parameterMSB == 0x00) {
			if(parameterLSB == 0x00) {
				parameterMSB = -1;
				parameterLSB = -1;
				System.out.println("Pitch bend sensitivity event");
				return Optional.of(new MidEventPitchBendSensitivity(channel));
			}
		}
		return null;
	}

	public static long getVariableLengthQuantity(BufferedInputStream midFileStream) throws IOException {
		lengthToAdd = 0;
		ArrayList<Byte> tempBytes = new ArrayList<>();
		byte tempByte = (byte) midFileStream.read();
		lengthToAdd++;
		while((tempByte & MidCs.VAR_LENGTH_QUANTITY_MASK) == MidCs.VAR_LENGTH_QUANTITY_MASK) {
			tempByte = (byte) (tempByte & ~MidCs.VAR_LENGTH_QUANTITY_MASK);
			tempBytes.add(tempByte);
			tempByte = (byte) midFileStream.read();
			lengthToAdd++;
		}
		tempBytes.add(tempByte);
		byte[] vlq = new byte[tempBytes.size()];
		int i = 0;
		for(byte b : tempBytes) {
			vlq[i++] = b;
		}
		byte[] vlqLong = new byte[8];
		for(int j = 0; j < vlqLong.length - vlq.length; j++) {
			vlqLong[j] = 0x00;
		}
		for(int j =  vlqLong.length - vlq.length; j <  vlqLong.length; j++) {
			vlqLong[j] = vlq[j-(vlqLong.length - vlq.length)];
		}
		ByteBuffer bb = ByteBuffer.wrap(vlqLong);
		return bb.getLong();
	}

	/**         Gets the chunk size from the BufferedInputStream containing the mid file.	 
	 *  @param  midFileStream as the BufferedInputStream containing the mid file.
	 *  @return The chunk size from the BufferedInputStream containing the mid file.
	 *  @throws IOException if there is a I\O error while reading from the BufferedInputStream containing the mid file.
	 */
	private static long getChunkSize(BufferedInputStream midFileStream) throws IOException {
		byte[] sizeBytes = new byte[4];
		midFileStream.read(sizeBytes);
		byte[] sizeBytesLong = new byte[8];
		sizeBytesLong[0] = 0x00;
		sizeBytesLong[1] = 0x00;
		sizeBytesLong[2] = 0x00;
		sizeBytesLong[3] = 0x00;
		sizeBytesLong[4] = sizeBytes[0];
		sizeBytesLong[5] = sizeBytes[1];
		sizeBytesLong[6] = sizeBytes[2];
		sizeBytesLong[7] = sizeBytes[3];
		ByteBuffer bb = ByteBuffer.wrap(sizeBytesLong);
		return bb.getLong();
	}

	/**        Verifies the File exists and can be read.
	 * @param  midFile as the file to be verified.
	 * @return True if the File exists and can be read or else false.
	 */
	static boolean verifyMidFile(File midFile) {
		return (midFile.exists() && midFile.canRead()) ? true : false;
	}

}
