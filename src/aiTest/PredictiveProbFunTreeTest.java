package aiTest;

import java.util.Arrays;
import java.util.List;

import ai.PredictiveProbFunTree;


public class PredictiveProbFunTreeTest {

	public static void main(String[] args) {
		testPredictiveProbFunTreeConstructor();
		
	}

	private static void testPredictiveProbFunTreeConstructor() {
		List<Integer> data = Arrays.asList(1, 2, 3, 4, 1, 2, 3, 5);
		PredictiveProbFunTree<Integer> ppft = new PredictiveProbFunTree<Integer>(data);
		System.out.print(ppft.getPft());
	}
	
}
