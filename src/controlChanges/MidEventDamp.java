package controlChanges;

import channelVoiceMessages.MidEventControlChange;
import events.MidEvent;

public final class MidEventDamp extends MidEventControlChange {

	public final int dampOutOf127;	
	
	private MidEventDamp() {
		super(-1, -1, -1);
		throw new AssertionError("Default MidEventDamp constructor is unsupported");
	}
	
	public MidEventDamp(int channel, int dampOutOf127) {
		super(channel, 64, dampOutOf127);
		this.dampOutOf127 = dampOutOf127;
	}
	
}
