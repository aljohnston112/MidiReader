package notes;

/**
@author Alexander Johnston 
        Copyright 2019 
        A class for making notes
 */
public class Note {

	// The name of this note
	private String name = "";

	// The frequency of this note
	private double hertz;

	/**
	 * @return The name of this note. May be empty if not set.
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return The frequency of this note
	 */
	public double getFrequency() {
		return hertz;
	}

	/**       Creates a note
	 * @param name The name of this note
	 * @param hertz The frequency of this note
	 */
	public Note(String name, double hertz) {
		this.name = name;
		this.hertz = hertz;
	}

	/**       Creates a note
	 * @param hertz The frequency of this note
	 */
	public Note(double hertz) {
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