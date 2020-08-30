package notes;

import java.util.ArrayList;
import java.util.Iterator;
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

	public void homogenize() {
		for(ArrayList<TimedNote> tns : noteArray) {
			for(TimedNote tn : tns) {
				for(ArrayList<TimedNote> tns2 : noteArray) {
					for(int i = 0; i < tns2.size(); i++) {
						if(tn.equals(tns2.get(i))) {
							tns2.set(i, tn);
						}
					}
				}
			}
		}
	}

	public void condense() {
		if(noteArray.size() > 1) {
			ArrayList<TimedNote> tns = noteArray.get(0);
			ArrayList<TimedNote> tns2 = noteArray.get(1);	
			condense(tns, tns2);
			for(int i = 2; i < tns.size(); i++) {
				tns = tns2;
				tns2 = noteArray.get(i);
				condense(tns, tns2);
			}
		}

	}

	private void condense(ArrayList<TimedNote> tns, ArrayList<TimedNote> tns2) {
		double time1 = 0;
		double time2 = 0;
		ArrayList<TimedNote> a1 = new ArrayList<>();
		ArrayList<TimedNote> a2 = new ArrayList<>();
		Iterator<TimedNote> it = tns.iterator();
		while(it.hasNext()) {
			TimedNote tn = it.next();
			a1.add(tn);
			time1+= tn.time;
			Iterator<TimedNote> it2 = tns2.iterator();
			TimedNote tn2;
			if(it2.hasNext()) {
				tn2 = it2.next();
				a2.add(tn2);
				time2+=tn2.time;
				if(time1 > time2) {
					while(time1 > time2 && it2.hasNext()) {
						tn2 = it2.next();
						a2.add(tn2);
						time2+=tn2.time;
					}
				} else {
					while(time1 > time2 && it2.hasNext()) {
						tn2 = it2.next();
						a2.add(tn2);
						time2+=tn2.time;
					}
				}
			}
		}
	}

}
