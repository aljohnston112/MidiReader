package channelVoiceMessages;

import java.io.ByteArrayOutputStream;

public abstract class MidEventControlChange extends MidChannelVoiceEvent {

	public final int controlNumber;

	public final int value;

	public MidEventControlChange(int channel, int controlNumber, int value) {
		super(channel);
		if(controlNumber > 127 || controlNumber < 0) {
			throw new IllegalArgumentException("int controlNumber passed to MidEventControlChange constructor is out of range");
		}
		if(value > 127 || value < 0) {
			throw new IllegalArgumentException("int value passed to MidEventControlChange constructor is out of range");
		}
		this.controlNumber = controlNumber;
		this.value = value;
	}
	
	public byte[] getEvent() {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		baos.write(0xB0 | ((byte)channel));
		baos.write((byte)controlNumber);
		baos.write(value);		
		return baos.toByteArray();
	}
	
}
