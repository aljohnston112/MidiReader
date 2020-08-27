package events;

import notes.Note;

public final class MidEventNoteOff extends MidEvent {

	private final int channel;
	
	private final Note note;
	
	private final int velocity;
	
	private MidEventNoteOff() {
		throw new AssertionError("Default MidNoteOffEvent constructor is unsupported");
	}
	
	public MidEventNoteOff(int channel, Note note, int velocity) {
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