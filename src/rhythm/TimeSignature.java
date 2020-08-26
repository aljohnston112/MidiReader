package rhythm;

/**
@author Alexander Johnston 
        Copyright 2019 
        A class for making time signatures
 */
public class TimeSignature {

	// The length of one note
	// 4 is quarter, 3 is third, etc
	double beatUnit;
	
	// The number of beat units in a bar
	public int beatsPerBar;
	
	/**       Creates a time signature
	 * @param beatUnit As the beat unit
	 * @param beatsPerBar As the beats per bar
	 */
	public TimeSignature(double beatUnit, int beatsPerBar){
		this.beatUnit = beatUnit;
		this.beatsPerBar = beatsPerBar;
	}
	
	/**        Gets an array of time values representing this time signature
	 * @param  tempo As the tempo
	 * @param  bars As the number of bars
	 * @param  beats As the number of beats after the bars
	 * @return An array of time values that represent this time signature
	 */
	public double[] getTime(Tempo tempo, int bars, int beats){
		double beatUnitTime = tempo.getQuarter()/(beatUnit/4);
		double[] time = new double[(bars*beatsPerBar) + (beats)];
		for(int i = 0; i < time.length; i++) {
			time[i] = beatUnitTime;
		}
		return time;
	}
	
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Time Signature: ");
		sb.append(beatUnit);
		sb.append("/");
		sb.append(beatsPerBar);
		return sb.toString();
	}
	
}