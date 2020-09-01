package events;

public class MidEventTrackName extends MidEvent {
	
	public final String name;
	
	private MidEventTrackName() {
		throw new AssertionError("Default MidEventTrackName constructor is unsupported");
	}
	
	public MidEventTrackName(String name) {
		this.name = name;
	}

}