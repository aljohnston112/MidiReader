package file;

import javax.activation.UnsupportedDataTypeException;

public class MidChunkHeader extends MidChunk {

	enum Format {
		SINGLE, SYNCHED, INDEPENDENT
	}
	
	private Format format;
	
	private int nTracks;
	
	private boolean isSeconds;
	
	private boolean isDropFrame = false;
	
	// If isSeconds then ticksPerSeconds else ticksPerQuarterNote
	private int ticksPer;
	
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
	
}
