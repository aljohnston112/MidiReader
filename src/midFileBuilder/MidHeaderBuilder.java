package midFileBuilder;

import java.security.InvalidParameterException;

import javax.activation.UnsupportedDataTypeException;

import file.MidHeader;

public class MidHeaderBuilder extends MidChunkBuilder {

	/** The formats of a midi file
	 *  
	 */
	public enum Format {
		SINGLE, SYNCHED, INDEPENDENT, UNKNOWN
	}
	
	// The format of the midi file
	private Format format = Format.UNKNOWN;

	// The number of tracks in the midi file
	private int nTracks = -1;

	// True when format is drop frame 
	private boolean isDropFrame = false;

	// If true then midi is in ticksPerSeconds else ticksPerQuarterNote
	private boolean isSeconds = false;

	// If isSeconds then ticksPerSeconds else ticksPerQuarterNote
	private int ticksPer = -1;
	
	void setFormat(Format format){
		this.format = format;
	}

	void setNTracks(int nTracks){
		this.nTracks = nTracks;
	}

	void setDivision(int division) throws UnsupportedDataTypeException {
		if((division & 0b1000000000000000) == 0b1000000000000000){
			isSeconds = true;
			byte framesPerSecond = (byte) ((division & 0b0111111111111111) >> 8);
			switch(framesPerSecond) {
			case -24 :
				framesPerSecond = 24;
				break;
			case -25 :
				framesPerSecond = 25;
				break;
			case -29 :
				framesPerSecond = 30;
				isDropFrame = true;
				break;
			case -30 :
				framesPerSecond = 30;
				break;
			default :
				throw new UnsupportedDataTypeException("The int passed to setDivision is invalid");
			}
			short ticksPerFrame = (short) (division & 0b0000000011111111);
			ticksPer = ticksPerFrame*framesPerSecond;
			System.out.print("Header has ");
			System.out.print(ticksPer);
			System.out.println(" ticks per second");
		} else {
			isSeconds = false;
			ticksPer = division;
			System.out.print("Header has ");
			System.out.print(ticksPer);
			System.out.println(" ticks per quarter note");
		}
	}
	
	public MidHeader build() {
		if(length == -1 || format == Format.UNKNOWN || nTracks == -1 | ticksPer == -1) {
			throw new InvalidParameterException();
		}
		return new MidHeader(format, nTracks, isDropFrame, isSeconds, ticksPer, length);
	}

}
