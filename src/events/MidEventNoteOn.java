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
	
	@Override
	public boolean equals(Object o) {
		if((o instanceof MidEventNoteOn && ((MidEventNoteOn)o).channel == this.channel && 
				((MidEventNoteOn)o).note.equals(this.note))){
			return true;
		} if((o instanceof MidEventNoteOff && ((MidEventNoteOff)o).channel == this.channel && 
				((MidEventNoteOff)o).note.equals(this.note))){
			return true;
		} else {
			return false;
		}
	}
	
}
