package notes;

import java.util.ArrayList;

/**
 *         A class for creating a 12 tone equal temperament.
 * @author Alexander Johnston 
 * @since  Copyright 2019 
 */
public final class TwelveToneEqualTemperament extends EqualTemperament {

	public final int middleCIndex;

	/**                           Creates a 12 tone equal temperament.
	 * @param middleA             The frequency of middle A.
	 * @param octavesUnderMiddleA The number of octaves under middle A to generate.
	 * @param maxFrequency        The max frequency.
	 *
	 * @throws IllegalArgumentException If middleA or octavesUnderMiddleA are not greater than 0, or
	 *                                  maxFrequency is not at least the frequency of middleA.
	 */
	public TwelveToneEqualTemperament(double middleA, int octavesUnderMiddleA, float maxFrequency) {
		super(middleA, 12, octavesUnderMiddleA, maxFrequency);
		if(notes.size() < middleAIndex + 4) {
			middleCIndex = -1;
		} else {
			middleCIndex = middleAIndex + 3;
		}
	}

	/** Adds note names for 12-TET (A, A#, B, C, C#, D, D#, E, F, F#, G, G#)
	 * 
	 */
	protected void addNames(){
		ArrayList<Note> newNotes = new ArrayList<>();
		String[] names  = {"A", "A#", "B", "C", "C#", "D", "D#", "E", "F", "F#", "G", "G#"};
		for(int j = 0; j < notes.size(); j++) {
			newNotes.add(new Note(names[j%12], notes.get(j).hertz));
		}
		this.notes.clear();
		for(Note n : newNotes) {
			notes.add(n);
		}
	}

}