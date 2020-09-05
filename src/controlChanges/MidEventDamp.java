package controlChanges;

import channelVoiceMessages.MidEventControlChange;

/**         MidEventDamp is a midi damp event.
 *  @author Alexander Johnston
 *  @since  2020
 */
public final class MidEventDamp extends MidEventControlChange {
	
	/**                    Creates a midi damp event.
	 * @param channel      The channel to send the event to.
	 * @param dampOutOf127 The damping value.
	 */
	public MidEventDamp(int channel, int dampOutOf127) {
		super(channel, 64, dampOutOf127);
	}
	
}
