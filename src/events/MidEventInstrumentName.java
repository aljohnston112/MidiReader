package events;

public class MidEventInstrumentName extends MidEvent {
	
	final String name;
	
	private MidEventInstrumentName() {
		throw new AssertionError("Default MidEventInstrumentName constructor is unsupported");
	}
	
	public MidEventInstrumentName(String name) {
		this.name = name;
	}

}