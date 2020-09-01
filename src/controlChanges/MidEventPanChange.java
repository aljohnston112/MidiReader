package controlChanges;

import channelVoiceMessages.MidEventControlChange;

public final class MidEventPanChange extends MidEventControlChange {

	// 64 is middle
	public final int panOutOf127;
		
	private MidEventPanChange() {
		super(-1, -1, -1);
		throw new AssertionError("Default MidEventPanChange constructor is unsupported");
	}
	
	public MidEventPanChange(int channel, int panOutOf127) {
		super(channel, 10, panOutOf127);
		this.panOutOf127 = panOutOf127;
	}

}
