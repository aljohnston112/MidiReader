package events;

public class MidEventDeviceName extends MidEvent {
	
	public final String name;
	
	private MidEventDeviceName() {
		throw new AssertionError("Default MidEventDeviceName constructor is unsupported");
	}
	
	public MidEventDeviceName(String name) {
		this.name = name;
	}

}
