package controlChanges;

import channelVoiceMessages.MidEventControlChange;

/**         MidEventDataEntryMSB is a midi data entry msb event.
 *  @author Alexander Johnston
 *  @since  Copyright 2020
 */
public final class MidEventDataEntryMSB extends MidEventControlChange {
	
	/**                    Creates a midi data entry msb event.
	 * @param channel      The channel to send the message to.
	 * @param dataEntryMSB The msb of the data entry.
	 */
	public MidEventDataEntryMSB(int channel, byte dataEntryMSB) {
		super(channel, 6, dataEntryMSB);
	}

}