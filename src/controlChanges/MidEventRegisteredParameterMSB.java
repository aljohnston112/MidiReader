package controlChanges;

import channelVoiceMessages.MidEventControlChange;

/**         MidEventRegisteredParameterMSB is a midi registered parameter msb event.
 *  @author Alexander Johnston
 *  @since  Copyright 2020
 */
public final class MidEventRegisteredParameterMSB extends MidEventControlChange {

	/**               Creates a midi registered parameter msb event.
	 * @param channel The channel to send the event to.
	 * @param lsb     The msb of the registered parameter.
	 */
	public MidEventRegisteredParameterMSB(int channel, int msb) {
		super(channel, 101, msb);
	}

}