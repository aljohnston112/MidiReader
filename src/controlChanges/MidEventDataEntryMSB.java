package controlChanges;

import channelVoiceMessages.MidEventControlChange;

public final class MidEventDataEntryMSB extends MidEventControlChange {

	public final int dataEntryMSB;
	
	public MidEventDataEntryMSB(int channel, byte dataEntryMSB) {
		super(channel, 6, dataEntryMSB);
		this.dataEntryMSB = dataEntryMSB;
	}

}
