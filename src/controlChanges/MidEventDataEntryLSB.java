package controlChanges;


import channelVoiceMessages.MidEventControlChange;

public final class MidEventDataEntryLSB extends MidEventControlChange {
	
	public final int controllerNumber;
	
	public MidEventDataEntryLSB(int channel, int controllerNumber, byte dataEntryLSB) {
		super(channel, controllerNumber+32, dataEntryLSB);
		if(controllerNumber > 31 || controllerNumber < 0) {
			throw new IllegalArgumentException("int controllerNumber passed to MidEventDataEntryLSB constructor is out of range");
		}
		this.controllerNumber = controllerNumber;
	}

}
