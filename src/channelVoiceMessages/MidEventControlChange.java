package channelVoiceMessages;

public abstract class MidEventControlChange extends MidChannelVoiceEvent {

	public final int controlNumber;

	public final int value;

	public MidEventControlChange(int channel, int controlNumber, int value) {
		super(channel);
		this.controlNumber = controlNumber;
		this.value = value;
	}
	
}
