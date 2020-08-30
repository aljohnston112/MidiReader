package ai;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

public class MarkovNode<T> {

	Map<T, ProbFunTree<T>> chain = new HashMap<T, ProbFunTree<T>>();

	public MarkovNode(List<T> seq) {
		if(seq.size() < 2 ) {
			throw new IllegalArgumentException("List passed to MarkovNode must have at least two elements");
		}
		int i = 0;
		chain.put(seq.get(i), new ProbFunTree<T>(new HashSet<>(Arrays.asList(seq.get(++i))), 1));
		i++;
		while(i < seq.size()) {
			if(!chain.containsKey(seq.get(i-1))) {
				chain.put(seq.get(i-1), new ProbFunTree<T>(new HashSet<>(Arrays.asList(seq.get(i))), 1));
			} else {
				chain.get(seq.get(i-1)).add(seq.get(i), null);
			}

		}
	}

}
