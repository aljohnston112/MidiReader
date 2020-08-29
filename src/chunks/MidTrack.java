package chunks;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import events.MidEvent;
import events.MidEventNoteOff;
import events.MidEventNoteOn;
import notes.Note;
import notes.ThreeDimensionalNoteArray;
import notes.TimedNote;

/** MidTrack is a midi track chunk.
 *  @author Alexander Johnston
 *  @since  2020
 */
public class MidTrack extends MidChunk {

	// The midi events
	final List<MidEvent> events;

	final String trackName;

	final String instrumentName;

	final String deviceName;

	/**       Creates a midi track.
	 * @param length as the length of the midi track in bytes.
	 * @param events as the midi events contained in the track.
	 */
	public MidTrack(long length, List<MidEvent> events, String trackName, String instrumentName, String deviceName) {
		super(length);
		this.events = Collections.unmodifiableList(events);
		this.trackName = trackName;
		this.instrumentName = instrumentName;
		this.deviceName = deviceName;
	}

	public ThreeDimensionalNoteArray getTrack(double ticksPerSecond) {
		ThreeDimensionalNoteArray tracks = new ThreeDimensionalNoteArray();
		List<ArrayList<TimedNote>> tempTracks = new ArrayList<>();
		Map<Integer, Long> currentTicks = new HashMap<>();
		Map<Integer, MidEventNoteOn> currentNotes = new HashMap<>();
		Map<Integer, Boolean> inUse = new HashMap<>();
		for(MidEvent me : events) {
			long ticks = me.getTicksFromLastEvent();
			for(Entry<Integer, Long> e : currentTicks.entrySet()) {
				currentTicks.put(e.getKey(), e.getValue()+ticks);
			}
			if(me instanceof MidEventNoteOn && ((MidEventNoteOn)me).velocity != 0) {
				int index = -1;
				int i = 0;
				while(index == -1) {
					if(inUse.get(i) == null || !inUse.get(i)) {
						index = i;
					}
					i++;
				}
				currentNotes.put(index, (MidEventNoteOn)me);		
				currentTicks.put(index, 0L);
				inUse.put(index, true);
			} else if(me instanceof MidEventNoteOff ||
					(me instanceof MidEventNoteOn && ((MidEventNoteOn)me).velocity == 0)) {
				int index = 0;
				while(index < inUse.size() && inUse.get(index)) {
						index++;
				}
				index--;
				MidEventNoteOn noe = currentNotes.get(index);	
				double time = currentTicks.get(index)/ticksPerSecond;
				while(index+1 > tempTracks.size()) {
					tempTracks.add(new ArrayList<TimedNote>());
				}
				tempTracks.get(index).add(new TimedNote(noe.note, time, noe.velocity));
				inUse.put(index, false);
			}
			
		}
		for(ArrayList<TimedNote> a : tempTracks) {
			tracks.addTrack(a);
		}
		return tracks;
	}

}
