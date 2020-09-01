package midiFileTest;

import midiFile.MidWriter;

public class MidWriterTest {

	public static void main(String[] args) {
		testvariableLengthQuantity();
	}

	private static void testvariableLengthQuantity() {
		int test1 = 0x7F;
		int test2 = 0x80;		
		int test3 = 0x4000;
		int test4 = 0x200000;
		int test5 = 0xFFFFFFF;
		byte[] out1 = MidWriter.variableLengthQuantity(test1);
		byte[] out2 = MidWriter.variableLengthQuantity(test2);
		byte[] out3 = MidWriter.variableLengthQuantity(test3);
		byte[] out4 = MidWriter.variableLengthQuantity(test4);
		byte[] out5 = MidWriter.variableLengthQuantity(test5);
		System.out.println();
		
	}

	
}
