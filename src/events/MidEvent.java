package events;

public abstract class MidEvent {

	private long ticksFromLastEvent;

	public void setTicksFromLastEvent(long ticksFromLastEvent) {
		this.ticksFromLastEvent = ticksFromLastEvent;
	}
	
	public long getTicksFromLastEvent() {
		return ticksFromLastEvent;
	}
	
	// TODO abstract byte[] getEvent()
	
}
