package chunks;

import java.util.Collections;
import java.util.List;

import events.MidEvent;

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
	
}
