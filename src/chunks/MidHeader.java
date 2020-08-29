package chunks;


import midFileBuilder.MidHeaderBuilder.Format;

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
	
	// If isSeconds then ticksPerSeconds else ticksPerQuarterNote
	public final int ticksPer;
	
	/**       Creates a midi header.
	 * @param format as the format of the midi header.
	 * @param nTracks as the number of tracks in the midi header.
	 * @param isDropFrame as true if the format is drop frame.
	 * @param isSeconds as true if the ticks are per second or false if the ticks are per quarter note.
	 * @param ticksPer as the ticks per second or the ticks per quarter note.
	 * @param length as the length of the header in bytes.
	 */
	public MidHeader(Format format, int nTracks, boolean isDropFrame, boolean isSeconds, int ticksPer, long length) {
		super(length);
		this.format = format;
		this.nTracks = nTracks;
		this.isDropFrame = isDropFrame;
		this.isSeconds = isSeconds;
		this.ticksPer = ticksPer;
	}
	
}
