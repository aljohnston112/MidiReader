package metaEvents;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public final class MidEventSMPTEOffset extends MidMetaEvent {
	
	public final byte hours;
	public final byte minutes;
	public final byte seconds;
	public final byte frames;
	public final byte oneHundredthsOfAFrame;
	
	public MidEventSMPTEOffset(byte hours, byte minutes, byte seconds, byte frames, byte oneHundredthsOfAFrame) {
		this.hours = hours;
		this.minutes = minutes;
		this.seconds = seconds;
		this.frames = frames;
		this.oneHundredthsOfAFrame = oneHundredthsOfAFrame;
	}
	
	public byte[] getEvent() throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		baos.write(0xFF);
		baos.write(0x54);
		baos.write(0x05);
		baos.write(hours);
		baos.write(minutes);
		baos.write(seconds);
		baos.write(frames);
		baos.write(oneHundredthsOfAFrame);
		return baos.toByteArray();
	}

}