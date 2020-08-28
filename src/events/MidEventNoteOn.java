package events;

import notes.Note;

public final class MidEventNoteOn extends MidEvent {

	final int channel;
	
	final Note note;
	
	final int velocity;
	
	private MidEventNoteOn() {
		throw new AssertionError("Default MidNoteOnEvent constructor is unsupported");
	}
	
	public MidEventNoteOn(int channel, Note note, int velocity) {
		this.channel = channel;
		this.note = note;
		this.velocity = velocity;
	}
	
}
