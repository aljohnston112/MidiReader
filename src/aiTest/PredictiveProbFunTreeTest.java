package aiTest;

import java.util.ArrayList;


import ai.PredictiveProbFunTree;

public class PredictiveProbFunTreeTest {

	public static void main(String[] args) {
		testPredictiveProbFunTreeConstructor();
	}

	private static void testPredictiveProbFunTreeConstructor() {
		ArrayList<Integer> r = new ArrayList<>();
		for(int i = 0; i < 8; i++) {
			r.add((int)Math.round(Math.random()*3.0));
			System.out.print(r.get(i));
			System.out.print(",");
		}
		System.out.println();
		PredictiveProbFunTree<Integer> ppft = new PredictiveProbFunTree<Integer>(r);
		System.out.print(ppft.getPft());
	}
	
}
