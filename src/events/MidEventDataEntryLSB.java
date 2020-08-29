package events;

public final class MidEventDataEntryLSB extends MidEvent {

	final int dataEntryLSB;

	final int channel;
	
	final int controllerNumber;
	
	private MidEventDataEntryLSB() {
		throw new AssertionError("Default MidEventDataEntryLSB constructor is unsupported");
	}
	
	public MidEventDataEntryLSB(int channel, int controllerNumber,byte dataEntryLSB) {
		this.channel = channel;
		this.controllerNumber = controllerNumber;
		this.dataEntryLSB = dataEntryLSB;
	}

}
