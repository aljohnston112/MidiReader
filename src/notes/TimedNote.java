package notes;

public final class TimedNote {

	public final Note note;
	
	public final double time;
	
	public final double velocity;
	
	public TimedNote(Note note, double time, double velocity) {
		this.note = note;
		this.time = time;
		this.velocity = velocity;
	}
	
}
