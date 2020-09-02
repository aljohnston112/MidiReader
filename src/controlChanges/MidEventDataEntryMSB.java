package controlChanges;

import channelVoiceMessages.MidEventControlChange;

public final class MidEventDataEntryMSB extends MidEventControlChange {
	
	public MidEventDataEntryMSB(int channel, byte dataEntryMSB) {
		super(channel, 6, dataEntryMSB);
	}

}
