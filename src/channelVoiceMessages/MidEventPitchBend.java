package channelVoiceMessages;

import java.io.ByteArrayOutputStream;

import events.MidEvent;

public final class MidEventPitchBend extends MidChannelVoiceEvent {

	// 8192 means no bend
	// 16,383 is the max bend
	public final int value;
	
	public MidEventPitchBend(int channel, int value) {
		super(channel);
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