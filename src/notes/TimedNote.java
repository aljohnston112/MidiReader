package notes;

import java.util.Objects;

/**
 *         A class for making musical TimedNotes.
 * @author Alexander Johnston 
 * @since  Copyright 2020 
 */
public final class TimedNote {

	public final Note note;
	
	public final double time;
	
	public final double velocity;
	
	/**
	 * @param note     The note to give a time and velocity.
	 * @param time     The time the note will take.
	 * @param velocity The velocity of the note.
	 * 
	 * @throws NullPointerException     If note is null.
	 * @throws IllegalArgumentException If time or velocity are not at least 0.
	 */
	public TimedNote(Note note, double time, double velocity) {
		Objects.requireNonNull(note);
		if(time < 0) {
			throw new IllegalArgumentException("double time passed to TimedNote constructor must be at least 0");
		}
		if(velocity < 0) {
			throw new IllegalArgumentException("double velocity passed to TimedNote constructor must be at least 0");
		}
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
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("TN: ");
		sb.append(String.format(note.name));
		sb.append(", ");
		sb.append(String.format("%.2f", note.hertz));
		sb.append("hz, ");
		sb.append(String.format("%.2f", time));
		sb.append("s, ");
		sb.append(String.format("%.2f", velocity));
		sb.append("/127");
		return sb.toString();
	}
	
}
