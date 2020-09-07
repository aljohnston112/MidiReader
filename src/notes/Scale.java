package notes;

import java.util.ArrayList;

/**
 *         A class for musical scales.
 * @author Alexander Johnston 
 * @since  Copyright 2019 
 */
public abstract class Scale {

	public ArrayList<Note> notes = new ArrayList<Note>();

	// Last index below 20hz
	public int subHertzIndex;

	// The tonic
	String tonicLetter;
	
	/**                 Gets the index of a Note with the closest frequency to the given frequency.
	 * @param frequency The frequency of the Note to get the index of.
	 * @return          The index of the Note with the closest frequency to the given frequency in this scale. 
	 */
	public int getIndexForFrequency(double frequency) {
		int i = 0;
		while(i < notes.size() && notes.get(i).hertz < frequency) {
			i++;
		}
		if(i == 0) {
			return 0;
		}
		double dif1 = Math.abs(notes.get(i).hertz - frequency);
		double dif2 = Math.abs(notes.get(i-1).hertz - frequency);
		if(dif1 < dif2) {
			return i;
		} else {
			return i-1;
		}
	}

}