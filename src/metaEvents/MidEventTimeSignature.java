package metaEvents;

import rhythm.TimeSignature;

public final class MidEventTimeSignature extends MidMetaEvent {

	public final TimeSignature ts;
	
	public MidEventTimeSignature(TimeSignature ts) {
		this.ts = ts;
	}

}
