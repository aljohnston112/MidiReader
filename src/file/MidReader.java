package file;

import java.io.File;
import java.io.FileInputStream;
import java.io.BufferedInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.activation.UnsupportedDataTypeException;

import file.MidChunk.ChunkType;
import file.MidChunkHeader.Format;
import notes.MajorScale;
import notes.MinorScale;
import notes.Note;
import notes.Scale;
import notes.TwelveToneEqualTemperament;
import rhythm.Tempo;
import rhythm.TimeSignature;

public class MidReader {

	static List<MidChunkTrack> tracks = new ArrayList<>();

	static File midFile;

	static long lengthToAdd = 0;

	static byte runningStatus;

	static TimeSignature ts;

	static int clocksPerMetTick;

	static int n32ndNotesPer24Clocks;

	static Scale scale;

	static Tempo tempo;

	static int volumeOutOf127;

	static int panOutOf127;

	static int dampOutOf127;	

	public static void main(String[] args) {
		System.out.println("MidReader started");
		midFile = new File("C:\\Users\\aljoh\\Downloads\\Undertale MIDI\\Undertale MIDI\\Undertale - Ooo.mid");
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

	private static void readMidFile(BufferedInputStream midFileStream) throws IOException {
		while(midFileStream.available() > 0) {
			MidChunk chunk = getChunk(midFileStream).orElse(null);
			long length = 0;
			if(chunk instanceof MidChunkHeader) {
				((MidChunkHeader) chunk).setFormat(getHeaderFormat(midFileStream));
				length += 2;
				((MidChunkHeader) chunk).setNTracks(getHeaderNTracks(midFileStream));
				length += 2;
				((MidChunkHeader) chunk).setDivision(getHeaderDivision(midFileStream));
				length += 2;
				if(length < chunk.getLength()) {
					System.out.println("Skipping over the rest of the header");
				}
				while(length < chunk.getLength()) {
					midFileStream.read();
					length++;
				}
			} else if(chunk instanceof MidChunkTrack) {
				while(length < chunk.getLength()) {
					long ticks = getVariableLengthQuantity(midFileStream);
					length += lengthToAdd;
					System.out.print("Track's next event is ");
					System.out.print(ticks);
					System.out.println(" ticks from the last event");
					MidEvent event = getEvent(midFileStream).orElse(null);
					if(event != null) {
						event.setTicksFromLastEvent(ticks);
					}
					length += lengthToAdd;
					if(event == null) {

					} else if(event instanceof MidEndOfTrackEvent) {
						while(length < chunk.getLength()) {
							midFileStream.read();
							length++;
						}
					} else if(event instanceof MidNoteOnEvent || event instanceof MidNoteOffEvent) {
						((MidChunkTrack) chunk).addEvent(ticks, event);
					}
				}
				tracks.add((MidChunkTrack)chunk);
			} else {
				while(length < chunk.getLength()) {
					midFileStream.read();
					length++;
				}
			}
		}
	}

	private static Optional<MidEvent> getEvent(BufferedInputStream midFileStream) throws IOException {
		lengthToAdd = 0;
		midFileStream.mark(0xFF);
		byte temp = (byte) midFileStream.read();

		if((temp & 0b10000000) == 0b10000000) {
			runningStatus = temp;
		} else {
			midFileStream.reset();
			temp = runningStatus;
		}

		lengthToAdd++;
		if(temp >= (byte)0x80 && temp <= (byte)0x89) {
			int channel = temp & 0b00001111;
			int fromMiddleC = (midFileStream.read() - 0x3C);
			int middleCIndex = scale.middleAIndex+3;
			int noteIndex = middleCIndex + fromMiddleC;
			Note note = scale.notes.get(noteIndex);
			int velocity = midFileStream.read();
			System.out.print("Note ");
			System.out.print(note.getName());
			System.out.println(" was turned off");
			System.out.print(" with velocity ");
			System.out.print(velocity);
			return Optional.of(new MidNoteOffEvent(note, velocity));
		} else if(temp >= (byte)0x90 && temp <= (byte)0x99) {
			int channel = temp & 0b00001111;
			int fromMiddleC = (midFileStream.read() - 0x3C);
			int middleCIndex = scale.middleAIndex+3;
			int noteIndex = middleCIndex + fromMiddleC;
			Note note = scale.notes.get(noteIndex);
			int velocity = midFileStream.read();
			System.out.print("Note ");
			System.out.print(note.getName());
			System.out.print(" with velocity ");
			System.out.print(velocity);
			System.out.println(" was turned on");
			return Optional.of(new MidNoteOnEvent(note, velocity));
		} else if(temp >= (byte)0xB0 && temp <= (byte)0xB9) {
			temp = (byte) midFileStream.read();
			lengthToAdd++;
			if(temp == 0x00) {
				int controllerNumber = midFileStream.read();
				lengthToAdd++;
				System.out.println("Bank select MSB");
				return Optional.empty();
			} else if(temp == 0x07) {
				volumeOutOf127 = midFileStream.read();
				lengthToAdd++;
				System.out.print("Volume changed to ");
				System.out.print(volumeOutOf127);
				System.out.println(" out of 127");
				return Optional.empty();
			} else if(temp == 0x0A) { 
				panOutOf127 = midFileStream.read();
				lengthToAdd++;
				System.out.print("Pan changed to ");
				System.out.print(panOutOf127);
				System.out.println(" out of 127");
				return Optional.empty();
			} else if(temp == 0x20) {
				int controllerNumber = midFileStream.read();
				lengthToAdd++;
				System.out.println("Bank select LSB");
				return Optional.empty();
			} else if(temp == 0x40) {
				dampOutOf127 = midFileStream.read();
				lengthToAdd++;
				System.out.print("Damp changed to ");
				System.out.print(dampOutOf127);
				System.out.println(" out of 127");
				return Optional.empty();
			}
		} else if(temp >= (byte)0xC0 && temp <= (byte)0xC9) {
			int programNumber = midFileStream.read();
			lengthToAdd+=2;
			System.out.println("Program number change");
			return Optional.empty();
		} else if(temp == (byte)0xFF) {
			temp = (byte) midFileStream.read();
			lengthToAdd++;
			if(temp == 0x03 ||temp == 0x04 || temp == 0x09) {
				long tempLengthToAdd = lengthToAdd;
				long nameLength = getVariableLengthQuantity(midFileStream);
				lengthToAdd += tempLengthToAdd;
				int tempChar;
				StringBuilder sb = new StringBuilder();
				for(int i = 0; i < nameLength; i++) {
					tempChar = midFileStream.read();
					sb.append((char)tempChar);
				}
				lengthToAdd+=nameLength;
				if(temp == 0x03) {
					System.out.print("Track name is ");
				} else {
					System.out.print("Instrument name is ");
				}
				System.out.println(sb);
				return Optional.empty();
			} else if(temp == 0x2F) {
				temp = (byte) midFileStream.read();
				lengthToAdd++;
				if(temp == 0x00) {
					System.out.println("End of track");
					return Optional.of(new MidEndOfTrackEvent());
				}
			} else if(temp == 0x51) {
				temp = (byte) midFileStream.read();
				lengthToAdd++;
				if(temp == 0x03) {
					byte[] uSecPerQuarterNoteBytes = new byte[3];
					midFileStream.read(uSecPerQuarterNoteBytes);
					lengthToAdd+=3;
					byte[] uSecPerQuarterNoteBytesBytesInt = new byte[4];
					uSecPerQuarterNoteBytesBytesInt[0] = 0x00;
					uSecPerQuarterNoteBytesBytesInt[1] = uSecPerQuarterNoteBytes[0];
					uSecPerQuarterNoteBytesBytesInt[2] = uSecPerQuarterNoteBytes[1];
					uSecPerQuarterNoteBytesBytesInt[3] = uSecPerQuarterNoteBytes[2];
					ByteBuffer bb = ByteBuffer.wrap(uSecPerQuarterNoteBytesBytesInt);
					double beatsPerMinute = (60000000.0/((double)(bb.getInt())));
					tempo = new Tempo(beatsPerMinute);
					System.out.println(tempo);
					return Optional.empty();
				}
			} else if(temp == 0x54) {
				temp = (byte) midFileStream.read();
				lengthToAdd++;
				if(temp == 0x05) {
					System.out.print("Track starts at ");
					System.out.print(midFileStream.read());
					System.out.print(":");
					System.out.print(midFileStream.read());
					System.out.print(":");
					System.out.print(midFileStream.read());
					System.out.print(":");
					System.out.print(midFileStream.read());
					System.out.print(":");
					System.out.println(midFileStream.read());
					lengthToAdd+=5;
					return Optional.empty();
				}
			} else if(temp == 0x58) {
				temp = (byte) midFileStream.read();
				lengthToAdd++;
				if(temp == 0x04) {
					int beatsPerBar = midFileStream.read();
					double beatUnit = midFileStream.read();
					ts = new TimeSignature(beatUnit, beatsPerBar);
					clocksPerMetTick = midFileStream.read();
					n32ndNotesPer24Clocks = midFileStream.read();
					lengthToAdd+=4;
					System.out.print("The time signature is ");
					System.out.println(ts);
					System.out.print("There are ");
					System.out.print(clocksPerMetTick);
					System.out.println(" midi clocks per metronome click");
					System.out.print("There are ");
					System.out.print(n32ndNotesPer24Clocks);
					System.out.println(" 32nd notes per 24 midi clocks");
					return Optional.empty();
				}
			} else if(temp == 0x59) {
				temp = (byte) midFileStream.read();
				lengthToAdd++;
				if(temp == 0x02) {
					int nSharps = midFileStream.read();
					byte keyType = (byte) midFileStream.read();
					lengthToAdd+=2;
					TwelveToneEqualTemperament twelveToneEqualTemperament = new TwelveToneEqualTemperament(440, 48000);
					if(keyType == 0x00) {
						// Major key
						createMajorScale(twelveToneEqualTemperament, nSharps);
					} else if(keyType == 0x01) {
						// Minor key
						createMinorScale(twelveToneEqualTemperament, nSharps);
					}
					System.out.println("Scale created");
					return Optional.empty();
				}
			}
		}
		midFileStream.reset();
		System.out.println(midFileStream.read());
		System.out.println(midFileStream.read());
		System.out.println(midFileStream.read());
		return null;
	}

	private static void createMinorScale(TwelveToneEqualTemperament twelveToneEqualTemperament, int nSharps) {
		switch(nSharps) {
		case -7 :
			scale = new MinorScale(twelveToneEqualTemperament, "G#");
			break;
		case -6 :
			scale = new MinorScale(twelveToneEqualTemperament, "D#");
			break;
		case -5 :
			scale = new MinorScale(twelveToneEqualTemperament, "A#");
			break;
		case -4 :
			scale = new MinorScale(twelveToneEqualTemperament, "F");
			break;
		case -3 :
			scale = new MinorScale(twelveToneEqualTemperament, "C");
			break;
		case -2 :
			scale = new MinorScale(twelveToneEqualTemperament, "G");
			break;
		case -1 :
			scale = new MinorScale(twelveToneEqualTemperament, "D");
			break;
		case 0 :
			scale = new MinorScale(twelveToneEqualTemperament, "A");
			break;
		case 1 :
			scale = new MinorScale(twelveToneEqualTemperament, "E");
			break;
		case 2 :
			scale = new MinorScale(twelveToneEqualTemperament, "B");
			break;
		case 3 :
			scale = new MinorScale(twelveToneEqualTemperament, "F#");
			break;
		case 4 :
			scale = new MinorScale(twelveToneEqualTemperament, "C#");
			break;
		case 5 :
			scale = new MinorScale(twelveToneEqualTemperament, "G#");
			break;
		case 6 :
			scale = new MinorScale(twelveToneEqualTemperament, "D#");
			break;
		case 7 :
			scale = new MinorScale(twelveToneEqualTemperament, "A#");
			break;
		}
	}

	private static void createMajorScale(TwelveToneEqualTemperament twelveToneEqualTemperament, int nSharps) {
		switch(nSharps) {
		case -7 :
			scale = new MajorScale(twelveToneEqualTemperament, "B");
			break;
		case -6 :
			scale = new MajorScale(twelveToneEqualTemperament, "F");
			break;
		case -5 :
			scale = new MajorScale(twelveToneEqualTemperament, "C");
			break;
		case -4 :
			scale = new MajorScale(twelveToneEqualTemperament, "G#");
			break;
		case -3 :
			scale = new MajorScale(twelveToneEqualTemperament, "D#");
			break;
		case -2 :
			scale = new MajorScale(twelveToneEqualTemperament, "A#");
			break;
		case -1 :
			scale = new MajorScale(twelveToneEqualTemperament, "F");
			break;
		case 0 :
			scale = new MajorScale(twelveToneEqualTemperament, "C");
			break;
		case 1 :
			scale = new MajorScale(twelveToneEqualTemperament, "G");
			break;
		case 2 :
			scale = new MajorScale(twelveToneEqualTemperament, "D");
			break;
		case 3 :
			scale = new MajorScale(twelveToneEqualTemperament, "A");
			break;
		case 4 :
			scale = new MajorScale(twelveToneEqualTemperament, "E");
			break;
		case 5 :
			scale = new MajorScale(twelveToneEqualTemperament, "B");
			break;
		case 6 :
			scale = new MajorScale(twelveToneEqualTemperament, "F#");
			break;
		case 7 :
			scale = new MajorScale(twelveToneEqualTemperament, "C#");
			break;
		}
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
			return Format.SINGLE;
		case 1: 
			System.out.println(" synched tracks");
			return Format.SYNCHED;
		case 2: 
			System.out.println(" independent tracks");
			return Format.INDEPENDENT;
		default : 
			System.out.println(" an unsupported format");
			throw new UnsupportedDataTypeException(
					"Mid file from the BufferedInputStream passed to getHeaderFormat has an invalid format");
		}
	}

