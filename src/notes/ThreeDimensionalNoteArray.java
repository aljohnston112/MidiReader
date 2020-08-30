package notes;

import java.util.ArrayList;
import java.util.List;

import dynamics.Dynamics;
import rhythm.Tempo;

public class ThreeDimensionalNoteArray {

	private List<ArrayList<TimedNote>> noteArray = new ArrayList<ArrayList<TimedNote>>();

	public void addTrack(ArrayList<TimedNote> track) {
		noteArray.add(track);
	}

	public void quantize(Tempo tempo, Dynamics d) {
		for(ArrayList<TimedNote> tns : noteArray) {
			for(int i = 0; i < tns.size(); i++) {
				TimedNote tn = tns.get(i);
				if(tempo.quantize(tn.time) == 0) {
					tns.remove(i);
					i--;
				} else if(d.quantize(tn.velocity) == 0) {
					TimedNote tn2 = new TimedNote(new Note(1), tempo.quantize(tn.time), d.quantize(tn.velocity));
					tns.set(i, tn2);
				} else {
					TimedNote tn2 = new TimedNote(tn.note, tempo.quantize(tn.time), d.quantize(tn.velocity));
					tns.set(i, tn2);
				}
			}
		}
	}

}
