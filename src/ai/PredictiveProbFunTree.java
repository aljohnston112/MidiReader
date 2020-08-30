package ai;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 *         A class where a list of data is scanned to generate a ProbFunTree.
 * @author Alexander Johnston
 * @since  Copyright 2020
 * @param  <T> The type of the data that will be scanned.
 */
public class PredictiveProbFunTree<T> {

	// Keeps track of the number of entries that have been scanned 
	// in order to keep track of the probabilities of novel entries.
	private int count = 0;

	// The sequence that will be analyzed.
	private LinkedList<T> currentSequence;

	// Sub sequences of the current sequence that will be analyzed.
	private List<List<T>> currentSubSequences;

	// Keeps track of subsequences that show up the most
	private Map<List<T>, Integer> subSequencesOccurences;

	// The ProbFunTree that will be representative of the data passed into the Constructors.
	private ProbFunTree<T> pft;

	public ProbFunTree<T> getPft() {
		return pft;
	}

	// The max amount of layers to be contained in the ProbFunTree
	int maxLayer = -1;

	/**        Creates a probability tree from data and can be used to predict the elements in the future.
	 *         If data is large, computation time may take a long time and memory could run out.
	 *         Use the constructor with maxLayers to set a bound on the probability tree to get 
	 *         faster computation time and less memory usage.
	 * @param  data as the data to be analyzed.
	 * @throws NullPointerException if data is null.
	 * @throws IllegalArgumentException if data is empty.
	 */
	public PredictiveProbFunTree(List<T> data) {
		Objects.requireNonNull(data);
		if(data.isEmpty()) {
			throw new IllegalArgumentException("data passed into the PredictiveProbFunTree contructor must have at least 1 entry\n");
		}
		// Invariants secured
		currentSequence = new LinkedList<T>();
		subSequencesOccurences = new HashMap<List<T>, Integer>();
		Iterator<T> it = data.iterator();
		currentSequence.add(it.next()); count++;
		Set<T> choices = new HashSet<T>();
		int layers = 1;
		choices.add(currentSequence.get(currentSequence.size()-1));
		pft = new ProbFunTree<T>(choices, layers);
		while(it.hasNext()) {
			currentSequence.add(it.next()); count++;
			choices = new HashSet<T>();
			choices.add(currentSequence.get(currentSequence.size()-1));
			parseSubSequences();
			addSubSequencesToTree();
		}
	}

	/**        Creates a probability tree from data and can be used to predict the elements in the future.
	 *         If data is large, computation time may take a long time and memory could run out.
	 *         Use the constructor with maxLayers to set a bound on the probability tree to get 
	 *         faster computation time and less memory usage.
	 * @param  data as the data to be analyzed.
	 * @throws NullPointerException if data is null.
	 * @throws IllegalArgumentException if data is empty, or maxLayers is less than 1.
	 */
	public PredictiveProbFunTree(List<T> data, int maxLayers) {
		Objects.requireNonNull(data);
		if(data.isEmpty()) {
			throw new IllegalArgumentException("data passed into the PredictiveProbFunTree contructor must have at least 1 entry\n");
		}
		if(maxLayers < 1) {
			throw new IllegalArgumentException("maxLayers passed to appendIfPresentToAll() must be at least 1");
		}
		// Invariants secured
		this.maxLayer = maxLayers;
		currentSequence = new LinkedList<T>();
		subSequencesOccurences = new HashMap<List<T>, Integer>();
		Iterator<T> it = data.iterator();
		currentSequence.add(it.next()); count++;
		Set<T> choices = new HashSet<T>();
		int layers = 1;
		choices.add(currentSequence.get(currentSequence.size()-1));
		pft = new ProbFunTree<T>(choices, layers);
		while(it.hasNext()) {
			currentSequence.add(it.next()); count++;
			choices = new HashSet<T>();
			choices.add(currentSequence.get(currentSequence.size()-1));
			parseSubSequences();
			addSubSequencesToTree();
		}
	}

	/** Searches the ProbFunTree for all the current sub-sequences without their last element and adds the last element to the node below. 
	 * 
	 */
	private void addSubSequencesToTree() {
		for(List<T> ll :currentSubSequences) {
			if(ll.size()-1 != -1) {
				List<T> ifPresent = ll.subList(0, ll.size()-1);
				T append = ll.get(ll.size()-1);
				if(this.maxLayer == -1) {
					pft.appendToNodePathToAll(ifPresent, append);
				} else {
					pft.appendToNodePathToAll(ifPresent, append, this.maxLayer);
				}
			}
		}
	}

	/** Finds the subSequences of the currentSequence and adds them to currentSubSequences 
	 *  and adds a count to subSequencesOccurences for every subsequence found.
	 * 
	 */
	private void parseSubSequences() {
		currentSubSequences = new LinkedList<List<T>>();
		List<T> currentSubSequence;
		List<T> currentSubSequenceCopy;
		int count;
		for(int i = 0; i < currentSequence.size()-1; i++) {
			currentSubSequence = currentSequence.subList(i, currentSequence.size());
			currentSubSequenceCopy = new LinkedList<T>();
			for(T t : currentSubSequence) {
				currentSubSequenceCopy.add(t);
			}
			currentSubSequences.add(currentSubSequenceCopy);
			if(subSequencesOccurences.containsKey(currentSubSequenceCopy)) {
				count = subSequencesOccurences.get(currentSubSequenceCopy)+1;
				subSequencesOccurences.put(currentSubSequenceCopy, count);
			} else {
				subSequencesOccurences.put(currentSubSequenceCopy, 1);
			}
		}
	}

}
