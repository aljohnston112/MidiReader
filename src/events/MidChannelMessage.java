package events;

public abstract class MidChannelMessage extends MidEvent {

	public final int channel;
	
	protected MidChannelMessage(int channel){
		this.channel = channel;
	}
	
}
