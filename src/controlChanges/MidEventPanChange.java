package controlChanges;

import channelVoiceMessages.MidEventControlChange;

/**         MidEventPanChange is a midi pan event.
 *  @author Alexander Johnston
 *  @since  Copyright 2020
 */
public final class MidEventPanChange extends MidEventControlChange {

	/**                   Creates a midi pan event.
	 * @param channel     The channel to send the event to.
	 * @param panOutOf127 The pan value. 64 is the middle.
	 */
	public MidEventPanChange(int channel, int panOutOf127) {
		super(channel, 10, panOutOf127);
	}

}