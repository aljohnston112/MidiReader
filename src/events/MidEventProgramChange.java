package events;

public final class MidEventProgramChange extends MidEvent {

	// 64 is middle
	final int programNumber;
	
	final int channel;
	
	private MidEventProgramChange() {
		throw new AssertionError("Default MidEventProgramChange constructor is unsupported");
	}
	
	public MidEventProgramChange(int channel, int programNumber) {
		this.channel = channel;
		this.programNumber = programNumber;
	}
	
}