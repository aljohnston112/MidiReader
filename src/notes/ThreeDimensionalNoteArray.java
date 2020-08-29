package notes;

import java.util.ArrayList;
import java.util.List;

public class ThreeDimensionalNoteArray {

	private List<ArrayList<TimedNote>> noteArray = new ArrayList<ArrayList<TimedNote>>();
	
	public void addTrack(ArrayList<TimedNote> track) {
		noteArray.add(track);
	}
	
}
