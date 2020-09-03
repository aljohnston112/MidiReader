package rhythmTest;

import rhythm.Tempo;

public class TempoTest {

	public static void main(String[] args) {
		testConstructor();
	}

	private static void testConstructor() {
		//Tempo(double beatsPerMinute)
		System.out.print("Creating a Tempo with 120 beats per minute");
		double beatsPerMinute =120;
		Tempo tempo = new Tempo(beatsPerMinute);
		System.out.print(tempo);
	}

}
