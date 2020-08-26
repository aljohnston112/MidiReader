package file;

import notes.Note;

public class MidNoteOnEvent extends MidEvent {

	Note note;
	
	int velocity;
	
	private MidNoteOnEvent() {
		throw new AssertionError("Default MidNoteOnEvent constructor is unsupported");
	}
	
	public MidNoteOnEvent(Note note, int velocity) {
		this.note = note;
		this.velocity = velocity;
	}
	
}
