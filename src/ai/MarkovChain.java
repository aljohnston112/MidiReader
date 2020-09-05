package ai;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

public class MarkovChain<T> {

	Map<T, ProbFunTree<T>> chain = new HashMap<T, ProbFunTree<T>>();

	ProbFunTree<T> choices;

	Map<T, HashMap<T, Integer>> count = new HashMap<>();

	T lastT;

	T currentT;

	public MarkovChain(List<T> seq) {
		if(seq.size() < 2 ) {
			throw new IllegalArgumentException("List seq passed to MarkovChain must have at least two elements");
		}
		HashSet<T> first = new HashSet<T>();
		first.add(seq.get(0));
		choices = new ProbFunTree<>(first, 1);
		for(int i = 1; i < seq.size(); i++) {
			choices.add(seq.get(i), null);
		}
		T s;
		T e = seq.get(0);
		for(int i = 1; i < seq.size(); i++) {
			s = e;
			e = seq.get(i);
			if(count.containsKey(s)) {
				HashMap<T, Integer> hm = count.get(s);
				if(hm.containsKey(e)) {
					hm.put(e, hm.get(e)+1);
				} else {
					hm.put(e, 1);					
				}
			} else {
				HashMap<T, Integer> hm = new HashMap<>();
				hm.put(e, 1);
				count.put(s, hm);
			}
			if(!chain.containsKey(s)) {
				HashSet<T> hs = new HashSet<T>();
				hs.add(e);
				chain.put(s, new ProbFunTree<>(hs, 1));
			} else {
				ProbFunTree<T> p = chain.get(s);
				if(p.getProbMap().containsKey(e)) {
					double percent = (double)count.get(s).get(e)/(double)(p.getModCount()+1);
					if(percent == 1) {
						p.add(e, null);
					} else {
						p.add(e, null, percent);
					}
				} else {
					p.add(e, null);
				}
			}
		}
		System.out.print(true);
	}
	
	

}