	public static long getVariableLengthQuantity(BufferedInputStream midFileStream) throws IOException {
		lengthToAdd = 0;
		ArrayList<Byte> tempBytes = new ArrayList<>();
		byte tempByte = (byte) midFileStream.read();
		lengthToAdd++;
		while((tempByte & 0b10000000) == 0b10000000) {
			tempByte = (byte) (tempByte & 0b01111111);
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

	/**         Gets the next chunk from the BufferedInputStream containg the mid file.
	 *  @param  midFileStream as the BufferedInputStream containing the mid file.
	 *  @return The next chunk from the BufferedInputStream containing the mid file.
	 *  @throws IOException if there is a I\O error while reading from the BufferedInputStream containing the mid file.
	 */
	private static Optional<MidChunk> getChunk(BufferedInputStream midFileStream) throws IOException {
		MidChunk chunk = null;
		// Get chunk type
		byte[] chunkTypeBytes = new byte[4];
		midFileStream.read(chunkTypeBytes);
		ChunkType chunkType = getChunkType(chunkTypeBytes).orElse(null);
		if(chunkType != null) {
			if(chunkType == ChunkType.HEADER) {
				chunk = new MidChunkHeader();
				System.out.println("Chunk type is a header");
			} else if(chunkType == ChunkType.TRACK) {
				chunk = new MidChunkTrack();
				System.out.println("Chunk type is a track");
			}
		} else {
			chunk = new MidChunk();
		}
		// Get chunk length
		chunk.setLength(getChunkSize(midFileStream));
		System.out.print("Chunk length is ");
		System.out.print(chunk.getLength());
		System.out.println(" bytes");
		return Optional.of(chunk);
	}

	/**        Gets the chunk type from the byte array.
	 * @param  chunkTypeBytes as the byte array containing the chunk type.
	 * @return The chunk type from the byte array.
	 */
	static Optional<ChunkType> getChunkType(byte[] chunkTypeBytes) {
		if(chunkTypeBytes[0] == 0x4d && chunkTypeBytes[1] == 0x54 &&
				chunkTypeBytes[2] == 0x68 && chunkTypeBytes[3] == 0x64) {
			return Optional.of(ChunkType.HEADER);
		} else if(chunkTypeBytes[0] == 0x4d && chunkTypeBytes[1] == 0x54 &&
				chunkTypeBytes[2] == 0x72 && chunkTypeBytes[3] == 0x6B) {
			return Optional.of(ChunkType.TRACK);
		} else {
			return Optional.empty();
		}
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
