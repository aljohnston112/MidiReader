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
	
	@Override
	public boolean equals(Object o) {
		if((o instanceof TimedNote) && (((TimedNote)o).time == this.time) && 
				(((TimedNote)o).velocity == (this.velocity)) && (((TimedNote)o).note.equals(this.note))) {
			return true;
		} else {
			return false;
		}
	}
	
}
