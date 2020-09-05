package controlChanges;

import channelVoiceMessages.MidEventControlChange;

/**         MidEventBankSelectLSB is a midi bank select lsb event.
 *  @author Alexander Johnston
 *  @since  2020
 */
public final class MidEventBankSelectLSB extends MidEventControlChange {

	/**                     Creates a midi bank select lsb event.
	 * @param channel       The channel to send the event to.
	 * @param bankNumberLSB The lsb of the bank number.
	 */
	public MidEventBankSelectLSB(int channel, int bankNumberLSB) {
		super(channel, 0x20, bankNumberLSB);
	}
	
}
