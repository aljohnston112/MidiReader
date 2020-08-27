package notes;

/**
@author Alexander Johnston 
        Copyright 2019 
        A class for making major scales
 */
public class MajorScale extends Scale{

	/**       Creates a major scale
	 * @param twelveToneEqualTemperament The notes of the temperament
	 * @param tonicLetter The tonic note
	 */
	public MajorScale(TwelveToneEqualTemperament twelveToneEqualTemperament, String tonicLetter) {
		this.tonicLetter = tonicLetter;
		double middleA = twelveToneEqualTemperament.notes.get(twelveToneEqualTemperament.middleAIndex).getFrequency();
		int startingIndex = -1;
		int j = 0;

		// Runs through arrays till an exception is thrown to loop through all the notes
		try {

			// Finds the first note that matches the tonic
			for(int i = 0; i < twelveToneEqualTemperament.notes.size(); i++) {
				if(startingIndex == -1) {
					notes.add(new Note("Blank", -1.0));
					if(twelveToneEqualTemperament.notes.get(i).getName().matches(String.valueOf(tonicLetter))){
						startingIndex = i;
						j = i;
					}
				}

				// Adds all the notes after the initial tonic
				if(startingIndex != -1) {
					addNoteAndName(i, twelveToneEqualTemperament);
					i+=2;
					addNoteAndName(i, twelveToneEqualTemperament);
					i+=2;
					addNoteAndName(i, twelveToneEqualTemperament);
					i+=1;
					addNoteAndName(i, twelveToneEqualTemperament);
					i+=2;
					addNoteAndName(i, twelveToneEqualTemperament);
					i+=2;
					addNoteAndName(i, twelveToneEqualTemperament);
					i+=2;
					addNoteAndName(i, twelveToneEqualTemperament);
				}
			}

			// End of array has been reached
		}catch(IndexOutOfBoundsException e){
		}
		System.out.println("Reached end of note array");

		// Adds notes before the initial tonic
		j = startingIndex;
		try {
			for(int i = 1; i < startingIndex; i++) {
				addNoteAndName(j, startingIndex-i, twelveToneEqualTemperament);
				i+=2;
				j--;
				addNoteAndName(j, startingIndex-i, twelveToneEqualTemperament);
				i+=2;
				j--;
				addNoteAndName(j, startingIndex-i, twelveToneEqualTemperament);
				i+=2;
				j--;
				addNoteAndName(j, startingIndex-i, twelveToneEqualTemperament);
				i+=1;
				j--;
				addNoteAndName(j, startingIndex-i, twelveToneEqualTemperament);
				i+=2;
				j--;
				addNoteAndName(j, startingIndex-i, twelveToneEqualTemperament);
				i+=2;
				j--;
				addNoteAndName(j, startingIndex-i, twelveToneEqualTemperament);
				j--;
			}

			// Start of array has been reached
		}catch(ArrayIndexOutOfBoundsException e){
		}
		System.out.println("Reached start of note array");

		// Remove blank notes
		for(int i = 0; i < j+1; i++) {
			notes.remove(j-i);
		}

		System.out.println("Reached start of note array");

		
		// Sets the sub index as the first note below 20hz
		for(int i = 0; i < notes.size(); i++) {
			if(Math.round(notes.get(i).getFrequency()) < Math.round(20)) {
				this.subHertzIndex = i;
			}

			// Setting the middleA index 
			if(Math.round(notes.get(i).getFrequency()) >= Math.round(middleA)) {
				this.middleAIndex = i;
			}
		}
	}
}