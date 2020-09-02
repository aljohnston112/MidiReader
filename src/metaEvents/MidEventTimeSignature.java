package metaEvents;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public final class MidEventTimeSignature extends MidMetaEvent {

	public final int numerator;
	public final int denominator;
	public final int clocksPerMetronome;
	public final int thirtySecondsPer24Clocks;
	
	public MidEventTimeSignature(int numerator, int denominator, int clocksPerMetronome, int thirtySecondsPer24Clocks) {
		this.numerator = numerator;
		this.denominator = denominator;
		this.clocksPerMetronome = clocksPerMetronome;
		this.thirtySecondsPer24Clocks = thirtySecondsPer24Clocks;
	}
	
	public byte[] getEvent() throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		baos.write(0xFF);
		baos.write(0x58);
		baos.write(0x04);
		baos.write((byte)(numerator));
		baos.write((byte)(Math.log(denominator)/Math.log(2)));
		baos.write((byte)(clocksPerMetronome));
		baos.write((byte)(thirtySecondsPer24Clocks));
		return baos.toByteArray();
	}

}
