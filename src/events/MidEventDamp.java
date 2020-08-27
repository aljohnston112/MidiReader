package events;

public final class MidEventDamp extends MidEvent {

	private final int dampOutOf127;	
	
	private final int channel;

	private MidEventDamp() {
		throw new AssertionError("Default MidEventDamp constructor is unsupported");
	}
	
	public MidEventDamp(int channel, int dampOutOf127) {
		this.channel = channel;
		this.dampOutOf127 = dampOutOf127;
	}

	public int getDampOutOf127() {
		return dampOutOf127;
	}

	public int getChannel() {
		return channel;
	}
	
}
