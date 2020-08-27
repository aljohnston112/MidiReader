package midFileBuilder;

import java.util.ArrayList;
import java.util.List;

import chunks.MidHeader;
import chunks.MidTrack;

public class MidFileBuilder {

	private MidHeader header;

	private List<MidTrack> tracks = new ArrayList<>();
	
	public MidHeader getHeader() {
		return header;
	}

	public void setHeader(MidHeader header) {
		this.header = header;
	}

	public void addTrack(MidTrack track) {
		this.tracks.add(track);
	}
	
}
