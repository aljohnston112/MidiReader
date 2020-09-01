package events;

import rhythm.TimeSignature;

public final class MidEventTimeSignature extends MidEvent {

	public final TimeSignature ts;
		
	private MidEventTimeSignature() {
		throw new AssertionError("Default MidEventTimeSignature constructor is unsupported");
	}
	
	public MidEventTimeSignature(TimeSignature ts) {
		this.ts = ts;
	}

}
