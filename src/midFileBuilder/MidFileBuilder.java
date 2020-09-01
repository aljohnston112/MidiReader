package midFileBuilder;

import java.util.ArrayList;
import java.util.List;

import chunks.MidHeader;
import chunks.MidTrack;
import midiFile.MidFile;
import midiFile.MidSMTPEOffset;
import rhythm.Tempo;
import rhythm.TimeSignature;

public class MidFileBuilder {

	private MidHeader header;

	private List<MidTrack> tracks = new ArrayList<>();
	
	private MidSMTPEOffset startTime = null;
	
	private Tempo tempo = null;

	private int n32ndNotesPer24Clocks = -1;
	
	private int clocksPerMetronomeTick = -1;
	
	private TimeSignature ts = null;
			
	public MidHeader getHeader() {
		return header;
	}

	public void setHeader(MidHeader header) {
		this.header = header;
	}
	
	public void addTrack(MidTrack track) {
		this.tracks.add(track);
	}
	
	public MidSMTPEOffset getStartTime() {
		return startTime;
	}

	public void setStartTime(MidSMTPEOffset startTime) {
		this.startTime = startTime;
	}

	public Tempo getTempo() {
		return tempo;
	}

	public void setTempo(Tempo tempo) {
		this.tempo = tempo;
	}
	
	public void setN32ndNotesPer24Clocks(int n32ndNotesPer24Clocks) {
		this.n32ndNotesPer24Clocks = n32ndNotesPer24Clocks;
	}
	
	public int getN32ndNotesPer24Clocks() {
		return n32ndNotesPer24Clocks;
	}
	
	public void setClocksPerMetronomeTick(int clocksPerMetronomeTick) {
		this.clocksPerMetronomeTick = clocksPerMetronomeTick;
	}

	public int getClocksPerMetronomeTick() {
		return clocksPerMetronomeTick;
	}
	
	public TimeSignature getTimeSignature() {
		return ts;
	}

	public void setTimeSignature(TimeSignature ts) {
		this.ts = ts;
	}
	
	public MidFile build() {
		return new MidFile(header, tracks, startTime, tempo, n32ndNotesPer24Clocks, clocksPerMetronomeTick, ts);
	}
	
}
