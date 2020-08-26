package midFileBuilder;

import java.util.ArrayList;
import java.util.List;

import file.MidEvent;

public class MidTrackBuilder extends MidChunkBuilder {

	// The midi events
		private List<MidEvent> events = new ArrayList<>();
		
		public void addEvent(MidEvent event) {
			events.add(event);
		}
	
}
