package notes;

import java.util.ArrayList;

/**
@author Alexander Johnston 
        Copyright 2020 
        A class for creating a 24 tone equal temperament
 */
public class TwentyFourToneEqualTemperament extends EqualTemperament {

	/**       Creates a 24 tone equal temperament.
	 * @param middleA is middle A.
	 * @param octavesUnderMiddleA is the number of octaves under middle A to generate.
	 * @param maxFrequency is the max frequency.
	 */
	public TwentyFourToneEqualTemperament(double middleA, int octavesUnderMiddleA, float maxFrequency) {
		super(middleA, 24, octavesUnderMiddleA, maxFrequency);
	}

	/** Adds note names for 24-TET (A, A\A#, A#, A#\B, B, B\C, C, C\C#, C#, C#\D, D, D\D#, 
				D#, D#\E, E, E\F, F, F\F#, F#, F#\G, G, G\G#, G#, G#\A)
	 * 
	 */
	protected void addNames(){
		ArrayList<Note> newNotes = new ArrayList<>();
		String[] names  = {"A", "A\\A#" ,"A#", "A#\\B" ,"B", "B\\C" ,"C", "C\\C#" ,"C#", "C#\\D" ,"D", "D\\D#" ,
				"D#", "D#\\E" ,"E", "E\\F" ,"F", "F\\F#" ,"F#", "F#\\G" ,"G", "G\\G#" ,"G#", "G#\\A"};
		for(int j = 0; j < notes.size(); j++) {
			newNotes.add(new Note(names[j%24], notes.get(j).getFrequency()));
		}
		this.notes.clear();
		for(Note n : newNotes) {
			notes.add(n);
		}
	}
	
}
