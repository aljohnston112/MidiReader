package chunks;


import midFileBuilder.MidHeaderBuilder.Format;
import midiFile.MidCs;

/** MidHeader is a midi header chunk.
 *  @author Alexander Johnston
 *  @since  2020
 */
public final class MidHeader extends MidChunk {

	// The format of the midi file
	public final Format format;

	// The number of tracks in the midi file
	public final int nTracks;

	// True when format is drop frame 
	public final boolean isDropFrame;

	// If true then midi is in ticksPerSeconds else ticksPerQuarterNote
	public final boolean isSeconds;

	// If isSeconds then ticksPerFrame else ticksPerQuarterNote
	public final int ticksPer;

	public final int frameRate;	

	/**       Creates a midi header.
	 * @param format as the format of the midi header.
	 * @param nTracks as the number of tracks in the midi header.
	 * @param isDropFrame as true if the format is drop frame.
	 * @param isSeconds as true if the ticks are per second or false if the ticks are per quarter note.
	 * @param ticksPer as the ticks per second or the ticks per quarter note.
	 * @param length as the length of the header in bytes.
	 */
	public MidHeader(Format format, int nTracks, boolean isDropFrame, boolean isSeconds, int ticksPer, int frameRate, long length) {
		super(length);
		this.format = format;
		this.nTracks = nTracks;
		this.isDropFrame = isDropFrame;
		this.isSeconds = isSeconds;
		this.frameRate = frameRate;
		this.ticksPer = ticksPer;
	}

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
