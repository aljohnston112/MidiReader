package events;

public final class MidEventDataEntry extends MidEvent {

	private final int dataEntryMSB;

	private final int dataEntryLSB;

	private final int channel;
	
	private MidEventDataEntry() {
		throw new AssertionError("Default MidEventDataEntry constructor is unsupported");
	}
	
	public MidEventDataEntry(int channel, int dataEntryMSB, byte dataEntryLSB) {
		this.channel = channel;
		this.dataEntryMSB = dataEntryMSB;
		this.dataEntryLSB = dataEntryLSB;
	}

	public int getChannel() {
		return channel;
	}

	public int getDataEntryLSB() {
		return dataEntryLSB;
	}

	public int getDataEntryMSB() {
		return dataEntryMSB;
	}

}
