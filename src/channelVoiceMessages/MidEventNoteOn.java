package channelVoiceMessages;

import java.io.ByteArrayOutputStream;
import java.util.Objects;

import notes.Note;
import notes.TwelveToneEqualTemperament;

/**         MidEventNoteOn is a midi note on event.
 *  @author Alexander Johnston
 *  @since  Copyright 2020
 */
public final class MidEventNoteOn extends MidChannelVoiceEvent {
	
	public final Note note;
	
	public final int velocity;
		
	public final TwelveToneEqualTemperament scale;
	
	/**                Creates a note on midi event.
	 * @param channel  The channel to receive the event.
	 * @param note     The note to be turned on.
	 * @param velocity The velocity.
	 * @param scale    The TwelveToneEqualTemperament used for determining the note index.
	 * 
	 * @throws NullPointerException     If note or scale are null.
	 * @throws IllegalArgumentException If velocity is not in it's 7-bit range (0-127 inclusive).
	 */
	public MidEventNoteOn(int channel, Note note, int velocity, TwelveToneEqualTemperament scale) {
		super(channel);
		Objects.requireNonNull(note);
		Objects.requireNonNull(scale);
		if(velocity > 127 || velocity < 0) {
			throw new IllegalArgumentException("int velocity passed to MidEventNoteOn constructor "
					+ "is not in range (0-127 inclusive)");
		}
		this.note = note;
		this.velocity = velocity;
		this.scale = scale;
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
	
	@Override
	public byte[] getEvent() {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		baos.write(0x90 | ((byte)channel));
		byte keyNumber;
		int i = scale.getIndexForFrequency(note.hertz);
		int mci = scale.middleCIndex;
		int dif = 0x3C - mci;
		keyNumber = (byte) (dif+i);
		baos.write(keyNumber);
		baos.write(velocity);		
		return baos.toByteArray();
	}
	
}
