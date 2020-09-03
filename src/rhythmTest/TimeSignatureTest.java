package rhythmTest;

import rhythm.Tempo;
import rhythm.TimeSignature;

public class TimeSignatureTest {

	public static void main(String[] args) {
		testConstructor();
		//testGetTime();
	}

	private static void testGetTime() {
		//getTime(Tempo tempo, int bars, int beats)	
		System.out.println("Creating a TimeSignature with...");
		System.out.println("Beat unit of 3...");
		System.out.println("and 6 beats per bar");
		double beatUnit = 3;
		int beatsPerBar = 6; 
		TimeSignature timeSignature = new TimeSignature(beatUnit, beatsPerBar);
		System.out.println("Creating a Tempo with 120 beats per minute");
		double beatsPerMinute =120;
		Tempo tempo = new Tempo(beatsPerMinute);
		System.out.println("Getting TimeSignature array with...");
		System.out.println("2 bars...");
		System.out.println("and 2 beats");
		int bars = 2;
		int beats = 2;
		double[] times = timeSignature.getRhythm(tempo, bars, beats);
		StringBuilder sb = new StringBuilder();
		for(double t : times) {
			sb.append(t);	
			sb.append(", ");	
		}
		sb.delete(sb.length()-2, sb.length());
		System.out.println(sb);
	}

	private static void testConstructor() {
		//TimeSignature(double beatUnit, int beatsPerBar)
		System.out.println("Creating a TimeSignature with...");
		System.out.println("Beat unit of 3...");
		System.out.println("and 6 beats per bar");
		double beatUnit = 3;
		int beatsPerBar = 6; 
		TimeSignature timeSignature = new TimeSignature(beatUnit, beatsPerBar);
		System.out.println(timeSignature);
	}
	
}
