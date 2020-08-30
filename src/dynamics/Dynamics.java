package dynamics;

/**
 * @author Alexander Johnston
 * Copyright 2019
 * A class for dynamics
 */
public class Dynamics {

	// The types of dynamics this class can generate
	public enum Dynamic{ 
		fff, ff, f, mf, mp, p, pp, ppp, silent
	}	
	
	public double[] dynamics = new double[9];
	
	/**       Makes a new Dynamics 
	 * @param amplitude as the max amplitude
	 */
	public Dynamics(double amplitude) {
		dynamics[0] = amplitude;
		dynamics[1] = amplitude * 112.0/127.0;
		dynamics[2] = amplitude * 96.0/127.0;
		dynamics[3] = amplitude * 80.0/127.0;
		dynamics[4] = amplitude * 64.0/127.0;
		dynamics[5] = amplitude * 49.0/127.0;
		dynamics[6] = amplitude * 33.0/127.0;
		dynamics[7] = amplitude * 16.0/127.0;
		dynamics[8] = 0;
	}
	
	/**        Gets the amplitude of a wave corresponding to the dynamic
	 * @param  amplitude as the original amplitude assumed to be fff
	 * @param  dynamic as the dynamic to set the amplitude to
	 * @return A double representing the amplitude scaled down to the dynamic dyn
	 */
	public double getAmplitude(Dynamic dynamic) {
		switch(dynamic) {
		case fff: 
			return dynamics[0];
		case ff: 
			return dynamics[1];
		case f: 
			return dynamics[2];
		case mf: 
			return dynamics[3];
		case mp: 
			return dynamics[4];
		case p: 
			return dynamics[5];
		case pp: 
			return dynamics[6];
		case ppp: 
			return dynamics[7];
		case silent:
			return dynamics[8];
		}
		throw new RuntimeException("Dynamics object is corrupt");
	}
	
	public double quantize(double velocity) {
		int i = 0;
		while(dynamics[i] > velocity) {
			i++;
		}
		if(i == 0) {
			return dynamics[i];
		}
		double dif1 = Math.abs(dynamics[i]-velocity);
		double dif2 = Math.abs(dynamics[i-1]-velocity);
		if(dif1 < dif2) {
			return dynamics[i];
		} else {
			return dynamics[i-1];
		}
	}
	
}