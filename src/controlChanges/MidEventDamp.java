package controlChanges;

import channelVoiceMessages.MidEventControlChange;

public final class MidEventDamp extends MidEventControlChange {

	public final int dampOutOf127;	
	
	public MidEventDamp(int channel, int dampOutOf127) {
		super(channel, 64, dampOutOf127);
		this.dampOutOf127 = dampOutOf127;
	}
	
}
