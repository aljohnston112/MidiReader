package events;

public final class MidEventDataEntryMSB extends MidEvent {

	final int dataEntryMSB;

	final int channel;
	
	private MidEventDataEntryMSB() {
		throw new AssertionError("Default MidEventDataEntryMSB constructor is unsupported");
	}
	
	public MidEventDataEntryMSB(int channel, byte dataEntryMSB) {
		this.channel = channel;
		this.dataEntryMSB = dataEntryMSB;
	}

}
