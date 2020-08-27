package midFileBuilder;

import java.util.ArrayList;
import java.util.List;

import chunks.MidTrack;
import events.MidEvent;

/** MidTrackBuilder is used to build MidTrack objects.
 *  @author Alexander Johnston
 *  @since  2020
 */
public class MidTrackBuilder extends MidChunkBuilder {

	// The midi events
	private List<MidEvent> events = new ArrayList<>();

	/**       Adds a MidEvent to the list of events used to build a MidTrack.
	 * @param event as the MidEvent to add to the list of events used to build a MidTrack.
	 */
	public void addEvent(MidEvent event) {
		events.add(event);
	}
	
	/**        Builds a MidTrack with the parameters that have been set.
	 *         Length must be set before this method is called.
	 * @return A MidTrack with the set parameters.
	 * @throws IllegalArgumentException if the length has not been set.
	 */
	public MidTrack build() {
		if(length == -1) {
			throw new IllegalArgumentException("The length for the MidTrack has not been set");
		}
		return new MidTrack(length, events);
	}

}
