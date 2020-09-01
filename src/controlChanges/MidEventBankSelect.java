package controlChanges;

import channelVoiceMessages.MidEventControlChange;

public final class MidEventBankSelect extends MidEventControlChange {

	public final int bankNumber;
	
	public MidEventBankSelect(int channel, int bankNumber) {
		super(channel, 0, bankNumber);
		this.bankNumber = bankNumber;
	}

}