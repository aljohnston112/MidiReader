package controlChanges;

import channelVoiceMessages.MidEventControlChange;

public final class MidEventDataEntryLSB extends MidEventControlChange {

	public final int dataEntryLSB;
	
	public final int controllerNumber;
	
	private MidEventDataEntryLSB() {
		super(-1, -1, -1);
		throw new AssertionError("Default MidEventDataEntryLSB constructor is unsupported");
	}
	
	public MidEventDataEntryLSB(int channel, int controllerNumber, byte dataEntryLSB) {
		super(channel, controllerNumber+32, dataEntryLSB);
		this.controllerNumber = controllerNumber;
		this.dataEntryLSB = dataEntryLSB;
	}

}
