package chunks;

/** MidChunk is a midi chunk.
 *  @author Alexander Johnston
 *  @since  2020
 */
public class MidChunk {
	
	/** The types of midi chunks
	 *  
	 */
	public enum ChunkType {
		HEADER, TRACK, UNKNOWN
	}
	
	// Length of this midi chunk in bytes
	final long length;
	
	private MidChunk() {
		throw new AssertionError("The default MidChunk constructor is not supported");
	}
	
	/**       Creates a midi chunk with a length in bytes.
	 * @param length as the the length of this midi chunk in bytes.
	 */
	public MidChunk(long length) {
		this.length = length;
	}
	
	/**
	 *  @return The length of this midi chunk in bytes.
	 */
	long getLength() {
		return length;
	}

}
