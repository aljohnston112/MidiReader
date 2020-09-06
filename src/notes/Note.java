package notes;

import java.util.Objects;

/**
 *         A class for making musical notes.
 * @author Alexander Johnston 
 * @since  Copyright 2019 
 */
public final class Note {

	// The name of this note
	public final String name;

	// The frequency of this note
	public final double hertz;

	/**             Creates a note.
	 * @param name  The name of the note.
	 * @param hertz The frequency of the note in hertz.
	 * 
	 * @throws NullPointerException If name is null.
	 */
	public Note(String name, double hertz) {
		Objects.requireNonNull(name);
		this.name = name;
		this.hertz = hertz;
	}
	
	@Override
	public boolean equals(Object o) {
		if((o instanceof Note) && (((Note)o).hertz == this.hertz) && (((Note)o).name.equals(this.name))) {
			return true;
		} else {
			return false;
		}
	}
	
}