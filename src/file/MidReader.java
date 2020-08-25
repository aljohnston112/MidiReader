package file;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Optional;

import javax.activation.UnsupportedDataTypeException;

import file.MidChunk.ChunkType;

public class MidReader {

	static File midFile;

	public static void main(String[] args) {
		System.out.println("MidReader started");
		midFile = new File("C:\\Users\\aljoh\\Downloads\\Undertale MIDI\\Undertale MIDI\\Undertale - Ooo.mid");
		FileInputStream midFileStream = null;
		if(verifyMidFile(midFile)) {
			System.out.print("Mid file: ");
			System.out.print(midFile.getName());
			System.out.println(" exists and can be read");	
			try {
				midFileStream = new FileInputStream(midFile);
				readMidFile(midFileStream);
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

	private static void readMidFile(FileInputStream midFileStream) throws IOException {
		MidChunk chunk = getChunk(midFileStream).orElse(null);
	}
	
	public static long getVariableLengthQuantity(FileInputStream midFileStream) throws IOException {
		ArrayList<Byte> tempBytes = new ArrayList<>();
		byte tempByte = (byte) midFileStream.read();
		while((tempByte & 0b10000000) == 0b10000000) {
			tempByte = (byte) (tempByte & 0b01111111);
			tempBytes.add(tempByte);
			tempByte = (byte) midFileStream.read();
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

	/**         Gets the next chunk from the FileInputStream containg the mid file.
	 *  @param  midFileStream as the FileInputStream containing the mid file.
	 *  @return The next chunk from the FileInputStream containing the mid file.
	 *  @throws IOException if there is a I\O error while reading from the FileInputStream containing the mid file.
	 */
	private static Optional<MidChunk> getChunk(FileInputStream midFileStream) throws IOException {
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
			return Optional.of(null);
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
				chunkTypeBytes[2] == 0x72 && chunkTypeBytes[3] == 0x68) {
			return Optional.of(ChunkType.TRACK);
		} else {
			// TODO skip chunk
			return Optional.of(null);
		}
	}
	
	/**         Gets the chunk size from the FileInputStream containing the mid file.	 
	 *  @param  midFileStream as the FileInputStream containing the mid file.
	 *  @return The chunk size from the FileInputStream containing the mid file.
	 *  @throws IOException if there is a I\O error while reading from the FileInputStream containing the mid file.
	 */
	private static long getChunkSize(FileInputStream midFileStream) throws IOException {
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
