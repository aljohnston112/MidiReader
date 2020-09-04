package channelVoiceMessages;

import java.io.ByteArrayOutputStream;

/**         MidEventControlChange is a midi control change.
 *  @author Alexander Johnston
 *  @since  Copyright 2020
 */
public abstract class MidEventControlChange extends MidChannelVoiceEvent {

	public final int controlNumber;

	public final int value;

	/**                     Creates a control change event.
	 * @param channel       The channel to be messaged by the control change.
	 * @param controlNumber The control to be changed.
	 * @param value         The value to change the control to.
	 * 
	 * @throws IllegalArgumentException If channel or value are not in their 7-bit range (0-127 inclusive).
	 */
	public MidEventControlChange(int channel, int controlNumber, int value) {
		super(channel);
		if(controlNumber > 127 || controlNumber < 0) {
			throw new IllegalArgumentException("int controlNumber passed to MidEventControlChange constructor "
					+ "is not in range (0-127 inclusive)");
		}
		if(value > 127 || value < 0) {
			throw new IllegalArgumentException("int value passed to MidEventControlChange constructor "
					+ "is not in range (0-127 inclusive)");
		}
		this.controlNumber = controlNumber;
		this.value = value;
	}
	
	@Override
	public byte[] getEvent() {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		baos.write(0xB0 | ((byte)channel));
		baos.write((byte)controlNumber);
		baos.write(value);		
		return baos.toByteArray();
	}
	
}
