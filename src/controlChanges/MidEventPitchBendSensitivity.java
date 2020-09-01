package controlChanges;

import channelVoiceMessages.MidEventControlChange;
import events.MidEvent;

public final class MidEventPitchBendSensitivity extends MidEventControlChange {
	
	public final int channel;
	
	private MidEventPitchBendSensitivity() {
		super(-1, -1, -1);
		throw new AssertionError("Default MidEventPitchBendSensitivity constructor is unsupported");
	}
	
	public MidEventPitchBendSensitivity(int channel) {
		this.channel = channel;
	}
	
}