package events;

public final class MidEventPitchBendSensitivity extends MidEvent {
	
	private final int channel;
	
	private MidEventPitchBendSensitivity() {
		throw new AssertionError("Default MidEventPitchBendSensitivity constructor is unsupported");
	}
	
	public MidEventPitchBendSensitivity(int channel) {
		this.channel = channel;
	}

	public int getChannel() {
		return channel;
	}
	
}