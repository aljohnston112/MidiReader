package events;

public final class MidEventDataEntryLSB extends MidEvent {

	final int dataEntryLSB;

	final int channel;
	
	private MidEventDataEntryLSB() {
		throw new AssertionError("Default MidEventDataEntryLSB constructor is unsupported");
	}
	
	public MidEventDataEntryLSB(int channel, byte dataEntryLSB) {
		this.channel = channel;
		this.dataEntryLSB = dataEntryLSB;
	}

}
