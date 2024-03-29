package notes;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import dynamics.Dynamics;
import rhythm.Tempo;

/**
 *         A class for making a channel of TimedNotes.
 * @author Alexander Johnston 
 * @since  Copyright 2020 
 */
public class TimedNoteChannel {

	public List<ArrayList<TimedNote>> noteArray = new ArrayList<ArrayList<TimedNote>>();

	/**             Adds a track to this TimedNoteChannel.
	 * @param track The track to add to this TimedNoteChannel.
	 * 
	 * @throws NullPointerException     If track is null.
	 * @throws IllegalArgumentException If track doesn't have at least one TimedNote.
	 */
	public void addTrack(ArrayList<TimedNote> track) {
		Objects.requireNonNull(track);
		if(track.size() < 1) {
			throw new IllegalArgumentException("ArrayList<TimedNote> track passed to addTrack() "
					+ "must have at least one TimedNote");
		}
		noteArray.add(track);
	}

	/**                  Quantizes the TimedNotes in this channel.
	 * @param timeQuanta The time quanta in seconds. Time will be rounded to the closest multiple of the quanta.
	 * @param dynamics   The dynamics used to quantize the velocities.
	 * 
	 * @throws NullPointerException     If dynamics is null.
	 * @throws IllegalArgumentException If timeQuanta is not greater than 0.
	 */
	public void quantize(double timeQuanta, Dynamics dynamics) {
		Objects.requireNonNull(dynamics);
		if(timeQuanta <= 0) {
			throw new IllegalArgumentException("double timeQuanta passed to quantize() "
					+ "must be greater than 0");
		}

		TimedNote timedNote;
		TimedNote timedNoteReplacement;
		for(ArrayList<TimedNote> track : noteArray) {
			for(int i = 0; i < track.size(); i++) {
				timedNote = track.get(i);
				if(Tempo.quantize(timedNote.time, timeQuanta) == 0) {
					track.remove(i);
					i--;
				} else if(dynamics.quantize(timedNote.velocity) == 0) {
					timedNoteReplacement = new TimedNote(new Note("", 1), 
							Tempo.quantize(timedNote.time, timeQuanta), dynamics.quantize(timedNote.velocity));
					track.set(i, timedNoteReplacement);
				} else {
					timedNoteReplacement = new TimedNote(timedNote.note, 
							Tempo.quantize(timedNote.time, timeQuanta), dynamics.quantize(timedNote.velocity));
					track.set(i, timedNoteReplacement);
				}
			}
		}
	}

	/** Replaces TimedNotes that are equal with the same TimedNote object.
	 * 
	 */
	public void homogenize() {
		for(ArrayList<TimedNote> track : noteArray) {
			for(TimedNote timedNote : track) {
				for(ArrayList<TimedNote> trackToHomogenize : noteArray) {
					for(int i = 0; i < trackToHomogenize.size(); i++) {
						if(timedNote.equals(trackToHomogenize.get(i))) {
							trackToHomogenize.set(i, timedNote);
						}
					}
				}
			}
		}
	}

	/**               Condenses the tracks in this channel by replacing as much silence in the lower index tracks 
	 *                with notes from the higher index tracks.
	 * @param overlap The amount of overlap in seconds between the end and start of two notes that can be combined.
	 */
	public void condense(double overlap) {
		boolean changed = true;
		boolean tempChanged = false;
		while(changed) {
			changed = false;
			if(noteArray.size() > 1) {
				ArrayList<TimedNote> trackToSearchForSilence = noteArray.get(0);
				ArrayList<TimedNote> trackToSearchForSilenceReplacment = noteArray.get(1);	
				tempChanged = condense(trackToSearchForSilence, trackToSearchForSilenceReplacment, overlap);
				if(tempChanged) {
					changed = true;
				}
				for(int i = 2; i < noteArray.size(); i++) {
					trackToSearchForSilence = trackToSearchForSilenceReplacment;
					trackToSearchForSilenceReplacment = noteArray.get(i);
					tempChanged = condense(trackToSearchForSilence, trackToSearchForSilenceReplacment, overlap);
					if(tempChanged) {
						changed = true;
					}
				}
			}
		}
	}

