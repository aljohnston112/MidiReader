package file;

abstract public class MidChunk {

	public enum ChunkType {
		HEADER, TRACK
	}
	
	private long length;
	
	long getLength() {
		return length;
	}
	
	void setLength(long length) {
		this.length = length;
	}

}
