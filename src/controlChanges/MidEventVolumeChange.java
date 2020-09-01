package controlChanges;

import channelVoiceMessages.MidEventControlChange;
import events.MidEvent;

public final class MidEventVolumeChange extends MidEventControlChange {

	public final int volumeOutOf127;
		
	private MidEventVolumeChange() {
		super(-1, -1, -1);
		throw new AssertionError("Default MidEventVolumeChange constructor is unsupported");
	}
	
	public MidEventVolumeChange(int channel, int volumeOutOf127) {
		super(channel, 7, volumeOutOf127);
		this.volumeOutOf127 = volumeOutOf127;
	}

}