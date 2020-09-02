package controlChanges;

import channelVoiceMessages.MidEventControlChange;

public final class MidEventVolumeChange extends MidEventControlChange {
	
	public MidEventVolumeChange(int channel, int volumeOutOf127) {
		super(channel, 7, volumeOutOf127);
	}

}