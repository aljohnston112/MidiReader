package events;

public class MidChannelMessage extends MidEvent {

	public final int channel;
	
	protected MidChannelMessage(int channel){
		this.channel = channel;
	}
	
}
