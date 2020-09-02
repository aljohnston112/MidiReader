package channelVoiceMessages;

import java.io.ByteArrayOutputStream;

public final class MidEventPitchBend extends MidChannelVoiceEvent {

	// 8192 means no bend
	// 16,383 is the max bend
	public final int value;
	
	public MidEventPitchBend(int channel, int value) {
		super(channel);
		if(value > 16383 || value < 0) {
			throw new IllegalArgumentException("int value passed to MidEventPitchBend constructor is out of range");
		}
		this.value = value;
	}
	
	public byte[] getEvent() {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		baos.write(0xE0 | ((byte)channel));
		baos.write((byte)(value & 0b01111111));
		baos.write((byte)(value & (0b01111111) << 7) >> 7);
		return baos.toByteArray();
	}
	
}