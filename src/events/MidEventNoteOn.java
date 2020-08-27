package events;

import notes.Note;

public final class MidEventNoteOn extends MidEvent {

	private final int channel;
	
	private final Note note;
	
	private final int velocity;
	
	private MidEventNoteOn() {
		throw new AssertionError("Default MidNoteOnEvent constructor is unsupported");
	}
	
	public MidEventNoteOn(int channel, Note note, int velocity) {
		this.channel = channel;
		this.note = note;
		this.velocity = velocity;
	}

	public int getChannel() {
		return channel;
	}

	public Note getNote() {
		return note;
	}

	public int getVelocity() {
		return velocity;
	}
	
}
