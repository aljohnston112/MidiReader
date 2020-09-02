package metaEvents;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public final class MidEventEndOfTrack extends MidMetaEvent {

	public byte[] getEvent() throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		baos.write(0xFF);
		baos.write(0x2F);
		baos.write(0x00);
		return baos.toByteArray();
	}
	
}
