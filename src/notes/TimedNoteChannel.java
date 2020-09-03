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

	private List<ArrayList<TimedNote>> noteArray = new ArrayList<ArrayList<TimedNote>>();

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

	/** Condenses the tracks in this channel by replacing as much silence in the lower index tracks 
	 *  with notes from the higher index tracks.
	 */
	public void condense() {
		boolean changed = true;
		boolean tempChanged = false;
		while(changed) {
			changed = false;
			if(noteArray.size() > 1) {
				ArrayList<TimedNote> trackToSearchForSilence = noteArray.get(0);
				ArrayList<TimedNote> trackToSearchForSilenceReplacment = noteArray.get(1);	
				tempChanged = condense(trackToSearchForSilence, trackToSearchForSilenceReplacment);
				if(tempChanged) {
					changed = true;
				}
				for(int i = 2; i < noteArray.size(); i++) {
					trackToSearchForSilence = trackToSearchForSilenceReplacment;
					trackToSearchForSilenceReplacment = noteArray.get(i);
					tempChanged = condense(trackToSearchForSilence, trackToSearchForSilenceReplacment);
					if(tempChanged) {
						changed = true;
					}
				}
			}
		}
	}

	/**                           Condenses the silence in one track with the notes in another, if possible.
	 *                            This method will need to be called until it returns false to get condense all the silence.
	 * @param  searchForSilence   The track to search for silence.
	 * @param  silenceReplacement The track to search for TimedNotes which will replace the silence.
	 * @return                    True if this method was able to fill some silence with a TimedNote 
	 *                            from the silenceReplacement track.
	 *                            
	 * @throws NullPointerException If searchForSilence or silenceReplacement are null.
	 */
	private boolean condense(ArrayList<TimedNote> searchForSilence, ArrayList<TimedNote> silenceReplacement) {
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
		double error = 0.01;
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
		double enet = silenceStartTime + silenceTime;
		// Find replacement notes
		i = 0;
		while(i < silenceReplacement.size() && replacementStartTime < silenceStartTime) {
			replacementStartTime+=silenceReplacement.get(i).time;
			i++;
		}
		if((Math.abs(replacementStartTime - silenceStartTime) < error)) {
			replacementStartIndex = i;
		} else if(replacementStartTime > silenceStartTime) {
			i--;
			replacementStartTime-=silenceReplacement.get(i).time;
			if((Math.abs(replacementStartTime - silenceStartTime) < error)) {
				replacementStartIndex = i;
			} else {
				replacementStartTime+=silenceReplacement.get(i).time;
				i++;
				if(replacementStartTime > silenceStartTime && replacementStartTime < enet) {
					replacementStartIndex = i;
				}
			}
		}
		if(replacementStartIndex == -1) {
			return false;
		}
		replacementTime += silenceReplacement.get(i).time;
		i++;
		while(i < silenceReplacement.size() && replacementTime < silenceTime) {
			replacementTime+=silenceReplacement.get(i).time;
			i++;
		}
		if((replacementTime < silenceTime) || (Math.abs(replacementTime - silenceTime) < error)) {
			replacementEndIndex = i;
		} else {
			i--;
			replacementTime-=silenceReplacement.get(i).time;
			if((replacementTime < silenceTime) || (Math.abs(replacementTime - silenceTime) < error)) {
				replacementEndIndex = i;
			}
		}
		// Replace silence
		double bnt = replacementStartTime-silenceStartTime;
		double eent = silenceTime-replacementTime;
		for(int j = silenceStartIndex; j < silenceEndIndex; j ++) {
			searchForSilence.remove(silenceStartIndex);
			changed = true;
		}
		if(bnt != 0) {
			searchForSilence.add(new TimedNote(new Note("", 1), bnt, 0));
			changed = true;
		}
		for(int j = replacementStartIndex, k = 0; j < replacementEndIndex; j++, k++) {
			searchForSilence.add(silenceStartIndex + k, silenceReplacement.get(replacementStartIndex));
			silenceReplacement.remove(replacementStartIndex);
			changed = true;
		}
		if(eent != 0) {
			searchForSilence.add(replacementEndIndex+1, new TimedNote(new Note("", 1), eent, 0));
			changed = true;
		}
		// Delete notes with no time
		for(ArrayList<TimedNote> track : noteArray) {
			for(int j = 0; j < track.size(); j++) {
				if(track.get(j).time == 0) {
					track.remove(j);
				}
			}
		}
		return changed;
	}

}