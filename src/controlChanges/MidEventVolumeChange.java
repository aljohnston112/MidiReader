package controlChanges;

import channelVoiceMessages.MidEventControlChange;

public final class MidEventVolumeChange extends MidEventControlChange {

	public final int volumeOutOf127;
	
	public MidEventVolumeChange(int channel, int volumeOutOf127) {
		super(channel, 7, volumeOutOf127);
		this.volumeOutOf127 = volumeOutOf127;
	}

}