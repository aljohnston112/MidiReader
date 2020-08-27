package events;

public final class MidDampEvent extends MidEvent {

	private final int dampOutOf127;	

	public MidDampEvent(int dampOutOf127) {
		this.dampOutOf127 = dampOutOf127;
	}

	public int getDampOutOf127() {
		return dampOutOf127;
	}
	
}
