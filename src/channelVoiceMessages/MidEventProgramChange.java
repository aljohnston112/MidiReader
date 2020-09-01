package channelVoiceMessages;

import java.io.ByteArrayOutputStream;

public final class MidEventProgramChange extends MidChannelVoiceEvent {

	public final int programNumber;
		
	private MidEventProgramChange() {
		super(-1);
		throw new AssertionError("Default MidEventProgramChange constructor is unsupported");
	}
	
	public MidEventProgramChange(int channel, int programNumber) {
		super(channel);
		if(programNumber > 127 || programNumber < 0) {
			throw new IllegalArgumentException("int programNumber passed to MidEventProgramChange is out of range");
		}
		this.programNumber = programNumber;
	}
	
	public byte[] getEvent() {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		baos.write(0xC0 | ((byte)channel));
		baos.write((byte)programNumber);
		return baos.toByteArray();
	}
	
}