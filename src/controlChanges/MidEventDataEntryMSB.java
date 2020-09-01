package controlChanges;

import channelVoiceMessages.MidEventControlChange;

public final class MidEventDataEntryMSB extends MidEventControlChange {

	public final int dataEntryMSB;
	
	private MidEventDataEntryMSB() {
		super(-1, -1, -1);
		throw new AssertionError("Default MidEventDataEntryMSB constructor is unsupported");
	}
	
	public MidEventDataEntryMSB(int channel, byte dataEntryMSB) {
		super(channel, 6, dataEntryMSB);
		this.dataEntryMSB = dataEntryMSB;
	}

}
