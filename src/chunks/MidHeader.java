package chunks;


import java.util.Objects;

import midFileBuilder.MidHeaderBuilder.Format;
import midiFile.MidCs;

/**         MidHeader is a midi header chunk.
 *  @author Alexander Johnston
 *  @since  2020
 */
public final class MidHeader extends MidChunk {

	public final Format format;

	public final int nTracks;

	// True when format is drop frame 
	public final boolean isDropFrame;

	// If true then midi is in ticksPerSeconds else ticksPerQuarterNote
	public final boolean isSeconds;

	// If isSeconds then ticksPerFrame else ticksPerQuarterNote
	public final int ticksPer;

	public final int frameRate;	

	/**                   Creates a midi header.
	 * @param format      The format of the midi header.
	 * @param nTracks     The number of tracks in the midi header.
	 * @param isDropFrame True if the format is drop frame.
	 * @param isSeconds   True if the ticks are per second, or false if the ticks are per quarter note.
	 * @param ticksPer    The ticks per second or the ticks per quarter note.
	 * @param length      The length of the header in bytes.
	 * 
	 * @throws NullPointerException     If format is null.
	 * @throws IllegalArgumentException If nTracks or ticksPer are note at least 1, or frameRate is not 24, 25, or 30.
	 */
	public MidHeader(Format format, int nTracks, boolean isDropFrame, 
			boolean isSeconds, int ticksPer, int frameRate, long length) {
		super(length);
		Objects.requireNonNull(format);
		if(nTracks < 1) {
			throw new IllegalArgumentException("int nTracks passed to MidHeader constructor must be at least 1");
		}
		if(ticksPer < 1) {
			throw new IllegalArgumentException("int ticksPer passed to MidHeader constructor must be at least 1");			
		}
		if(frameRate != 24 && frameRate != 25 && frameRate != 30) {
			throw new IllegalArgumentException("int frameRate passed to MidHeader constructor must be 24, 25, or 30");			
		}
		this.format = format;
		this.nTracks = nTracks;
		this.isDropFrame = isDropFrame;
		this.isSeconds = isSeconds;
		this.frameRate = frameRate;
		this.ticksPer = ticksPer;
	}

	/**
	 * @return The byte representing the SMPTE format used for this midi file.
	 */
	public byte getSMPTEFormat() {
		switch(frameRate) {
		case 24 :
			return -24;
		case 25 :
			return -25;
		case MidCs.DIVISION_SMTPE_FRAMES_PER_SEC_30_DROP_FRAME :
			if(isDropFrame) {
				return -29;
			} else {
				return -30;
			}
		default :
			throw new IllegalArgumentException("The Midheader has an invalid frameRate");
		}
	}
	
}
