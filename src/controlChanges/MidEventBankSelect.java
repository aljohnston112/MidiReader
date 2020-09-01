package controlChanges;

import channelVoiceMessages.MidEventControlChange;

public final class MidEventBankSelect extends MidEventControlChange {

	public final int bankNumber;
		
	private MidEventBankSelect() {
		super(-1, -1, -1);
		throw new AssertionError("Default MidEventBankSelect constructor is unsupported");
	}
	
	public MidEventBankSelect(int channel, int bankNumber) {
		super(channel, 0, bankNumber);
		this.bankNumber = bankNumber;
	}

}