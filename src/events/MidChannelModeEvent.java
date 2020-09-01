package events;

public abstract class MidChannelModeEvent extends MidChannelMessage {

	protected MidChannelModeEvent(int channel) {
		super(channel);
	}
	
}
