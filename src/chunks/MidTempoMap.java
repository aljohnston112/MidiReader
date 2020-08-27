package chunks;

import java.util.List;

import events.MidEvent;

/** MidTempoMap is a midi tempo map which is a midi track chunk.
 *  @author Alexander Johnston
 *  @since  2020
 */
public final class MidTempoMap extends MidTrack {
	
	/**       Creates a midi tempo map track.
	 * @param length as the length of the midi tempo map track in bytes.
	 * @param events as the midi events contained in the midi tempo map track.
	 */
	public MidTempoMap(long length, List<MidEvent> events) {
		super(length, events);
	}
	
}
