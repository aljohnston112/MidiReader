package registeredParameters;

import controlChanges.MidEventRegisteredParameterLSB;
import controlChanges.MidEventRegisteredParameterMSB;
import events.MidEvent;

// TODO get rid of extension
public abstract class RegisteredParameter extends MidEvent {
	
	final MidEventRegisteredParameterLSB merplsb;
	
	final MidEventRegisteredParameterMSB merpmsb;
		
	RegisteredParameter(MidEventRegisteredParameterLSB merplsb, MidEventRegisteredParameterMSB merpmsb) {
		this.merplsb = merplsb;
		this.merpmsb = merpmsb;
	}
	
}
