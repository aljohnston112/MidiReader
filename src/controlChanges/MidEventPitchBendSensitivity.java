package controlChanges;

import channelVoiceMessages.MidEventControlChange;

public final class MidEventPitchBendSensitivity extends MidEventControlChange {
	
	public MidEventPitchBendSensitivity(int channel) {
		super(channel, 100, -1);
	}
	
}