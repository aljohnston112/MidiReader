package notes;

/**
@author Alexander Johnston 
        Copyright 2019 
        A class for musical equal temperaments
 */
public abstract class EqualTemperament extends Temperament {

	// Number of notes in an octave
	public int notesPerOctave;

	/**       Creates an equal temperament with 43 octaves under middle A up to frequency sr/2
	 * @param middleA is middle A
	 * @param notesPerOctave is the number of notes in an octave
	 * @param samplesPerSecond the sample rate 
	 */
	public EqualTemperament(double middleA, int notesPerOctave, float samplesPerSecond) {
		
		this.notesPerOctave = notesPerOctave;
		
		// The frequency 44 octaves under middle A
		double bottomFrequency = middleA / (Math.pow(2.0, 44.0));
		double hertz = bottomFrequency;

		// The scale for calculating note frequencies
		double multiplier = (StrictMath.pow(2, (1.0/notesPerOctave)));
		int i = 0;
		
		// Filling the note array
		while(hertz < (samplesPerSecond/2.0)) {
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