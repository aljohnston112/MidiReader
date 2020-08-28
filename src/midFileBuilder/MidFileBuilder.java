package midFileBuilder;

import java.util.ArrayList;
import java.util.List;

import chunks.MidHeader;
import chunks.MidTrack;
import file.MidSMTPEOffset;
import rhythm.Tempo;
import rhythm.TimeSignature;

public class MidFileBuilder {

	private MidHeader header;

	private List<MidTrack> tracks = new ArrayList<>();
	
	private Tempo tempo = null;
	
	private TimeSignature ts = null;
	
	private int n32ndNotesPer24Clocks = -1;
	
	private int clocksPerMetronomeTick = -1;
	
	private MidSMTPEOffset startTime = null;
	
	public MidHeader getHeader() {
		return header;
	}

	public void setHeader(MidHeader header) {
		this.header = header;
	}

	public void addTrack(MidTrack track) {
		this.tracks.add(track);
	}

	public void addTempo(Tempo tempo) {
		this.tempo = tempo;		
	}

	public void addStartTime(MidSMTPEOffset startTime) {
		this.startTime = startTime;
	}

	public void addTimeSignature(TimeSignature ts) {
		this.ts = ts;
	}

	public void setN32ndNotesPer24Clocks(int n32ndNotesPer24Clocks) {
		this.n32ndNotesPer24Clocks = n32ndNotesPer24Clocks;
	}

	public void setClocksPerMetronomeTick(int clocksPerMetronomeTick) {
		this.clocksPerMetronomeTick = clocksPerMetronomeTick;
	}
	
}
