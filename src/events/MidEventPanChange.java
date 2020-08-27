package events;

public final class MidEventPanChange extends MidEvent {

	// 64 is middle
	private final int panOutOf127;
	
	private final int channel;
	
	private MidEventPanChange() {
		throw new AssertionError("Default MidEventPanChange constructor is unsupported");
	}
	
	public MidEventPanChange(int channel, int panOutOf127) {
		this.channel = channel;
		this.panOutOf127 = panOutOf127;
	}

	public int getPanOutOf127() {
		return panOutOf127;
	}

	public int getChannel() {
		return channel;
	}

}
