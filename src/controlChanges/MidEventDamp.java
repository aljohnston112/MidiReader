package controlChanges;

import java.io.ByteArrayOutputStream;

import channelVoiceMessages.MidEventControlChange;

public final class MidEventDamp extends MidEventControlChange {
	
	public MidEventDamp(int channel, int dampOutOf127) {
		super(channel, 64, dampOutOf127);
	}
	
}
