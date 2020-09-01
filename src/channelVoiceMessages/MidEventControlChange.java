package channelVoiceMessages;

public class MidEventControlChange extends MidChannelVoiceEvent {

	public final int controlNumber;

	public final int value;
	
	private MidEventControlChange() {
		super(-1);
		throw new AssertionError("Default MidNoteOffEvent constructor is unsupported");
	}

	public MidEventControlChange(int channel, int controlNumber, int value) {
		super(channel);
		this.controlNumber = controlNumber;
		this.value = value;
	}
	
}
