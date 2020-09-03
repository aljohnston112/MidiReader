package notes;

import java.util.ArrayList;
import java.util.List;

import dynamics.Dynamics;
import rhythm.Tempo;

public class ThreeDimensionalNoteArray {

	private List<ArrayList<TimedNote>> noteArray = new ArrayList<ArrayList<TimedNote>>();

	private boolean changed = false;

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

	public void homogenize(Tempo tempo, Dynamics d) {
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

	public void condense(Tempo tempo, Dynamics d) {
		changed = true;
		if(changed) {
			changed = false;
			if(noteArray.size() > 1) {
				ArrayList<TimedNote> tns = noteArray.get(0);
				ArrayList<TimedNote> tns2 = noteArray.get(1);	
				condense(tempo, d, tns, tns2);
				for(int i = 2; i < noteArray.size(); i++) {
					tns = tns2;
					tns2 = noteArray.get(i);
					condense(tempo, d, tns, tns2);
				}
			}
		}
	}

	private void condense(Tempo tempo, Dynamics d, ArrayList<TimedNote> tns, ArrayList<TimedNote> tns2) {
		int silenceStartIndex = -1;
		int replacementStartIndex = -1;
		int silenceEndIndex = -1;
		int replacementEndIndex = -1;
		double silenceStartTime = 0;
		double replacementStartTime = 0;
		double silenceTime = 0;
		double replacementTime = 0;
		double error = 0.01;
		// Find blank space
		int i = 0;
		while(silenceStartIndex == -1 && i < tns.size()) {
			if(tns.get(i).velocity == 0) {
				silenceStartIndex = i;
			}
			i++;
		}
		if(silenceStartIndex == -1) {
			return;
		}
		silenceEndIndex = silenceStartIndex;
		while(i < tns.size() && tns.get(i).velocity == 0) {
			i++;
		}
		silenceEndIndex = i;
		for(int j = 0; j < silenceStartIndex; j++) {
			silenceStartTime+=tns.get(j).time;
		}
		for(int j = silenceStartIndex; j < silenceEndIndex; j++) {
			silenceTime+=tns.get(j).time;
		}
		double enet = silenceStartTime + silenceTime;
		// Find replacement notes
		i = 0;
		while(i < tns2.size() && replacementStartTime < silenceStartTime) {
			replacementStartTime+=tns2.get(i).time;
			i++;
		}
		if((Math.abs(replacementStartTime - silenceStartTime) < error)) {
			replacementStartIndex = i;
		} else if(replacementStartTime > silenceStartTime) {
			i--;
			replacementStartTime-=tns2.get(i).time;
			if((Math.abs(replacementStartTime - silenceStartTime) < error)) {
				replacementStartIndex = i;
			} else {
				replacementStartTime+=tns2.get(i).time;
				i++;
				if(replacementStartTime > silenceStartTime && replacementStartTime < enet) {
					replacementStartIndex = i;
				}
			}
		}
		if(replacementStartIndex == -1) {
			return;
		}
		replacementTime += tns2.get(i).time;
		i++;
		while(i < tns2.size() && replacementTime < silenceTime) {
			replacementTime+=tns2.get(i).time;
			i++;
		}
		if((replacementTime < silenceTime) || (Math.abs(replacementTime - silenceTime) < error)) {
			replacementEndIndex = i;
		} else {
			i--;
			replacementTime-=tns2.get(i).time;
			if((replacementTime < silenceTime) || (Math.abs(replacementTime - silenceTime) < error)) {
				replacementEndIndex = i;
			}
		}
		// Replace silence
		double bnt = replacementStartTime-silenceStartTime;
		double eent = silenceTime-replacementTime;
		for(int j = silenceStartIndex; j < silenceEndIndex; j ++) {
			tns.remove(silenceStartIndex);
			changed = true;
		}
		if(bnt != 0) {
			tns.add(new TimedNote(new Note(1), bnt, 0));
			changed = true;
		}
		for(int j = replacementStartIndex, k = 0; j < replacementEndIndex; j++, k++) {
			tns.add(silenceStartIndex + k, tns2.get(replacementStartIndex));
			tns2.remove(replacementStartIndex);
			changed = true;
		}
		if(eent != 0) {
			tns.add(replacementEndIndex+1, new TimedNote(new Note(1), eent, 0));
			changed = true;
		}
		quantize(tempo, d);
		System.out.print(true);
	}

}
