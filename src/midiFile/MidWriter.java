package midiFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import chunks.MidHeader;
import chunks.MidTrack;
import events.MidEvent;
import midFileBuilder.MidHeaderBuilder.Format;

public class MidWriter {

	public static byte[] variableLengthQuantity(long vlq) {
		int max = 0xFFFFFFF;
		if(vlq > max || vlq < 0) {
			throw new IllegalArgumentException("int passed to variableLengthQuantity is out of range");
		}
		int lsbMask = 0b00000000000000000000000001111111;
		int vlqMask = 0b00000000000000000000000010000000;
		byte lsb = (byte) (vlq & lsbMask);
		if(vlq <= lsbMask) {
			byte[] b = {lsb};
			return b;
		} else if(vlq < (lsbMask << 7)) {
			byte msb = (byte) (((vlq & (lsbMask << 7)) >> 7) | vlqMask) ; 
			byte[] b = {msb, lsb};	
			return b;
		} else if(vlq < (lsbMask << 14)) {
			byte msb = (byte) (((vlq & (lsbMask << 14)) >> 14) | vlqMask) ; 
			byte msb2 = (byte) (((vlq & (lsbMask << 7)) >> 7) | vlqMask) ; 
			byte[] b = {msb, msb2, lsb};
			return b;
		} else {
			byte msb = (byte) (((vlq & (lsbMask << 21)) >> 21) | vlqMask) ; 
			byte msb2 = (byte) (((vlq & (lsbMask << 14)) >> 14) | vlqMask) ; 
			byte msb3 = (byte) (((vlq & (lsbMask << 7)) >> 7) | vlqMask) ; 
			byte[] b = {msb, msb2, msb3, lsb};
			return b;
		}
	}

	public static byte[] makeHeader(MidHeader mh) throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		baos.write(MidCs.MIDI_TYPE_HEADER);
		byte[] length = {0,0,0,6};
		baos.write(length);
		Format form = mh.format;
		byte f = -1;
		switch(form) {
		case SINGLE_MULTI_CHANNEL : 
			f = 0;
			break;
		case SYNCHED_TRACKS : 
			f = 1;
			break;
		case SEQUENTIALLY_INDEPENDENT_TRACKS : 
			f = 2;
			break;
		}
		if(f == -1) {
			throw new IllegalArgumentException("MidHeader passed to makeHeader has an invalid format");
		}
		byte[] format = {0, f};
		baos.write(format);
		byte[] bNTracks = {0, 0};
		int nTracks = mh.nTracks;
		int maxNTracks = 0b1111111111111111;
		if(nTracks > maxNTracks || nTracks < 1 || (form == Format.SINGLE_MULTI_CHANNEL && nTracks != 1)) {
			throw new IllegalArgumentException("MidHeader passed to makeHeader has an invalid number of tracks");
		} 
		bNTracks[0] = (byte) ((nTracks & (0b11111111 << 8)) >> 8);
		bNTracks[1] = (byte) ((nTracks & (0b11111111)));
		baos.write(bNTracks);
		byte[] division = {0, 0};
		if(!mh.isSeconds) {
			int tpqn = mh.ticksPer;
			int maxtpqn = 0b111111111111111;
			if(tpqn > maxtpqn || tpqn < 1) {
				throw new IllegalArgumentException("MidHeader passed to makeHeader has an invalid division");
			} 
			division[0] = (byte) ((tpqn & (0b1111111 << 8)) >> 8);
			division[1] = (byte) ((tpqn & (0b11111111)));
		} else {
			int maxtpf = 0b11111111;
			byte tickPerFrame = (byte) mh.ticksPer;
			byte smpteFormat = mh.getSMPTEFormat();
			smpteFormat = (byte) (smpteFormat | 0b10000000);
			if(tickPerFrame > maxtpf || tickPerFrame < 1) {
				throw new IllegalArgumentException("MidHeader passed to makeHeader has an invalid division");
			} 
			division[0] = (byte) smpteFormat;
			division[1] = (byte) tickPerFrame;
		}
		baos.write(division);
		return baos.toByteArray();
	}

	public static byte[] makeTrack(MidTrack mt) throws IOException {
		// Tempo 120
		// time sig 4/4
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		baos.write(MidCs.MIDI_TYPE_TRACK);
		long length = 0;
		ByteArrayOutputStream baosEvents = new ByteArrayOutputStream();
		for(MidEvent me: mt.events) {
			long ticks = me.getTicksFromLastEvent();
			byte[] b = variableLengthQuantity((int)ticks);
			baosEvents.write(b);
			length+=b.length;
			b = me.getEvent();
			baosEvents.write(b);
			length+=b.length;
		}
		byte[] lengthB = new byte[4];
		lengthB[0] = (byte) ((length & (0b11111111 << 24)) >> 24);
		lengthB[1] = (byte) ((length & (0b11111111 << 16)) >> 16);
		lengthB[2] = (byte) ((length & (0b11111111 << 8)) >> 8);
		lengthB[3] = (byte) (length & 0b11111111);
		baos.write(lengthB);
		baos.write(baosEvents.toByteArray());
		return baos.toByteArray();
	}
	
	public static byte[] makeFile(MidFile mf) throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		baos.write(makeHeader(mf.header));
		for(MidTrack mt : mf.tracks) {
		baos.write(makeTrack(mt));
		}
		return baos.toByteArray();
	}


}
