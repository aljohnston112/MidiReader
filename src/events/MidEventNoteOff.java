package events;

import notes.Note;

public final class MidEventNoteOff extends MidEvent {

	public final int channel;
	
	public final Note note;
	
	public final int velocity;
	
	private MidEventNoteOff() {
		throw new AssertionError("Default MidNoteOffEvent constructor is unsupported");
	}
	
	public MidEventNoteOff(int channel, Note note, int velocity) {
		this.channel = channel;
		this.note = note;
		this.velocity = velocity;
	}
	
}