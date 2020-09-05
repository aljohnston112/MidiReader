package aiTest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import ai.MarkovChain;

public class MarkovChainTest {

	public static void main(String[] args) {
		testConstructor();
	}

	private static void testConstructor() {
		List<Integer> seq = new ArrayList<>();
		seq.addAll(Arrays.asList(1, 2, 1, 1, 2, 1));
		MarkovChain mc = new MarkovChain(seq);		
	}
	
}
