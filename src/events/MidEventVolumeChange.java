package events;

public final class MidEventVolumeChange extends MidEvent {

	private final int volumeOutOf127;
	
	private final int channel;
	
	private MidEventVolumeChange() {
		throw new AssertionError("Default MidEventVolumeChange constructor is unsupported");
	}
	
	public MidEventVolumeChange(int channel, int volumeOutOf127) {
		this.channel = channel;
		this.volumeOutOf127 = volumeOutOf127;
	}

	public int getVolumeOutOf127() {
		return volumeOutOf127;
	}

	public int getChannel() {
		return channel;
	}

}