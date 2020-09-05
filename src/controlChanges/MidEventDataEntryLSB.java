package controlChanges;

import channelVoiceMessages.MidEventControlChange;

/**         MidEventDataEntryLSB is a midi data entry lsb event.
 *  @author Alexander Johnston
 *  @since  2020
 */
public final class MidEventDataEntryLSB extends MidEventControlChange {
	
	public final int controllerNumber;
	
	/**                        Creates a midi data entry lsb event.
	 * @param channel          The channel to send the event to.
	 * @param controllerNumber The controller to send the data to.
	 * @param dataEntryLSB     The data to be sent to the controller.
	 * 
	 * @throws IllegalArgumentException If the controller number isn't between 0 and 31 inclusive.
	 */
	public MidEventDataEntryLSB(int channel, int controllerNumber, byte dataEntryLSB) {
		super(channel, controllerNumber+32, dataEntryLSB);
		if(controllerNumber > 31 || controllerNumber < 0) {
			throw new IllegalArgumentException("int controllerNumber passed to MidEventDataEntryLSB constructor "
					+ "isn't between 0 and 31 inclusive");
		}
		this.controllerNumber = controllerNumber;
	}

}