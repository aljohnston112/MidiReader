package controlChanges;

import channelVoiceMessages.MidEventControlChange;

/**         MidEventVolumeChange is a midi volume event.
 *  @author Alexander Johnston
 *  @since  Copyright 2020
 */
public final class MidEventVolumeChange extends MidEventControlChange {
	
	/**                      Creates a midi volume event.
	 * @param channel        The channel to receive the event.
	 * @param volumeOutOf127 The volume value.
	 */
	public MidEventVolumeChange(int channel, int volumeOutOf127) {
		super(channel, 7, volumeOutOf127);
	}

}