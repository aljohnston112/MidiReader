package events;

public final class MidEventDamp extends MidEvent {

	final int dampOutOf127;	
	
	final int channel;

	private MidEventDamp() {
		throw new AssertionError("Default MidEventDamp constructor is unsupported");
	}
	
	public MidEventDamp(int channel, int dampOutOf127) {
		this.channel = channel;
		this.dampOutOf127 = dampOutOf127;
	}
	
}
