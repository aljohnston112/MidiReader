package file;

public class MidCs {

	public final static byte[] MIDI_TYPE_HEADER = {0x4d, 0x54,  0x68,  0x64};
	public final static byte[] MIDI_TYPE_TRACK = {0x4d, 0x54,  0x72,  0x6B};
	public final static int DIVISION_SMTPE_MASK = 0b1000000000000000;
	public final static int DIVISION_SMTPE_FRAMES_PER_SEC_SHIFT = 8;
	public final static int DIVISION_SMTPE_FRAMES_PER_SEC_24 = -24;
	public final static int DIVISION_SMTPE_FRAMES_PER_SEC_25 = -25;
	public final static int DIVISION_SMTPE_FRAMES_PER_SEC_30_DROP_FRAME = -29;
	public final static int DIVISION_SMTPE_FRAMES_PER_SEC_30 = -30;
	public final static int DIVISION_SMTPE_TICK_PER_FRAME_MASK = 0b0000000011111111;

}
