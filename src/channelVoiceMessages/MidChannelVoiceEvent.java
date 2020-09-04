package channelVoiceMessages;

import events.MidChannelMessage;

/**         MidChannelVoiceEvent is a midi channel voice event.
 *  @author Alexander Johnston
 *  @since  Copyright 2020
 */
public abstract class MidChannelVoiceEvent extends MidChannelMessage {

	/**               Creates a channel voice event for the given channel.
	 * @param channel The channel to be messaged by the channel voice event.
	 */
	MidChannelVoiceEvent(int channel) {
		super(channel);
	}

}