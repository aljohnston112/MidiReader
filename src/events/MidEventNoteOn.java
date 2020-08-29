package events;

import notes.Note;

public final class MidEventNoteOn extends MidEvent {

	public final int channel;
	
	public final Note note;
	
	public final int velocity;
	
	private MidEventNoteOn() {
		throw new AssertionError("Default MidNoteOnEvent constructor is unsupported");
	}
	
	public MidEventNoteOn(int channel, Note note, int velocity) {
		this.channel = channel;
		this.note = note;
		this.velocity = velocity;
	}
	
}
