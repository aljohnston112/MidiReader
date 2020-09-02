package controlChanges;

import channelVoiceMessages.MidEventControlChange;

public final class MidEventBankSelectMSB extends MidEventControlChange {
	
	public MidEventBankSelectMSB(int channel, int bankNumber) {
		super(channel, 0, bankNumber);
	}

}