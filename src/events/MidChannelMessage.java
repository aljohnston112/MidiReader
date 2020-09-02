package events;

public abstract class MidChannelMessage extends MidEvent {

	public final int channel;
	
	protected MidChannelMessage(int channel){
		if(channel > 15 || channel < 0) {
			throw new IllegalArgumentException("int channel passed to MidChannelMessage constructor is out of range");
		}
		this.channel = channel;
	}
	
}
