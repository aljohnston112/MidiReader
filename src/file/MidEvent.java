package file;

public abstract class MidEvent {

	private long ticksFromLastEvent;

	public void setTicksFromLastEvent(long ticksFromLastEvent) {
		this.ticksFromLastEvent = ticksFromLastEvent;
	}
	
	public long getTicksFromLastEvent() {
		return ticksFromLastEvent;
	}
	
}
