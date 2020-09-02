package channelVoiceMessages;

import java.io.ByteArrayOutputStream;
import java.util.Objects;

import notes.Note;
import notes.Scale;

public final class MidEventNoteOff extends MidChannelVoiceEvent {

	public final Note note;

	public final int velocity;
	
	public final Scale scale;

	public MidEventNoteOff(int channel, Note note, int velocity, Scale s) {
		super(channel);
		Objects.requireNonNull(note);
		Objects.requireNonNull(s);
		if(velocity > 127 || velocity < 0) {
			throw new IllegalArgumentException("int velocity passed to MidEventNoteOff constructor is out of range");
		}
		this.note = note;
		this.velocity = velocity;
		this.scale =s;
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
	
	public byte[] getEvent() {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		baos.write(0x80 | ((byte)channel));
		byte keyNumber;
		int i = scale.getIndexForFrequency(note.getFrequency());
		int mci = scale.middleCIndex;
		int dif = 0x3C - mci;
		keyNumber = (byte) (dif+i);
		baos.write(keyNumber);
		baos.write(velocity);		
		return baos.toByteArray();
	}

}