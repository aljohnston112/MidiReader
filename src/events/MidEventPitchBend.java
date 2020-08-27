package events;

public final class MidEventPitchBend extends MidEvent {

	// 8192 means no bend
	// 16,383 is the max bend
	private final int value;
	
	private final int channel;
	
	private MidEventPitchBend() {
		throw new AssertionError("Default MidEventPitchBend constructor is unsupported");
	}
	
	public MidEventPitchBend(int channel, int value) {
		this.channel = channel;
		this.value = value;
	}

	public int getValue() {
		return value;
	}

	public int getChannel() {
		return channel;
	}

}