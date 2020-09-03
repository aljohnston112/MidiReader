package notes;

/**
 *         A class for creating musical equal temperaments.
 * @author Alexander Johnston 
 * @since  Copyright 2019 
 */
public abstract class EqualTemperament extends Temperament {

	// Number of notes in an octave
	public final int notesPerOctave;
	
	public final int middleAIndex;

	/**                           Creates an equal temperament.
	 * @param middleA             The frequency of middle A.
	 * @param notesPerOctave      The number of notes in an octave.
	 * @param octavesUnderMiddleA The number of octaves under middle A to generate.
	 * @param maxFrequency        The max frequency to generate.
	 * 
	 * @throws IllegalArgumentException If middleA, notesPerOctave, or octavesUnderMiddleA are not greater than 0, or
	 *                                  maxFrequency is not at least the frequency of middleA.
	 */
	public EqualTemperament(double middleA, int notesPerOctave, int octavesUnderMiddleA, double maxFrequency) {
		if(middleA <= 0) {
			throw new IllegalArgumentException("double middleA passed to EqualTemperament constructor "
					+ "must be greater than zero");
		}
		if(notesPerOctave <= 0) {
			throw new IllegalArgumentException("int octavesUnderMiddleA passed to EqualTemperament constructor "
					+ "must be greater than zero");
		}
		if(notesPerOctave <= 0) {
			throw new IllegalArgumentException("int notesPerOctave passed to EqualTemperament constructor "
					+ "must be greater than zero");
		}
		if(maxFrequency < middleA) {
			throw new IllegalArgumentException("double maxFrequency passed to EqualTemperament constructor "
					+ "must be at least the frequency of middleA");
		}
		this.notesPerOctave = notesPerOctave;	
		// The exponent base used for calculating note frequencies
		double base = (StrictMath.pow(2, (1.0/notesPerOctave)));
		double bottomFrequency = middleA / (Math.pow(2.0, octavesUnderMiddleA));
		double hertz = bottomFrequency;
		int i = 0;
		while(hertz < maxFrequency) {
			hertz = (bottomFrequency*StrictMath.pow(base, i));
			notes.add(new Note("", hertz));
			i++;
		}
		subHertzIndex = (int)Math.round(Math.log(20.0/bottomFrequency)/Math.log(base));
		middleAIndex = (int)Math.round(Math.log(middleA/bottomFrequency)/Math.log(base));
		addNames();
	}

	/** Adds names to the Note objects.
	 *  
	 */
	protected abstract void addNames();

}