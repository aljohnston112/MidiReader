package notes;

import java.util.ArrayList;

/**
@author Alexander Johnston 
        Copyright 2019 
        A class for musical scales
 */
public class Scale {

	// Holds the notes
	public ArrayList<Note> notes = new ArrayList<Note>();

	// Middle A index
	public int middleAIndex;

	// Last index below 20hz
	public int subHertzIndex;
	
	public int middleCIndex;

	// The tonic
	String tonicLetter;

	/**       Adds a note and name to the scale
	 * @param i The index of the notes and names of the 12 tone equal temperament to add
	 * @param ttet The 12 tone equal temperament
	 */
	protected void addNoteAndName(int i, TwelveToneEqualTemperament twelveToneEqualTemperament) {
		notes.add(new Note(twelveToneEqualTemperament.notes.get(i).getName(), twelveToneEqualTemperament.notes.get(i).getFrequency()));
	}

	/**       Adds a note and name to the scale
	 * @param j The index of the note and name arrays to add the note and names to
	 * @param i The index of the notes and names of the 12 tone equal temperament to add
	 * @param twelveToneEqualTemperament The 12 tone equal temperament
	 */
	protected void addNoteAndName(int j, int i, TwelveToneEqualTemperament twelveToneEqualTemperament) {
		notes.set(j, new Note(twelveToneEqualTemperament.notes.get(i).getName(), twelveToneEqualTemperament.notes.get(i).getFrequency()));
	}
	
	public int getIndexForFrequency(double f) {
		int i = 0;
		while(i < notes.size() && notes.get(i).getFrequency() < f) {
			i++;
		}
		if(i == 0) {
			return 0;
		}
		double s0 = Math.abs(notes.get(i).getFrequency() - f);
		double s1 = Math.abs(notes.get(i-1).getFrequency() - f);
		if(s0 < s1) {
			return i;
		} else {
			return i-1;
		}
	}

}
