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

	@Override
	public boolean equals(Object o) {
		if((o instanceof MidEventNoteOn && ((MidEventNoteOn)o).channel == this.channel && 
				((MidEventNoteOn)o).velocity == this.velocity &&  ((MidEventNoteOn)o).note.equals(this.note))){
			return true;
		} if((o instanceof MidEventNoteOff && ((MidEventNoteOff)o).channel == this.channel && 
				((MidEventNoteOff)o).velocity == this.velocity &&  ((MidEventNoteOff)o).note.equals(this.note))){
			return true;
		} else {
			return false;
		}
	}

}