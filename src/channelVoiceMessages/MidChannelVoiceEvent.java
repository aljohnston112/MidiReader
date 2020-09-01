package channelVoiceMessages;

import events.MidChannelMessage;

public abstract class MidChannelVoiceEvent extends MidChannelMessage {

	MidChannelVoiceEvent(int channel) {
		super(channel);
	}

}
