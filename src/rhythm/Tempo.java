package rhythm;

import java.util.ArrayList;

/**
 *         A class for creating a tempo and quantizing time values to a time division.
 * @author Alexander Johnston 
 * @since  2019 
 */
public final class Tempo {

	// The time interval of the quarter note in seconds
	public final double beatsPerMinute;
	public final double wholeNoteInSeconds;
	public final double twoThirdsNoteInSeconds;
	public final double halfNoteInSeconds;
	public final double thirdNoteInSeconds;
	public final double quarterNoteInSeconds;
	public final double sixthNoteInSeconds;
	public final double eighthNoteInSeconds;
	public final double twelthNoteInSeconds;
	public final double sixteenthNoteInSeconds;
	public final double twentyFourthNoteInSeconds;
	public final double thirtySecondNoteInSeconds;
	public final double fortyEighthNoteInSeconds;
	public final double sixtyFourthNoteInSeconds;
	public final double ninetySixthNoteInSeconds;
	public final double oneTwentyEighthNoteInSeconds;
	public final double oneNinetySecondNoteInSeconds;

	/**
	 * @param  beatsPerMinute The beats per minute of the Tempo.
	 * 
	 * @throws IllegalArgumentException If beatsPerMinute is not greater than 0.
	 */
	public Tempo(double beatsPerMinute){
		if(beatsPerMinute <= 0) {
			throw new IllegalArgumentException(
					"double beatsPerMinute passed to Tempo constructor must be greater than 0");
		}
		this.beatsPerMinute = beatsPerMinute;
		double quarterNoteTime = (60.0/beatsPerMinute);
		wholeNoteInSeconds = quarterNoteTime*4.0;
		twoThirdsNoteInSeconds = quarterNoteTime*8.0/3.0;
		halfNoteInSeconds = quarterNoteTime*2.0;
		thirdNoteInSeconds = quarterNoteTime*4.0/3.0;
		quarterNoteInSeconds = quarterNoteTime;
		sixthNoteInSeconds = quarterNoteTime*4.0/6.0;
		eighthNoteInSeconds = quarterNoteTime/2.0;
		twelthNoteInSeconds = quarterNoteTime*4.0/12.0;
		sixteenthNoteInSeconds = quarterNoteTime/4.0;
		twentyFourthNoteInSeconds = quarterNoteTime*4.0/24.0;
		thirtySecondNoteInSeconds = quarterNoteTime/8.0;
		fortyEighthNoteInSeconds = quarterNoteTime*4.0/48.0;
		sixtyFourthNoteInSeconds = quarterNoteTime/16.0;
		ninetySixthNoteInSeconds = quarterNoteTime*4.0/96.0;
		oneTwentyEighthNoteInSeconds = quarterNoteTime/32.0;
		oneNinetySecondNoteInSeconds = quarterNoteTime*4.0/192.0;
	}

	/**                   Quantizes a time to the closest multiple of the time quanta.
	 * @param  time       The time to quantize.
	 * @param  timeQuanta The time quanta in seconds. Time will be rounded to the closest multiple of the quanta.
	 * @return            Time rounded to the closest multiple of the quanta.
	 * 
	 * @throws IllegalArgumentException If time is not at least 0 or timeQuanta is note greater than 0.
	 */
	public static double quantize(double time, double timeQuanta) {
		if(time < 0) {
			throw new IllegalArgumentException("double time passed to quantize() must be at least 0");
		}
		if(timeQuanta <= 0) {
			throw new IllegalArgumentException("double timeQuanta passed to quantize() must be at greater than 0");
		}
		ArrayList<Double> noteTimes = new ArrayList<>();
		int i = 0;
		while((timeQuanta*(double)i) < time) {
			noteTimes.add((timeQuanta*(double)i));
			i++;
		}
		noteTimes.add((timeQuanta*(double)i));
		double dif1 = Math.abs(noteTimes.get(noteTimes.size()-1)-time);
		double dif2 = Math.abs(noteTimes.get(noteTimes.size()-2)-time);
		if(dif1 < dif2) {
			return noteTimes.get(noteTimes.size()-1);
		} else {
			return noteTimes.get(noteTimes.size()-2);
		}
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Tempo: ");
		sb.append(beatsPerMinute);
		sb.append("bpm");
		return sb.toString();
	}

}