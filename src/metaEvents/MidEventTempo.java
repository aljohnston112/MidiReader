package metaEvents;

public final class MidEventTempo extends MidMetaEvent {
	
	public final long uSecsPerQuarter;
	
	public MidEventTempo(long uSecsPerQuarter) {
		this.uSecsPerQuarter = uSecsPerQuarter;
	}

}