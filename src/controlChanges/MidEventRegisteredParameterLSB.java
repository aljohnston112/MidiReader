package controlChanges;

import channelVoiceMessages.MidEventControlChange;

public final class MidEventRegisteredParameterLSB extends MidEventControlChange {

	public MidEventRegisteredParameterLSB(int channel, int lsb) {
		super(channel, 100, lsb);
	}

}
