package metaEvents;

public final class MidEventSMPTEOffset extends MidMetaEvent {
	
	public final byte hours;
	public final byte minutes;
	public final byte seconds;
	public final byte frames;
	public final byte oneHundredthsOfAFrame;
	
	public MidEventSMPTEOffset(byte hours, byte minutes, byte seconds, byte frames, byte oneHundredthsOfAFrame) {
		this.hours = hours;
		this.minutes = minutes;
		this.seconds = seconds;
		this.frames = frames;
		this.oneHundredthsOfAFrame = oneHundredthsOfAFrame;
	}

}