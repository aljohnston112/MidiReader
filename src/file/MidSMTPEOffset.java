package file;

public final class MidSMTPEOffset {

	final int hours;
	
	final int minutes;
	
	final int seconds;
	
	final int frames;
	
	final int nHundredthFrames;
	
	private MidSMTPEOffset() {
		throw new AssertionError("The default MidSMTPEOffset constructor is not supported");
	}
	
	public MidSMTPEOffset(int hours, int minutes, int seconds, int frames, int nHundredthFrames) {
		this.hours = hours;
		this.minutes = minutes;
		this.seconds = seconds;
		this.frames = frames;
		this.nHundredthFrames = nHundredthFrames;
	}
	
}
