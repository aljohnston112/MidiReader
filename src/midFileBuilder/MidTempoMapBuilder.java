package midFileBuilder;

import java.util.ArrayList;
import java.util.List;

import chunks.MidTempoMap;
import events.MidEvent;

/** MidTempoMapBuilder is used to build MidTempoMap objects.
 *  @author Alexander Johnston
 *  @since  2020
 */
public class MidTempoMapBuilder extends MidTrackBuilder {

	// The midi events
	private List<MidEvent> events = new ArrayList<>();

	/**       Adds a MidEvent to the list of events used to build a MidTempoMap.
	 * @param event as the MidEvent to add to the list of events used to build a MidTempoMap.
	 */
	public void addEvent(MidEvent event) {
		events.add(event);
	}

	/**        Builds a MidTempoMap with the parameters that have been set.
	 *         Length must be set before this method is called.
	 * @return A MidTempoMap with the set parameters.
	 * @throws IllegalArgumentException if the length has not been set.
	 */
	public MidTempoMap build() {
		if(length == -1) {
			throw new IllegalArgumentException("The length for the MidTempoMap has not been set");
		}
		return new MidTempoMap(length, events);
	}

}