	/**                           Condenses the silence in one track with the notes in another, if possible.
	 *                            This method will need to be called until it returns false to condense all the silence.
	 * @param  searchForSilence   The track to search for silence.
	 * @param  silenceReplacement The track to search for TimedNotes which will replace the silence.
	 * @return                    True if this method was able to fill some silence with a TimedNote 
	 *                            from the silenceReplacement track.
	 *                            
	 * @throws NullPointerException If searchForSilence or silenceReplacement are null.
	 */
	private boolean condense(ArrayList<TimedNote> searchForSilence, ArrayList<TimedNote> silenceReplacement, double overlap) {
		Objects.requireNonNull(searchForSilence);
		Objects.requireNonNull(silenceReplacement);
		boolean changed = false;
		int silenceStartIndex = -1;
		int replacementStartIndex = -1;
		int silenceEndIndex = -1;
		int replacementEndIndex = -1;
		double silenceStartTime = 0;
		double replacementStartTime = 0;
		double silenceTime = 0;
		double replacementTime = 0;
		// Find blank space
		int i = 0;
		while(silenceStartIndex == -1 && i < searchForSilence.size()) {
			if(searchForSilence.get(i).velocity == 0) {
				silenceStartIndex = i;
			}
			i++;
		}
		if(silenceStartIndex == -1) {
			return false;
		}
		silenceEndIndex = silenceStartIndex;
		while(i < searchForSilence.size() && searchForSilence.get(i).velocity == 0) {
			i++;
		}
		silenceEndIndex = i;
		for(int j = 0; j < silenceStartIndex; j++) {
			silenceStartTime+=searchForSilence.get(j).time;
		}
		for(int j = silenceStartIndex; j < silenceEndIndex; j++) {
			silenceTime+=searchForSilence.get(j).time;
		}
		double silenceEndTime = silenceStartTime + silenceTime;
		// Find replacement notes
		i = 0;
		while(i < silenceReplacement.size() && replacementStartTime < silenceStartTime) {
			replacementStartTime+=silenceReplacement.get(i).time;
			i++;
		}
		if((Math.abs(replacementStartTime - silenceStartTime) < overlap)) {
			replacementStartIndex = i;
		} else if(replacementStartTime > silenceStartTime) {
			i--;
			replacementStartTime-=silenceReplacement.get(i).time;
			if((Math.abs(replacementStartTime - silenceStartTime) < overlap)) {
				replacementStartIndex = i;
			} else {
				replacementStartTime+=silenceReplacement.get(i).time;
				i++;
				if(replacementStartTime > silenceStartTime && replacementStartTime < silenceEndTime) {
					replacementStartIndex = i;
				}
			}
		}
		if(replacementStartIndex == -1) {
			return false;
		}
		while(i < silenceReplacement.size() && replacementTime < silenceTime) {
			replacementTime+=silenceReplacement.get(i).time;
			i++;
		}
		if((replacementTime < silenceTime) || (Math.abs(replacementTime - silenceTime) < overlap)) {
			replacementEndIndex = i;
		} else {
			i--;
			replacementTime-=silenceReplacement.get(i).time;
			if((replacementTime < silenceTime) || (Math.abs(replacementTime - silenceTime) < overlap)) {
				replacementEndIndex = i;
			}
		}
		// Replace silence
		double silenceAtStart = replacementStartTime-silenceStartTime;
		double silenceAtEnd = silenceTime-replacementTime;
		for(int j = silenceStartIndex; j < silenceEndIndex; j++) {
			searchForSilence.remove(silenceStartIndex);
			changed = true;
		}
		if(silenceAtStart > 0 && replacementTime > 0) {
			searchForSilence.add(silenceStartIndex, new TimedNote(new Note("", 1), silenceAtStart, 0));
			changed = true;
		}
		for(int j = replacementStartIndex, k = 0; j < replacementEndIndex; j++, k++) {
			searchForSilence.add(silenceStartIndex + k, silenceReplacement.get(j));
			silenceReplacement.add(j, new TimedNote(new Note("", 1), silenceReplacement.get(j).time, 0));
			silenceReplacement.remove(j+1);
			changed = true;
		}
		if(silenceAtEnd > 0 && replacementTime > 0) {
			searchForSilence.add(replacementEndIndex+1, new TimedNote(new Note("", 1), silenceAtEnd, 0));
			changed = true;
		}
		// Delete notes and tracks with no time
		for(int k = 0; k < noteArray.size(); k++) {
			ArrayList<TimedNote> track = noteArray.get(k);
			boolean keepTrack = false;
			for(int j = 0; j < track.size(); j++) {
				if(track.get(j).time < overlap) {
					track.remove(j);
					j--;
				}
				if(track.get(j).velocity != 0) {
					keepTrack = true;
				}
			}
			if(!keepTrack) {
				noteArray.remove(k);
				k--;
			}
		}
		// Append extra notes from replacement track to silence track
		silenceStartTime = 0;
		for(TimedNote tn : searchForSilence) {
			silenceStartTime+=tn.time;
		}
		replacementStartTime = 0;
		i = 0;
		while(replacementStartTime < silenceStartTime && i < silenceReplacement.size()) {
			replacementStartTime+=silenceReplacement.get(i).time;
			i++;
		}
		silenceAtStart = replacementStartTime-silenceStartTime;
		if(silenceAtStart > 0) {
			searchForSilence.add(new TimedNote(new Note("", 1), silenceAtStart, 0));
			changed = true;
		}
		replacementStartIndex = i;
		for(int j = replacementStartIndex; j < silenceReplacement.size(); j++) {
			searchForSilence.add(silenceReplacement.get(j));
			silenceReplacement.add(j, new TimedNote(new Note("", 1.0), silenceReplacement.get(j).time, 0));
			silenceReplacement.remove(j+1);
		}
		// Delete silence at the end
		for(int k = 0; k < noteArray.size(); k++) {
			ArrayList<TimedNote> track = noteArray.get(k);
			int j = track.size()-1;
			while(track.get(j).time < overlap) {
				track.remove(j);
				j--;
			}
		}
		return changed;
	}

}