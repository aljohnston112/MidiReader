package dynamics;

/**
 *         A class for volume dynamics.
 * @author Alexander Johnston
 * @since  Copyright 2019
 */
public final class Dynamics {
	
	final double fffAmplitude;
	final double ffAmplitude;
	final double fAmplitude;
	final double mfAmplitude;
	final double mpAmplitude;
	final double pAmplitude;
	final double ppAmplitude;
	final double pppAmplitude;
	final double silenceAmplitude;
	
	/**                 Makes a Dynamics object.
	 * @param amplitude The max amplitude.
	 * 
	 * @throws IllegalArgumentException If amplitude is not greater than 0.
	 */
	public Dynamics(double amplitude) {
		// TODO Volume should be an exponential function of velocity
		if(amplitude <= 0) {
			throw new IllegalArgumentException("double amplitude passed to Dynamics constructor must be greater than 0");
		}
		double base = Math.pow(2.0, (1.0/amplitude));
		fffAmplitude = amplitude;
		ffAmplitude = Math.pow(base, 7.0/8.0);
		fAmplitude = Math.pow(base, 3.0/4.0);
		mfAmplitude = Math.pow(base, 5.0/8.0);
		mpAmplitude = Math.pow(base, 1.0/2.0);
		pAmplitude = Math.pow(base, 3.0/8.0);
		ppAmplitude = Math.pow(base, 1.0/4.0);
		pppAmplitude = Math.pow(base, 1.0/8.0);
		silenceAmplitude = 0;
	}
	
	/**                 Quantizes an amplitude to the nearest dynamic.
	 * @param amplitude The amplitude to quantize.
	 * @return          The amplitude quantized to the nearest dynamic.
	 */
	public double quantize(double amplitude) {
		// TODO perhaps take slope into account
		double[] dynamics = {fffAmplitude, ffAmplitude, fAmplitude, mfAmplitude, 
				mpAmplitude, pAmplitude, ppAmplitude, pppAmplitude, silenceAmplitude};
		int i = 0;
		while(dynamics[i] > amplitude) {
			i++;
		}
		if(i == 0) {
			return dynamics[i];
		}
		double dif1 = Math.abs(dynamics[i]-amplitude);
		double dif2 = Math.abs(dynamics[i-1]-amplitude);
		if(dif1 < dif2) {
			return dynamics[i];
		} else {
			return dynamics[i-1];
		}
	}
	
}