package controlChanges;

import channelVoiceMessages.MidEventControlChange;

/**         MidEventRegisteredParameterLSB is a midi registered parameter lsb event.
 *  @author Alexander Johnston
 *  @since  Copyright 2020
 */
public final class MidEventRegisteredParameterLSB extends MidEventControlChange {

	/**               Creates a midi registered parameter lsb event.
	 * @param channel The channel to send the event to.
	 * @param lsb     The lsb of the registered parameter.
	 */
	public MidEventRegisteredParameterLSB(int channel, int lsb) {
		super(channel, 100, lsb);
	}

}
