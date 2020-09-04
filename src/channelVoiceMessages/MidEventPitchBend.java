package channelVoiceMessages;

import java.io.ByteArrayOutputStream;

/**         MidEventPitchBend is a midi pitch bend event.
 *  @author Alexander Johnston
 *  @since  Copyright 2020
 */
public final class MidEventPitchBend extends MidChannelVoiceEvent {

	public final int value;
	
	/**               Creates a midi pitch bend event.
	 * @param channel The channel to receive the event.
	 * @param value   The pitch bend value. 8192 means no bend. 16,383 is the max bend.
	 * 
	 * @throws IllegalArgumentException If the pitch bend value is out of range (0-16383 inclusive).
	 */
	public MidEventPitchBend(int channel, int value) {
		super(channel);
		if(value > 16383 || value < 0) {
			throw new IllegalArgumentException("int value passed to MidEventPitchBend constructor "
					+ "is out of range (0-16383 inclusive)");
		}
		this.value = value;
	}
	
	@Override
	public byte[] getEvent() {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		baos.write(0xE0 | ((byte)channel));
		baos.write((byte)(value & 0b01111111));
		baos.write((byte)(value & (0b01111111) << 7) >> 7);
		return baos.toByteArray();
	}
	
}