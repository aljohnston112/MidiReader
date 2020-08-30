package rhythm;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
@author Alexander Johnston 
        Copyright 2019 
        A class for creating tempos and extracting time values of note divisions
 */
public class Tempo {

	// The time interval of the quarter note in seconds
	private double beatsPerMinute;

	public double[] noteTimes = new double[16];

	/**
	 * @param beatsPerMinute The beats per minute of the tempo
	 */
	public Tempo(double beatsPerMinute){
		this.beatsPerMinute = beatsPerMinute;
		double quarterNote = (60.0/beatsPerMinute);
		noteTimes[15] = quarterNote*4;
		noteTimes[14] = quarterNote*8.0/3.0;
		noteTimes[13] = quarterNote*2.0;
		noteTimes[12] = quarterNote*4.0/3.0;
		noteTimes[11] = quarterNote;
		noteTimes[10] = quarterNote*4.0/6.0;
		noteTimes[9] = quarterNote/2.0;
		noteTimes[8] = quarterNote*4.0/12.0;
		noteTimes[7] = quarterNote/4.0;
		noteTimes[6] = quarterNote*4.0/24.0;
		noteTimes[5] = quarterNote/8.0;
		noteTimes[4] = quarterNote*4.0/48.0;
		noteTimes[3] = quarterNote/16.0;
		noteTimes[2] = quarterNote*4.0/96.0;
		noteTimes[1] = quarterNote/32.0;
		noteTimes[0] = quarterNote*4.0/192.0;
	}

	public double quantize(double time) {
		ArrayList<Double> noteTimes = new ArrayList<>();
		int i = 0;
		while((this.noteTimes[5]*(double)i) < time) {
			noteTimes.add((this.noteTimes[5]*(double)i));
			i++;
		}
		noteTimes.add((this.noteTimes[5]*(double)i));
		double dif1 = Math.abs(noteTimes.get(noteTimes.size()-1)-time);
		double dif2 = Math.abs(noteTimes.get(noteTimes.size()-2)-time);
		if(dif1 < dif2) {
			return noteTimes.get(noteTimes.size()-1);
		} else {
			return noteTimes.get(noteTimes.size()-2);
		}
	}

	/**
	 * @return The time interval of the whole note in seconds
	 */
	public double getWhole(){
		return noteTimes[15];
	}

	/**
	 * @return The time interval of the two-thirds note in seconds
	 */
	public double getTwoThirds(){
		return noteTimes[14];
	}

	/**
	 * @return The time interval of the half note in seconds
	 */
	public double getHalf(){
		return noteTimes[13];
	}

	/**
	 * @return The time interval of the third note in seconds
	 */
	public double getThird(){
		return noteTimes[12];
	}

	/**
	 * @return The time interval of the quarter note in seconds
	 */
	public double getQuarter(){
		return noteTimes[11];
	}

	/**
	 * @return The time interval of the sixth note in seconds
	 */
	public double getSixth(){
		return noteTimes[10];
	}

	/**
	 * @return The time interval of the eighth note in seconds
	 */
	public double getEighth(){
		return noteTimes[9];
	}

	/**
	 * @return The time interval of the twelfth note in seconds
	 */
	public double getTwelth(){
		return noteTimes[8];
	}

	/**
	 * @return The time interval of the sixteenth note in seconds
	 */
	public double getSixteenth(){
		return noteTimes[7];
	}

	/**
	 * @return The time interval of the twenty-fourth note in seconds
	 */
	public double getTwentyFourth(){
		return noteTimes[6];
	}

	/**
	 * @return The time interval of the thirty-second note in seconds
	 */
	public double getThirtySecond(){
		return noteTimes[5];
	}

	/**
	 * @return The time interval of the forty-eighth note in seconds
	 */
	public double getFortyEighth(){
		return noteTimes[4];
	}

	/**
	 * @return The time interval of the sixty-fourth note in seconds
	 */
	public double getSixtyFourth(){
		return noteTimes[3];
	}

	/**
	 * @return The time interval of the ninety-sixth note in seconds
	 */
	public double getNinetySixth(){
		return noteTimes[2];
	}

	/**
	 * @return The time interval of the one-hundred-twenty-eighth note in seconds
	 */
	public double getOneTwentyEighth(){
		return noteTimes[1];
	}

	/**
	 * @return The time interval of the one-hundred-ninety-second note in seconds
	 */
	public double getOneNinetySecond(){
		return noteTimes[0];
	}

	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Tempo: ");
		sb.append(beatsPerMinute);
		sb.append("bpm");
		return sb.toString();
	}

}