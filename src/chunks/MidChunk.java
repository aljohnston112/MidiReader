package chunks;

/**         MidChunk is a midi chunk.
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
	
	/**              Creates a midi chunk.
	 * @param length The length of the midi chunk in bytes.
	 * 
	 * @throws IllegalArgumentException If length is not at least 0.
	 */
	public MidChunk(long length) {
		if(length < 0) {
			throw new IllegalArgumentException("long length passed to MidChunk constructor must be at least 0");
		}
		this.length = length;
	}
	
	/**
	 *  @return The length of this midi chunk in bytes.
	 */
	long getLength() {
		return length;
	}

}
