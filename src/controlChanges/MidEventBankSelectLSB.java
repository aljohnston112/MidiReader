package controlChanges;

import channelVoiceMessages.MidEventControlChange;

public final class MidEventBankSelectLSB extends MidEventControlChange {

	public MidEventBankSelectLSB(int channel, int bankNumber) {
		super(channel, 0x20, bankNumber);
	}
	
}
