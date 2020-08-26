package file;

import java.util.Collections;
import java.util.List;

/** MidTrack is a midi track chunk.
 *  @author Alexander Johnston
 *  @since  2020
 */
public class MidTrack extends MidChunk {

	// The midi events
	final List<MidEvent> events;
	
	/**       Creates a midi track.
	 * @param length as the length of the midi track in bytes.
	 * @param events as the midi events contained in the track.
	 */
	public MidTrack(long length, List<MidEvent> events) {
		super(length);
		this.events = Collections.unmodifiableList(events);
	}
	
}
