package midiDevices;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

import javax.sound.midi.MidiDevice;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;

import ai.MarkovChain;
import ai.PredictiveProbFunTree;
import dynamics.Dynamics;
import midiFile.MidFile;
import midiFile.MidReader;
import midiFile.MidWriter;
import notes.TimedNote;
import notes.TimedNoteChannel;
import rhythm.Tempo;

public class Devices {

	public static void main(String[] args) {
		List<MidFile> mfs = MidReader.read(new File("C:\\Users\\aljoh\\Downloads\\Undertale MIDI\\Undertale MIDI\\0\\"));
		MidFile mf = mfs.get(0);
		Tempo tempo = mf.tempo;
		TimedNoteChannel b = mf.tracks.get(2).getTrack(mf.getTicksPerSecond());
		b.quantize(tempo.sixteenthNoteInSeconds, new Dynamics(127));
		b.homogenize();
		b.condense(tempo.sixteenthNoteInSeconds);
		MarkovChain<TimedNote> mc = new MarkovChain(b.noteArray.get(0));
		System.out.println("Original:\n");
		for(int i = 0; i < b.noteArray.get(0).size(); i++) {
			System.out.print(b.noteArray.get(0).get(i));
		}
		System.out.println("MarkovChain output:\n");
		TimedNote tn = mc.fun();
		while(tn != null) {
			System.out.print(tn);
			tn = mc.fun();
		}
		System.out.println("Predictive ProbFunTree output:\n");
		PredictiveProbFunTree<TimedNote> ppft = new PredictiveProbFunTree<>(b.noteArray.get(0));
		int j = 0;
		tn = ppft.fun();
		while(tn != null && j < 100) {
			System.out.print(tn);
			tn = ppft.fun();
		}
		System.out.print(true);
		
		File fo = new File("C:\\Users\\aljoh\\Downloads\\Undertale MIDI\\Undertale MIDI\\0\\out.mid");
		try {
			byte[] out = MidWriter.makeFile(mfs.get(0));
			FileOutputStream fos = new FileOutputStream(fo);
			fos.write(out);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public Optional<MidiDevice> getMSGS() throws MidiUnavailableException {
		MidiDevice.Info[] ds = MidiSystem.getMidiDeviceInfo();
		String msgs = "Microsoft GS Wavetable Synth";
		for(MidiDevice.Info mdi : ds) {
			if(mdi.getName().equals(msgs)) {
				return Optional.of(MidiSystem.getMidiDevice(mdi));
			}
		}
		return Optional.empty();
	}
	
}
