package events;

public final class MidEventVolumeChange extends MidEvent {

	final int volumeOutOf127;
	
	final int channel;
	
	private MidEventVolumeChange() {
		throw new AssertionError("Default MidEventVolumeChange constructor is unsupported");
	}
	
	public MidEventVolumeChange(int channel, int volumeOutOf127) {
		this.channel = channel;
		this.volumeOutOf127 = volumeOutOf127;
	}

}