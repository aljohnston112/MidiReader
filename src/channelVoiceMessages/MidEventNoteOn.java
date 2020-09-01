package channelVoiceMessages;

import java.io.ByteArrayOutputStream;

import midiFile.MidCs;
import notes.Note;
import notes.Scale;

public final class MidEventNoteOn extends MidChannelVoiceEvent {
	
	public final Note note;
	
	public final int velocity;
		
	public final Scale scale;
	
	private MidEventNoteOn() {
		super(-1);
		throw new AssertionError("Default MidNoteOnEvent constructor is unsupported");
	}
	
	public MidEventNoteOn(int channel, Note note, int velocity, Scale s) {
		super(channel);
		this.note = note;
		this.velocity = velocity;
		this.scale = s;
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
	
	public byte[] getEvent() {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		baos.write(0x90 | ((byte)channel));
		byte keyNumber;
		int i = scale.getIndexForFrequency(note.getFrequency());
		int mci = scale.middleCIndex;
		keyNumber = (byte) (mci - i);
		baos.write(keyNumber);
		baos.write(velocity);		
		return baos.toByteArray();
	}
	
}
