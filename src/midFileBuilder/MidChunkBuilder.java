package midFileBuilder;

public class MidChunkBuilder {

	protected long length;
	
	public void setLength(long chunkSize) {
		if(chunkSize < 1) {
			throw new IllegalArgumentException("chunkSize passed to setLength() must be greater than 0");
		}
		
		length = chunkSize;
	}

	public long getLength() {
		return length;
	}

}
