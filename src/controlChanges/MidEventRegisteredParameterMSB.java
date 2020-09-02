package controlChanges;

import channelVoiceMessages.MidEventControlChange;

public final class MidEventRegisteredParameterMSB extends MidEventControlChange {

	public MidEventRegisteredParameterMSB(int channel, int msb) {
		super(channel, 101, msb);
	}

}
