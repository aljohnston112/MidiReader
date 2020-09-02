package events;

import java.io.IOException;

public abstract class MidEvent {

	private long ticksFromLastEvent;

	public void setTicksFromLastEvent(long ticksFromLastEvent) {
		if(ticksFromLastEvent > 4294967295L || ticksFromLastEvent < 0) {
			throw new IllegalArgumentException("int ticksFromLastEvent passed to setTicksFromLastEvent constructor is out of range");
		}
		this.ticksFromLastEvent = ticksFromLastEvent;
	}
	
	public long getTicksFromLastEvent() {
		return ticksFromLastEvent;
	}
	
	public abstract byte[] getEvent() throws IOException;
	
}
