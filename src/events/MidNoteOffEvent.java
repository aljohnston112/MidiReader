package events;

import notes.Note;

public class MidNoteOffEvent extends MidEvent {

	Note note;
	
	int velocity;
	
	private MidNoteOffEvent() {
		throw new AssertionError("Default MidNoteOffEvent constructor is unsupported");
	}
	
	public MidNoteOffEvent(Note note, int velocity) {
		this.note = note;
		this.velocity = velocity;
	}
	
}