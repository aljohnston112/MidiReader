package file;

import java.util.List;

public final class MidTempoMap extends MidTrack {

	/**       Creates a midi tempo map track.
	 * @param length as the length of the midi tempo map track in bytes.
	 * @param events as the midi events contained in the midi tempo map track.
	 */
	public MidTempoMap(long length, List<MidEvent> events) {
		super(length, events);
	}
	
}
