package channelVoiceMessages;

import java.io.ByteArrayOutputStream;

/**         MidEventProgramChange is a midi program change event.
 *  @author Alexander Johnston
 *  @since  Copyright 2020
 */
public final class MidEventProgramChange extends MidChannelVoiceEvent {

	public final int programNumber;
	
	/**                     Creates a midi program change event.
	 * @param channel       The channel to receive the event.
	 * @param programNumber The program number to update the program to.
	 * 
	 * @throws IllegalArgumentException If the programNumber is out of range (0-16383 inclusive).
	 */
	public MidEventProgramChange(int channel, int programNumber) {
		super(channel);
		if(programNumber > 127 || programNumber < 0) {
			throw new IllegalArgumentException("int programNumber passed to MidEventProgramChange "
					+ "is out of range (0-16383 inclusive)");
		}
		this.programNumber = programNumber;
	}
	
	@Override
	public byte[] getEvent() {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		baos.write(0xC0 | ((byte)channel));
		baos.write((byte)programNumber);
		return baos.toByteArray();
	}
	
}