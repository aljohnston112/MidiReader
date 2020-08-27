package midFileBuilder;

import chunks.MidChunk;

/** MidChunkBuilder is used to build MidiChunk objects.
 *  @author Alexander Johnston
 *  @since  2020
 */
public class MidChunkBuilder {

	protected long length = -1;
	
	/**       Sets the length of the MidiChunk to be built.
	 * @param chunkSize as the length of the chunk in bytes.
	 */
	public void setLength(long chunkSize) {
		if(chunkSize < 1) {
			throw new IllegalArgumentException("chunkSize passed to setLength() must be greater than 0");
		}
		
		length = chunkSize;
	}

	/**
	 * @return The length to be used when building a MidiChunk.
	 */
	public long getLength() {
		return length;
	}
	
	/**        Builds a MidChunk with the parameters that have been set.
	 *         Length must be set before this method is called.
	 * @return A MidChunk with the set parameters.
	 * @throws IllegalArgumentException if the length has not been set.
	 */
	public MidChunk build() {
		if(length == -1) {
			throw new IllegalArgumentException("The length for the MidChunk has not been set");
		}
		return new MidChunk(length);
	}

}
