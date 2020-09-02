package metaEvents;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.PrimitiveIterator.OfInt;

import midiFile.MidWriter;

public final class MidEventTrackName extends MidMetaEvent {
	
	public final String name;
	
	public MidEventTrackName(String name) {
		this.name = name;
	}

	public byte[] getEvent() throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		baos.write(0xFF);
		baos.write(0x03);
		baos.write(MidWriter.variableLengthQuantity(name.length()));
		OfInt oi = name.chars().iterator();
		while(oi.hasNext()) {
			baos.write(oi.next().byteValue());
		}
		return baos.toByteArray();
	}
	
}