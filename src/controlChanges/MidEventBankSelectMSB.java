package controlChanges;

import channelVoiceMessages.MidEventControlChange;

/**         MidEventBankSelectMSB is a midi bank select msb event.
 *  @author Alexander Johnston
 *  @since  2020
 */
public final class MidEventBankSelectMSB extends MidEventControlChange {
	
	/**                     Creates a midi bank select lsb event.
	 * @param channel       The channel to send the event to.
	 * @param bankNumberMSB The msb of the bank number.
	 */
	public MidEventBankSelectMSB(int channel, int bankNumberMSB) {
		super(channel, 0, bankNumberMSB);
	}

}