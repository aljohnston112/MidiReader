package metaEvents;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public final class MidEventTempo extends MidMetaEvent {
	
	public final long uSecsPerQuarter;
	
	public MidEventTempo(long uSecsPerQuarter) {
		this.uSecsPerQuarter = uSecsPerQuarter;
	}
	
	public byte[] getEvent() throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		baos.write(0xFF);
		baos.write(0x51);
		baos.write(0x03);
		baos.write((byte)((uSecsPerQuarter & (0b11111111 << 16)) >> 16));
		baos.write((byte)((uSecsPerQuarter & (0b11111111 << 8)) >> 8));
		baos.write((byte)(uSecsPerQuarter & (0b11111111)));
		return baos.toByteArray();
	}

}