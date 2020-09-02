package registeredParameters;

import java.io.IOException;

import controlChanges.MidEventRegisteredParameterLSB;
import controlChanges.MidEventRegisteredParameterMSB;

public final class MidEventPitchBendSensitivity extends RegisteredParameter {
	
	// TODO figure out
	
	public MidEventPitchBendSensitivity(int channel) {
		super(new MidEventRegisteredParameterLSB(channel, 0x00), new MidEventRegisteredParameterMSB(channel, 0x00));
	}

	@Override
	public byte[] getEvent() throws IOException {
		// TODO Auto-generated method stub
		return null;
	}
	
}