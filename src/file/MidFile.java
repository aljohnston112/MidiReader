package file;

import java.util.List;

import chunks.MidHeader;
import chunks.MidTrack;
import notes.TimedNote;
import rhythm.Tempo;
import rhythm.TimeSignature;

public final class MidFile {

	public final MidHeader header;
	public final List<MidTrack> tracks;
	public final MidSMTPEOffset startTime;
	public final Tempo tempo;
	public final int n32ndNotesPer24Clocks;
	public final int clocksPerMetronomeTick;
	public final TimeSignature ts;
	
	public MidFile(MidHeader header, List<MidTrack> tracks, MidSMTPEOffset startTime, Tempo tempo,
			int n32ndNotesPer24Clocks, int clocksPerMetronomeTick, TimeSignature ts) {
		this.header = header;
		this.tracks = tracks;
		this.startTime = startTime;
		this.tempo = tempo;
		this.n32ndNotesPer24Clocks = n32ndNotesPer24Clocks;
		this.clocksPerMetronomeTick = clocksPerMetronomeTick;
		this.ts = ts;
	}
	
	public double getTicksPerSecond() {
		double ticksPer = header.ticksPer;
		double ticksPerSecond;
		if(header.isSeconds) {
			ticksPerSecond = ticksPer;		
		} else {
			ticksPerSecond = ((double)ticksPer)/tempo.getQuarter();
		}
		return ticksPerSecond;
	}
	
	public void get3DNoteArray() {
		
	}
	
}
