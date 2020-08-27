package notes;

/**
@author Alexander Johnston 
        Copyright 2019 
        A class for musical equal temperaments
 */
public abstract class EqualTemperament extends Temperament {

	// Number of notes in an octave
	public int notesPerOctave;

	/**       Creates an equal temperament.
	 * @param middleA is middle A.
	 * @param notesPerOctave is the number of notes in an octave.
	 * @param octavesUnderMiddleA is the number of octaves under middle A to generate.
	 * @param maxFrequency the max frequency.
	 */
	public EqualTemperament(double middleA, int notesPerOctave, int octavesUnderMiddleA, float maxFrequency) {
		
		this.notesPerOctave = notesPerOctave;
		
		double bottomFrequency = middleA / (Math.pow(2.0, octavesUnderMiddleA));
		double hertz = bottomFrequency;

		// The scale for calculating note frequencies
		double multiplier = (StrictMath.pow(2, (1.0/notesPerOctave)));
		int i = 0;
		
		// Filling the note array
		while(hertz < maxFrequency) {
			hertz = (bottomFrequency*StrictMath.pow(multiplier, i));
			notes.add(new Note(hertz));
			i++;
		}
		subHertzIndex = (int)Math.round(Math.log(20.0/bottomFrequency)/Math.log(multiplier));
		middleAIndex = (int)Math.round(Math.log(middleA/bottomFrequency)/Math.log(multiplier));
		addNames();
	}

	/**Adds note names
	 *  
	 */
	protected abstract void addNames();
	
}