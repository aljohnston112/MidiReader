package chunks;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;

import channelVoiceMessages.MidEventNoteOff;
import channelVoiceMessages.MidEventNoteOn;
import events.MidEvent;
import notes.Note;
import notes.TimedNoteChannel;
import notes.TimedNote;

/**         MidTrack is a midi track chunk.
 *  @author Alexander Johnston
 *  @since  2020
 */
public class MidTrack extends MidChunk {

	public final List<MidEvent> events;

	public final String trackName;

	public final String instrumentName;

	public final String deviceName;

	/**                      Creates a midi track.
	 * @param length         The length of the midi track in bytes.
	 * @param events         The midi events contained in the track.
	 * @param trackName      The name of the track.
	 * @param instrumentName The name of the instrument on this track.
	 * @param deviceName     The name of the device on this track.
	 * 
	 * @throws NullPointerException If events, trackName, instrumentName, or deviceName are null.
	 */
	public MidTrack(long length, List<MidEvent> events, String trackName, String instrumentName, String deviceName) {
		super(length);
		Objects.requireNonNull(events);
		Objects.requireNonNull(trackName);
		Objects.requireNonNull(instrumentName);
		Objects.requireNonNull(deviceName);
		this.events = Collections.unmodifiableList(events);
		this.trackName = trackName;
		this.instrumentName = instrumentName;
		this.deviceName = deviceName;
	}

	/**                      Creates a TimedNoteChannel containing the sequences in this track.
	 * @param ticksPerSecond The ticks in one second; used for determining time of the notes.
	 * @return               A TimedNoteChannel containing the sequences in this track.
	 * 
	 * @throws IllegalArgumentException If the events in this track contain a note off event 
	 *                                  without a preceding note on event.
	 */
	public TimedNoteChannel getTrack(double ticksPerSecond) {
		TimedNoteChannel notesOut = new TimedNoteChannel();
		Map<Integer, ArrayList<MidEventNoteOn>> channelToNoteOn = new HashMap<>();
		Map<Integer, ArrayList<Long>> channelToTicks = new HashMap<>();
		Map<Integer, ArrayList<ArrayList<TimedNote>>> channelToTimedNotes = new HashMap<>();
		long runningTicks = 0;
		for(MidEvent me : events) {
			// Add ticks
			runningTicks+=me.getTicksFromLastEvent();
			for(Entry<Integer, ArrayList<MidEventNoteOn>> e : channelToNoteOn.entrySet()) {
				for(int i = 0; i < e.getValue().size(); i++) {
					long ticks = me.getTicksFromLastEvent();
					int tempChannel = e.getKey();
					channelToTicks.get(tempChannel).set(i ,channelToTicks.get(tempChannel).get(i)+ticks);
				}
			}
			// Note on events
			if(me instanceof MidEventNoteOn && ((MidEventNoteOn)me).velocity != 0) {
				int channel = ((MidEventNoteOn)me).channel;
				if(channelToNoteOn.get(channel) == null) {
					channelToNoteOn.put(channel, new ArrayList<MidEventNoteOn>());
				}
				if(channelToTicks.get(channel) == null) {
					channelToTicks.put(channel, new ArrayList<Long>());
				}
				if(channelToTimedNotes.get(channel) == null) {
					channelToTimedNotes.put(channel, new ArrayList<ArrayList<TimedNote>>());
				}
				int index = -1;
				for(int i = 0; i < channelToNoteOn.get(channel).size(); i++) {
					if(channelToNoteOn.get(channel).get(i) == null) {
						index = i;
					}
				}
				MidEventNoteOn tme = null;
				if(index == -1) {
					channelToNoteOn.get(channel).add((MidEventNoteOn)me);
					index = channelToNoteOn.get(channel).size()-1;
				} else {
					tme = channelToNoteOn.get(channel).get(index);
					channelToNoteOn.get(channel).set(index, (MidEventNoteOn)me);
				}
				double time = 0;
				while(channelToTicks.get(channel).size() < (index+1)) {
					channelToTicks.get(channel).add(runningTicks);
				}
				while(channelToTimedNotes.get(channel).size() < (index+1)) {
					channelToTimedNotes.get(channel).add(new ArrayList<TimedNote>());
				}
				time = channelToTicks.get(channel).get(index)/ticksPerSecond;
				if(me.getTicksFromLastEvent() != 0 && tme == null) {
					channelToTimedNotes.get(channel).get(index).add(new TimedNote(new Note("", 0), time, 0));
				} else if(me.getTicksFromLastEvent() != 0){
					channelToTimedNotes.get(channel).get(index).add(new TimedNote(tme.note, time, 0));
				}
				channelToTicks.get(channel).set(index, 0L);
			} 
			// Note off events
			else if(me instanceof MidEventNoteOn && ((MidEventNoteOn)me).velocity == 0) {
				int channel = ((MidEventNoteOn)me).channel;
				int index = -1;
				for(int i = 0; i < channelToNoteOn.get(channel).size(); i++) {
					if(channelToNoteOn.get(channel).get(i).equals(((MidEventNoteOn)me))) {
						index = i;
					}
				}
				if(index == -1) {
					throw new IllegalArgumentException("Note off didn't have a preceeding note on");
				}
				double time = channelToTicks.get(channel).get(index)/ticksPerSecond;
				
				channelToTimedNotes.get(channel).get(index).add(
						new TimedNote(channelToNoteOn.get(channel).get(index).note, time, 
								channelToNoteOn.get(channel).get(index).velocity));
				channelToNoteOn.get(channel).set(index, null);
				channelToTicks.get(channel).set(index, 0L);
			} else if(me instanceof MidEventNoteOff) {
				int channel = ((MidEventNoteOff)me).channel;
				int index = -1;
				for(int i = 0; i < channelToNoteOn.get(channel).size(); i++) {
					if(channelToNoteOn.get(channel).get(i) != null) {
						if(channelToNoteOn.get(channel).get(i).equals(((MidEventNoteOff)me))) {
							index = i;
						}
					}
				}
				if(index == -1) {
					throw new IllegalArgumentException("Note off didn't have a preceeding note on");
				}
				double time = channelToTicks.get(channel).get(index)/ticksPerSecond;
				channelToTimedNotes.get(channel).get(index).add(
						new TimedNote(channelToNoteOn.get(channel).get(index).note, time, 
								channelToNoteOn.get(channel).get(index).velocity));
				channelToNoteOn.get(channel).set(index, null);
				channelToTicks.get(channel).set(index, 0L);
			}

		}
		for(Entry<Integer, ArrayList<ArrayList<TimedNote>>> t : channelToTimedNotes.entrySet()) {
			for(ArrayList<TimedNote> a : t.getValue()) {
				notesOut.addTrack(a);
			}
		}
		return notesOut;
	}

}