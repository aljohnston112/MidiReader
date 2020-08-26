package rhythmTest;

import rhythm.Tempo;

public class TempoTest {

	public static void main(String[] args) {
		//testConstructor();
		testGetters();
	}

	private static void testGetters() {
		double beatsPerMinute =120;
		Tempo tempo = new Tempo(beatsPerMinute);
		System.out.print(tempo);
		System.out.print("\n");
		System.out.print(tempo.getWhole());
		System.out.print(", ");
		System.out.print(tempo.getHalf());
		System.out.print(", ");
		System.out.print(tempo.getQuarter());
		System.out.print(", ");
		System.out.print(tempo.getEighth());
		System.out.print(", ");
		System.out.print(tempo.getSixteenth());
		System.out.print(", ");
		System.out.print(tempo.getThirtySecond());
		System.out.print(", ");
		System.out.print(tempo.getSixtyFourth());
		System.out.print(", ");
		System.out.print(tempo.getOneTwentyEighth());
		System.out.print(", ");
		System.out.print(tempo.getTwoThirds());
		System.out.print(", ");
		System.out.print(tempo.getThird());
		System.out.print(", ");
		System.out.print(tempo.getSixth());
		System.out.print(", ");
		System.out.print(tempo.getTwelth());
		System.out.print(", ");
		System.out.print(tempo.getTwentyFourth());
		System.out.print(", ");
		System.out.print(tempo.getFortyEighth());
		System.out.print(", ");
		System.out.print(tempo.getNinetySixth());
		System.out.print(", ");
		System.out.print(tempo.getOneNinetySecond());
	}

	private static void testConstructor() {
		//Tempo(double beatsPerMinute)
		System.out.print("Creating a Tempo with 120 beats per minute");
		double beatsPerMinute =120;
		Tempo tempo = new Tempo(beatsPerMinute);
		System.out.print(tempo);
	}

}
