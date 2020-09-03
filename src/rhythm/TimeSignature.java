package rhythm;

import java.util.Objects;

/**
 *         A class for making time signatures and generating rhythms from a Tempo.
 * @author Alexander Johnston 
 * @since  Copyright 2019 
 */
public final class TimeSignature {

	// The length of one note
	// 4 is quarter, 3 is third, etc
	public final double beatUnit;
	
	// The number of beat units in a bar
	public final int beatsPerBar;
	
	/**                    Creates a time signature.
	 * @param  beatUnit    The beat unit; the length of one note. 
	 *                     4 is a quarter note, 3 is third note, etc.
	 * @param  beatsPerBar The beat units per bar.
	 * 
	 * @throws IllegalArgumentException If beatUnit and beatsPerBar are not greater than 0.
	 */
	public TimeSignature(double beatUnit, int beatsPerBar){
		if(beatUnit <= 0) {
			throw new IllegalArgumentException("double beatUnit passed to TimeSignature constructor must be greater than 0");
		}
		if(beatsPerBar <= 0) {
			throw new IllegalArgumentException("int beatsPerBar passed to TimeSignature constructor must be greater than 0");
		}
		this.beatUnit = beatUnit;
		this.beatsPerBar = beatsPerBar;
	}
	
	/**              Gets an array of time values representing a rhythm in this TimeSignature.
	 * @param  tempo The Tempo containing the times to be used for the rhythm.
	 * @param  bars  The number of bars.
	 * @param  beats The number of beats after bars.
	 * @return       An array of time values that represent a rhythm in this TimeSignature.
	 * 
	 * @throws NullPointerException     If tempo is null.
	 * @throws IllegalArgumentException If both bars and beats are 0 or either one if less than 0.
	 */
	public double[] getRhythm(Tempo tempo, int bars, int beats){
		Objects.requireNonNull(tempo);
		if(bars == 0 && beats == 0) {
			throw new IllegalArgumentException("int bars and int beats passed to getRhythm() cannot both be 0");
		}
		if(beats < 0) {
			throw new IllegalArgumentException("int beats passed to getRhythm() must be at least 0");
		}
		if(bars < 0) {
			throw new IllegalArgumentException("int bars passed to getRhythm() must be at least 0");
		}
		double beatUnitTime = tempo.quarterNoteInSeconds/(beatUnit/4.0);
		double[] ryhthm = new double[(bars*beatsPerBar) + (beats)];
		for(int i = 0; i < ryhthm.length; i++) {
			ryhthm[i] = beatUnitTime;
		}
		return ryhthm;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Time Signature: ");
		sb.append(beatUnit);
		sb.append("/");
		sb.append(beatsPerBar);
		return sb.toString();
	}
	
}