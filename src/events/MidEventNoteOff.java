package events;

import notes.Note;

public final class MidEventNoteOff extends MidEvent {

	final int channel;
	
	final Note note;
	
	final int velocity;
	
	private MidEventNoteOff() {
		throw new AssertionError("Default MidNoteOffEvent constructor is unsupported");
	}
	
	public MidEventNoteOff(int channel, Note note, int velocity) {
		this.channel = channel;
		this.note = note;
		this.velocity = velocity;
	}
	
}