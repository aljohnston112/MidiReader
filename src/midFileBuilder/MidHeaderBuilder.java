package midFileBuilder;

import chunks.MidHeader;
import midiFile.MidCs;

/** MidHeaderBuilder is used to build MidiHeader objects.
 *  @author Alexander Johnston
 *  @since  2020
 */
public class MidHeaderBuilder extends MidChunkBuilder {

	/** The formats of a midi file
	 *  
	 */
	public enum Format {
		SINGLE_MULTI_CHANNEL, SYNCHED_TRACKS, SEQUENTIALLY_INDEPENDENT_TRACKS, UNKNOWN
	}
	
	// The format of the midi file
	private Format format = Format.UNKNOWN;

	// The number of tracks in the midi file
	private int nTracks = -1;

	// True when format is drop frame 
	private boolean isDropFrame = false;

	// If true then midi is in ticksPerSeconds else ticksPerQuarterNote
	private boolean isSeconds = false;

	// If isSeconds then ticksPerFrame else ticksPerQuarterNote
	private int ticksPer = -1;
	
	private int frameRate = -1;
		
	/**       Sets the format of the MidHeader to be built.
	 * @param format as the format of the MidHeader.
	 */
	public void setFormat(Format format){
		this.format = format;
	}
	
	/**
	 * @return The format to be used when building a MidHeader.
	 */
	public Format getFormat() {
		return format;
	}

	/**       Sets the number of tracks in the MidHeader to be built.
	 * @param nTracks as the number of tracks in the MidHeader.
	 */
	public void setNTracks(int nTracks) {
		this.nTracks = nTracks;
	}
	
	/**
	 * @return The number of tracks to be used when building a MidHeader.
	 */
	public int getnTracks() {
		return nTracks;
	}

	/**       Sets the ticks per second or the ticks per quarter note based on the division.
	 * @param division as the midi header division used to figure out the ticks per second or the ticks per quarter note.
	 */
	public void setDivision(int division) {
		if((division & MidCs.DIVISION_SMTPE_MASK) == MidCs.DIVISION_SMTPE_MASK){
			isSeconds = true;
			byte frameFormat = (byte) ((division & ~MidCs.DIVISION_SMTPE_MASK) >> 
					MidCs.DIVISION_SMTPE_FRAMES_PER_SEC_SHIFT);
			
			switch(frameFormat) {
			case MidCs.DIVISION_SMTPE_FRAMES_PER_SEC_24 :
				frameRate = 24;
				break;
			case MidCs.DIVISION_SMTPE_FRAMES_PER_SEC_25 :
				frameRate = 25;
				break;
			case MidCs.DIVISION_SMTPE_FRAMES_PER_SEC_30_DROP_FRAME :
				frameRate = 30;
				isDropFrame = true;
				break;
			case MidCs.DIVISION_SMTPE_FRAMES_PER_SEC_30 :
				frameRate = 30;
				break;
			default :
				throw new IllegalArgumentException("The int passed to setDivision is invalid");
			}
			short ticksPerFrame = (short) (division & MidCs.DIVISION_SMTPE_TICK_PER_FRAME_MASK);
			ticksPer = ticksPerFrame;
			System.out.print("Header has ");
			System.out.print(ticksPer);
			System.out.print(" ticks per frame");
			System.out.print("With a frame rate of ");
			System.out.print(frameRate);
			System.out.print(" frames per second");
		} else {
			isSeconds = false;
			ticksPer = division;
			System.out.print("Header has ");
			System.out.print(ticksPer);
			System.out.println(" ticks per quarter note");
		}
	}
	
	/**        Builds a MidHeader with the parameters that have been set.
	 *         Length, format, number of tracks and division must be set before this method is called.
	 * @return A MidHeader with the set parameters.
	 * @throws IllegalArgumentException if the length, format, number of tracks or division has not been set.
	 */
	public MidHeader build() {
		if(length == -1) { 
			throw new IllegalArgumentException("The length for the MidHeader has not been set");
		}
		if(format == Format.UNKNOWN ) { 
			throw new IllegalArgumentException("The format for the MidHeader has not been set");
		}
		if(nTracks == -1) { 
			throw new IllegalArgumentException("The number of tracks for the MidHeader has not been set");
		}
		if(ticksPer == -1) { 
			throw new IllegalArgumentException("The division for the MidHeader has not been set");
		}
		return new MidHeader(format, nTracks, isDropFrame, isSeconds, ticksPer, frameRate, length);
	}

}