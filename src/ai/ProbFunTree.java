package ai;

import java.io.Serializable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Set;
import java.util.TreeMap;
import java.util.concurrent.ThreadLocalRandom;

/**
 *         A tree node where a set of elements are picked from a probability function to decide which child node 
 *         will produce the next element when fun() is called.
 * @author Alexander Johnston
 * @since  Copyright 2020
 * @param  <T> The type of the elements that make up the elements in the probability function.
 */
public class ProbFunTree<T> implements Serializable, Comparable<T> {

	private static final long serialVersionUID = -6556634307811294014L;

	// The set of elements to be picked from, mapped to the probabilities of getting picked 
	private Map<T, Double> probMap = new TreeMap<T, Double>();

	// The set of elements to be picked from, mapped to the probabilities of getting picked 
	private Map<T, ProbFunTree<T>> children = new HashMap<T, ProbFunTree<T>>();

	// The parent ProbFunTree of this ProbFunTree
	private ProbFunTree<T> parent = null;

	// The last element returned from the probMap of this ProbFunTree
	private T previousElement = null;

	// The unique id of this ProbFunTree
	private int id = 0;

	// The number of layers deep this ProbFunTree is
	private int layer;

	private double maxPercent;

	// The rounding error to prevent over and under flow
	private double roundingError = 0;

	private boolean wasPruned = false;

	private int modCount = 0;

	public int getModCount() {
		return modCount;
	}

	public void resetModCount(int modCount) {
		this.modCount = 0;
	}

	/**        Creates a ProbFunTree where there is an equal chance of getting any element from choices when fun() in called.
	 *         Note that the elements in choices passed into this constructor will NOT be copied and will be added by reference.
	 * @param  choices as the choices to be randomly picked from.
	 * @param  layers as the number of layers for this ProbFunTree to generate.
	 *         Ex: for choices[0, 1] and layers=2, the following data structure will be made,
	 *         <br>{@literal 1st layer: [[0->0.5][1->0.5]]} where the first choice is propagated to a 2nd layer:
	 *         {@literal [[0->[[0->0.5][1->0.5]]][1->[[0->0.5][1->0.5]]]]}.
	 * @throws NullPointerException if choices is null.
	 * @throws IllegalArgumentException if there isn't at least one element in choices, or 
	 *         layers is not at least 1.
	 */
	public ProbFunTree(Set<T> choices, int layers) {
		Objects.requireNonNull(choices);
		if(choices.size() < 1) 
			throw new IllegalArgumentException("Must have at least 1 element in the choices passed to the ProbFunTree constructor\n");
		if(layers < 1) {
			throw new IllegalArgumentException("layers passed into the ProbFunTree constructor must be at least 1");
		}
		// Invariants secured
		this.layer = 0;
		this.maxPercent = 1.0;
		for(T choice : choices) {
			this.probMap.put(choice, 1.0/choices.size());
			modCount++;
		}
		fixProbSum();
		if(layers != this.layer+1) {
			for(T t: this.probMap.keySet()) {
				this.children.put(t, new ProbFunTree<T>(choices, layers,  this));
			}
		}
	}

	public ProbFunTree(Set<T> choices, int layers, double maxPercent){
		Objects.requireNonNull(choices);
		if(choices.size() < 1) 
			throw new IllegalArgumentException("Must have at least 1 element in the choices passed to the ProbFunTree constructor\n");
		if(layers < 1) {
			throw new IllegalArgumentException("layers passed into the ProbFunTree constructor must be at least 1");
		}
		if(maxPercent <= 0 || maxPercent > 1.0) {
			throw new IllegalArgumentException("maxPercent passed into the ProbFunTree constructor must be above 0 and 1.0 or under");
		}
		// Invariants secured
		this.layer = 0;
		this.maxPercent = maxPercent;
		for(T choice : choices) {
			this.probMap.put(choice, 1.0/choices.size());
			modCount++;
		}
		fixProbSum();
		if(layers != this.layer+1) {
			for(T t: this.probMap.keySet()) {
				this.children.put(t, new ProbFunTree<T>(choices, layers, maxPercent, this));
			}
		}	
	}

	private ProbFunTree(Set<T> choices, int layers, double maxPercent, ProbFunTree<T> parent){
		Objects.requireNonNull(choices);
		if(choices.size() < 1) 
			throw new IllegalArgumentException("Must have at least 1 element in the choices passed to the ProbFunTree constructor\n");
		if(layers < 1) {
			throw new IllegalArgumentException("layers passed into the ProbFunTree constructor must be at least 1");
		}
		if(maxPercent <= 0 || maxPercent > 1.0) {
			throw new IllegalArgumentException("maxPercent passed into the ProbFunTree constructor must be above 0 and 1.0 or under");
		}
		// Invariants secured
		this.layer = parent.layer+1;
		this.parent = parent;
		this.maxPercent = maxPercent;
		for(T choice : choices) {
			this.probMap.put(choice, 1.0/choices.size());
			modCount++;
		}
		fixProbSum();
		if(this.layer+1 < layers) {
			for(T t: this.probMap.keySet()) {
				this.children.put(t, new ProbFunTree<T>(choices, layers, maxPercent, this));
			}
		}
	}

	/**        Private constructor for tracking parent nodes in the ProbFunTree.
	 * @param  choices as the elements for the ProbFunTree to generate.
	 * @param  layers as the number of layers to make the ProbFunTree.
	 * @param  parent as the parent node in the ProbFunTree.
	 * @throws NullPointerException if choices is null.
	 * @throws IllegalArgumentException if there isn't at least one element in choices, or 
	 *         layers and currentLayer are not at least 1.	 
	 */
	private ProbFunTree(Set<T> choices, int layers, ProbFunTree<T> parent){
		Objects.requireNonNull(choices);
		if(choices.size() < 1) 
			throw new IllegalArgumentException("Must have at least 1 element in the choices passed to the ProbFunTree constructor\n");
		if(layers < 1) {
			throw new IllegalArgumentException("layers passed into the ProbFunTree constructor must be at least 1");
		}
		// Invariants secured
		this.layer = parent.layer+1;
		this.parent = parent;
		this.maxPercent = 1.0;
		for(T choice : choices) {
			this.probMap.put(choice, 1.0/choices.size());
			modCount++;
		}
		fixProbSum();
		if(this.layer+1 < layers) {
			for(T t: this.probMap.keySet()) {
				this.children.put(t, new ProbFunTree<T>(choices, layers, this));
			}
		}
	}

	/**        Creates a ProbFunTree where getting a key from this ProbFunTree's fun() function is based on the probability values.
	 *         The entries in probMap passed into this constructor will NOT be copied and will be added by reference.
	 * @param  probMap as the Object-probability pairs where the probabilities must add up to 1.0 using double addition.
	 * @param  layers as the number of layers for this ProbFunTree to generate.
	 *         Ex: for probMap {@literal [0->0.25, 1->0.75]} and layers=2, the following data structure will be made,
	 *         <br>{@literal [[0->0.25][1->0.75]]} where the first choice is propagated like so 
	 *         {@literal [[0->[[0->0.25][1->0.75]]][1->[[0->0.25][1->0.75]]]]}.
	 * @throws NullPointerException if probMap is null.
	 * @throws IllegalArgumentException if there isn't at least one entry in probMap, 
	 *         probMap values do not add up to 1.0 using double addition, or 
	 *         layers is not at least 1.
	 */
	public ProbFunTree(Map<T, Double> probMap , int layers){
		Objects.requireNonNull(probMap);
		if(probMap.size() < 1) 
			throw new IllegalArgumentException("Must have at least 1 entry in the probMap passed to the ProbFunTree constructor\n");
		if(layers < 1) {
			throw new IllegalArgumentException("layers passed into the ProbFunTree constructor must be at least 1");
		}
		double sum = 0;
		for(double d : probMap.values()) {
			sum += d;
		}
		if(sum != 1.0) {
			throw new IllegalArgumentException("probMap values must add up to 1.0 using double addition "
					+ "when passed to the ProbFunTree constructor\n");
		}
		// Invariants secured
		this.layer = 0;
		this.maxPercent = 1.0;
		for(Entry<T, Double> choice : probMap.entrySet()) {
			this.probMap.put(choice.getKey(), choice.getValue());
			modCount++;
		}
		fixProbSum();
		if(this.layer+1 != layers) {
			for(T t: probMap.keySet()) {
				this.children.put(t, new ProbFunTree<T>(probMap, layers, this));
			}
		}
	}

	/**        Constructor for making a ProbFunTree under a parent ProbFunTree.
	 * @param  probMap as the Object-probability pairs where the probabilities must add up to 1.0 using double addition.
	 * @param  layers as the number of layers to make the ProbFunTree.
	 * @param  parent as the parent node in the ProbFunTree.
	 * @throws NullPointerException if probMap is null.
	 * @throws IllegalArgumentException if there isn't at least one entry in probMap, 
	 *         probMap entries do not add up to 1.0 using double addition, or
	 *         layers or currentLayer are not at least 1.
	 */
	private ProbFunTree(Map<T, Double> probMap , int layers, ProbFunTree<T> parent){
		Objects.requireNonNull(probMap);
		if(probMap.size() < 1) 
			throw new IllegalArgumentException("Must have at least 1 entry in the probMap passed to the ProbFunTree constructor\n");
		if(layers < 1) {
			throw new IllegalArgumentException("layers passed into the ProbFunTree constructor must be at least 1");
		}
		double sum = 0;
		for(double d : probMap.values()) {
			sum += d;
		}
		if(sum != 1.0) {
			throw new IllegalArgumentException("probMap values must add up to 1.0 using double addition "
					+ "when passed to the ProbFunTree constructor\n");
		}
		// Invariants secured
		this.layer = parent.layer+1;
		this.parent = parent;
		this.maxPercent = 1.0;
		for(Entry<T, Double> choice : probMap.entrySet()) {
			this.probMap.put(choice.getKey(), choice.getValue());
			modCount++;
		}
		fixProbSum();
		if(this.layer+1 != layers) {
			for(T t: probMap.keySet()) {
				this.children.put(t, new ProbFunTree<T>(probMap, layers, this));
			}
		}
	}

	/**        returns the Map of element-probability pairs that make up this ProbFunTree. 
	 *         Any changes in the returned Map will be reflected in this ProbFunTree.
	 * @return the Map of element-probability pairs that make up this ProbFunTree. 
	 *         Any changes in the returned Map will be reflected in this ProbFunTree.
	 */
	public Map<T, Double> getProbMap() {
		return this.probMap;
	}

	/**        Returns the Map of element-ProbFunTree pairs that represent which ProbFunTree 
	 *         will be used to generate the next element given the last returned element. 
	 *         Any changes in the returned Map will be reflected in this ProbFunTree.
	 * @return the Map of element-ProbFunTree pairs that represent which ProbFunTree 
	 *         will be used to generate the next element given the last returned element. 
	 */
	public Map<T, ProbFunTree<T>> getChildMap() {
		return this.children;
	}

	/** Scales the probabilities so they add up to 1.0.
	 * 
	 */
	private void scaleProbs() {
		double scale = 1.0/probSum();
		Set<Entry<T, Double>> probabilities = this.probMap.entrySet();
		for(Entry<T, Double> e : probabilities) {
			e.setValue(e.getValue()*scale);
		}	
		fixProbSum();
	}

	/**
	 * @return the sum of all the probabilities in order to fix rounding error.
	 */
	private double probSum() {
		Collection<Double> probabilities = this.probMap.values();
		double sum = 0;
		for(Double d : probabilities) {
			sum += d;
		}
		return sum;
	}

	/** Fixes rounding error in the probabilities by adding up the probabilities 
	 *  and changing the first probability so all probabilities add up to 1.0.
	 */
	private void fixProbSum() {
		Entry<T, Double> firstProb = this.probMap.entrySet().iterator().next();
		this.roundingError  = 1.0-probSum();
		firstProb.setValue(firstProb.getValue() + this.roundingError);		
	}

	/** Sets the probabilities to there being an equal chance of getting any element from this ProbFunTree.
	 * 
	 */
	public void clearProbs() {
		this.probMap = (new ProbFunTree<T>(this.probMap.keySet(), 1)).probMap;
	}

	/** Sets the probabilities to there being an equal chance of getting any element from this ProbFunTree and it's descendants.
	 * 
	 */
	public void clearAllProbs() {
		this.probMap = (new ProbFunTree<T>(this.probMap.keySet(), 1)).probMap;
		for(ProbFunTree<T> p : this.children.values()) {
			p.clearAllProbs();
		}
	}

	/**       Lowers the probabilities so there is about at most a low chance of getting any element from this ProbFunTree.
	 * @param low as the low chance.
	 */
	public void lowerProbs(double low) {
		Collection<T> probs = this.probMap.keySet();
		for(T t : probs) {
			while(probMap.get(t) > low) {
				bad(t, 0.1);
			}
		}
	}

	/**       Lowers the probabilities so there is about at most a low chance of getting any element from this ProbFunTree.
	 * @param low as the low chance.
	 */
	public void lowerAllProbs(double low) {
		lowerProbs(low);
		for(ProbFunTree<T> p : this.children.values()) {
			p.lowerProbs(low);
		}
	}


	/** Propagation of past values determine the next value, therefore this method
	 *  clears the past values, but not the probabilities produced by feedback,
	 *  so the next generation is a sequence that starts from the first layer.
	 */
	public void clearHistory() {
		this.previousElement = null;
		for(ProbFunTree<T> t : this.children.values()) {
			t.clearHistory();
		}
	}

	/**        Adds element to this ProbFunTree, making the probability equal to 1.0/n
	 *         where n is the number of elements contained in this ProbFunTree, 
	 *         and appends elements as a child ProbFunTree, where elements are the choices.
	 * @param  element as the element to add to this ProbFunTree.
	 * @param  elements as the elements to be picked from after fun() returns element.
	 *         It may be empty or null if no elements should be picked from after fun() is called.
	 *         In this case, the probability function in this ProbFunTree will be used to generate 
	 *         the next value based on the Objects it contains.
	 * @throws NullPointerException if element is null.
	 */
	public void add(T element, Set<T> elements) {
		Objects.requireNonNull(element);
		// Invariants secured
		double probability = 1.0/(this.probMap.size());
		if(!this.probMap.containsKey(element)) {
			this.probMap.put(element, probability);
			modCount++;
		}
		scaleProbs();
		if(!this.children.containsKey(element) && elements!= null && !elements.isEmpty()) {
			int layers = this.layer+2;
			this.children.put(element, new ProbFunTree<>(elements, layers, this));
		} else if(this.children.containsKey(element) && elements!= null && !elements.isEmpty()) {
			for(T t : elements) {
				this.children.get(element).add(t, null);
			}
		}
	}

	/**        Adds an element to every descendant of this ProbFunTree and this ProbFunTree, 
	 *         making the probability equal to 1.0/n, 
	 *         where n is the number of elements contained in this ProbFunTree, 
	 *         and appends elements as a child ProbFunTree, where elements are the choices.
	 *         If the element already exists in a node, then the element will not be overwritten.
	 *         In order to overwrite old element probabilities, you must remove the element using removeFromAll().
	 * @param  element as the element to add to this ProbFunTree and it's descendants.
	 * @param  elements as the elements to be picked from after fun() returns element from the descendant with the greatest depth.
	 *         It may be empty or null if no elements should be picked from after fun() is called.
	 *         In this case, the probability function in this ProbFunTree will be used to generate 
	 *         the next value based on the Objects it contains.
	 * @throws NullPointerException if element is null.
	 */
	public void addToAll(T element, Set<T> elements) {
		Objects.requireNonNull(element);
		// Invariants secured
		add(element, elements);
		for(Entry<T, ProbFunTree<T>> p : this.children.entrySet()) {
			if(!p.getKey().equals(element)) {
				p.getValue().addToAll(element, elements);
			}
		}
	}

	/**        Adds an element to this ProbFunTree with the specified probability.
	 *         If the element exists in this ProbFunTree then it's probability will be overwritten with percent.
	 * @param  element as the element to add to this ProbFunTree.
	 * @param  elements as the elements to be picked from after fun() returns element.
	 *         It may be empty or null if no elements should be picked from after fun() is called.
	 *         In this case, the probability function in this ProbFunTree will be used to generate 
	 *         the next value based on the Objects it contains.	 
	 * @param  percent between 0 and 1 exclusive, as the chance of this ProbFunTree returning element.
	 * @throws NullPointerException if element is null.
	 * @throws IllegalArgumentException if percent is not between 0.0 and 1.0 (exclusive).
	 */
	public void add(T element, Set<T> elements, double percent) {
		Objects.requireNonNull(element);
		if(percent >= 1.0 || percent <= 0.0) {
			throw new IllegalArgumentException("percent passed to add() is not between 0.0 and 1.0 (exclusive)");
		}
		// Invariants secured
		double scale = (percent);
		Set<Entry<T, Double>> probabilities = this.probMap.entrySet();
		for(Entry<T, Double> e : probabilities) {
			e.setValue(e.getValue()*scale);
		}
		this.probMap.put(element, percent);
		modCount++;
		scaleProbs();
		if(!this.children.isEmpty() && !this.children.containsKey(element) && elements != null && !elements.isEmpty()) {
			int layers = this.layer+2;
			this.children.put(element, new ProbFunTree<>(elements, layers, this));
			for(ProbFunTree<T> t : this.children.get(element).children.values()) {
				t.remove(element);
				t.add(element, elements, percent);
			}
		} else if(this.children.containsKey(element) && elements!= null && !elements.isEmpty()) {
			for(T t : elements) {
				this.children.get(element).add(t, null, percent);
			}
		}
	}

	/**        Adds an element to every node in this ProbFunTree with the specified probability.
	 *         If the element already exists in a node, then the element will not be updated.
	 *         In order to overwrite old element probabilities, you must remove the element using removeFromAll().
	 * @param  element as the element to add to every node.
	 * @param  elements as the elements to be picked from after fun() returns element.
	 *         It may be empty or null if no elements should be picked from after fun() is called.
	 *         In this case, the probability function in this ProbFunTree will be used to generate 
	 *         the next value based on the Objects it contains.	 
	 * @param  percent between 0 and 1 exclusive, as the chance of 
	 *         the parent and all it's children of this ProbFunTree returning element.
	 * @throws NullPointerException if element is null.
	 * @throws IllegalArgumentException if percent is not between 0.0 and 1.0 (exclusive).
	 */
	public void addToAll(T element, Set<T> elements, double percent) {
		Objects.requireNonNull(element);
		if(percent >= 1.0 || percent <= 0.0) {
			throw new IllegalArgumentException("percent passed to add() is not between 0.0 and 1.0 (exclusive)");
		}
		// Invariants secured
		add(element, elements, percent);
		for(Entry<T, ProbFunTree<T>> p : this.children.entrySet()) {
			if(!p.getKey().equals(element)) {
				p.getValue().addToAll(element, elements, percent);
			}
		}

	}

	/**        Adds elements to a new layer that will be appended to this ProbFunTree's descendants that have the greatest depth,
	 *         making the probability of each element added equal to 1.0/n, 
	 *         where n is the number of elements in choices.
	 * @param  choices as the elements to append to this ProbFunTree's descendants that have the greatest depth.
	 * @throws NullPointerException if choices is null.
	 * @throws IllegalArgumentException if choices doesn't have at least one item.
	 */
	public void appendLayer(Set<T> choices) {
		Objects.requireNonNull(choices);
		if(choices.size() < 1) 
			throw new IllegalArgumentException("Must have at least 1 element in the choices passed to appendLayer\n");
		// Invariants secured
		appendLayer(this, choices);
	}

	/**        Adds elements to a new layer that will be appended to this ProbFunTree's descendants that have the greatest depth,
	 *         making the probability of each element added equal to 1.0/n, 
	 *         where n is the number of elements in choices.
	 * @param  parent as the parent ProbFunTree to check the children of 
	 *         to see if they are the descendants that have the greatest depth.
	 *         If they are, a layer will be appended as a node below.
	 * @param  choices as the elements to append to this ProbFunTree's descendants that have the greatest depth.
	 * @throws NullPointerException if choices or parent is null.
	 * @throws IllegalArgumentException if choices doesn't have at least one item.
	 */
	public void appendLayer(ProbFunTree<T> parent, Set<T> choices) {
		Objects.requireNonNull(parent);
		Objects.requireNonNull(choices);
		if(choices.size() < 1) 
			throw new IllegalArgumentException("Must have at least 1 element in the choices passed to appendLayer\n");
		// Invariants secured
		for(ProbFunTree<T> child : parent.children.values()) {
			if(child.children.isEmpty()) {
				for(T t : child.probMap.keySet()) {
					int layers = this.layer+3;
					child.children.put(t, new ProbFunTree<T>(choices, layers, parent));
				}
			} else {
				appendLayer(child, choices);
			}
		}
	}

	/**        Adds probMap keys as the elements to be returned when fun() is called 
	 *         to a new layer that will be appended to this ProbFunTree's descendants that have the greatest depth,
	 *         making the probability of each element added equal to the corresponding values in probMap.
	 * @param  probMap as the Object-probability pairs where the probabilities must add up to 1.0 using double addition.
	 * @throws NullPointerException if probMap is null.
	 * @throws IllegalArgumentException if probMap doesn't have at least one entry, 
	 *         or probMap values aren't under 1.0 using double addition.
	 */
	public void appendLayer(Map<T, Double> probMap) {
		Objects.requireNonNull(probMap);
		if(probMap.size() < 1) 
			throw new IllegalArgumentException("Must have at least 1 entry in the probMap passed to appendLayer\n");
		double sum = 0;
		for(double d : probMap.values()) {
			sum += d;
		}
		if(sum != 1.0) {
			throw new IllegalArgumentException("probMap must have values that add up to 1.0 using double addition when passed to appendLayer\n");
		}
		// Invariants secured
		appendLayer(this, probMap);
	}

	/**        Adds probMap keys as the elements to be returned when fun() is called 
	 *         to a new layer that will be appended to this ProbFunTree's descendants that have the greatest depth,
	 *         making the probability of each element added equal to the corresponding values in probMap.
	 * @param  parent as the parent ProbFunTree to check the children of 
	 *         to see if they are the descendants that have the greatest depth.
	 *         If they are, a layer will be appended as a node below.
	 * @param  probMap as the Object-probability pairs where the probabilities must add up to 1.0 using double addition.
	 * @throws NullPointerException if parent or probMap is null.
	 * @throws IllegalArgumentException if probMap doesn't have at least one entry, 
	 *         or probMap values don't add up to 1.0 using double addition.
	 */
	public void appendLayer(ProbFunTree<T> parent, Map<T, Double> probMap) {
		Objects.requireNonNull(probMap);
		Objects.requireNonNull(parent);
		if(probMap.size() < 1) {
			throw new IllegalArgumentException("Must have at least 1 element in the choices passed to appendLayer\n");
		}
		double sum = 0;
		for(double d : probMap.values()) {
			sum += d;
		}
		if(sum != 1.0) {
			throw new IllegalArgumentException("probMap must have values that add up to 1.0 using double addition when passed to appendLayer\n");
		}
		// Invariants secured
		for(ProbFunTree<T> child : parent.children.values()) {
			if(child.children.isEmpty()) {
				for(T t : child.probMap.keySet()) {
					int layers = this.layer+3;
					child.children.put(t, new ProbFunTree<T>(probMap, layers, parent));
				}
			} else {
				appendLayer(child, probMap);
			}
		}
	}

	/**        Removes an element from this ProbFunTree unless there is only one element.
	 * @param  element as the element to remove from this ProbFunTree.
	 * @return True if this ProbFunTree's parent contained the element and it was removed, else false.
	 * @throws NullPointerException if element is null.
	 */
	public boolean remove(T element) {
		Objects.requireNonNull(element);
		if(parentSize() == 1) {
			return false;
		}
		// Invariants secured
		if(this.probMap.remove(element) == null) {
			return false;
		} else {
			this.children.remove(element);
		}
		scaleProbs();
		modCount--;
		return true;
	}

	/**        Removes an element from every node in this ProbFunTree.
	 *         If an node in the tree has only 1 element, then element will not be removed that that specific node.
	 * @param  element as the element to remove from every node in this ProbFunTree.
	 * @throws NullPointerException if element is null.
	 */
	public void removeFromAll(T element) {
		Objects.requireNonNull(element);
		if(parentSize() != 1) {
			remove(element);
		}
		// Invariants secured
		for(ProbFunTree<T> p : this.children.values()) {
			p.removeFromAll(element);
		}
	}

	/** Removes elements with the lowest probability of occurring when fun() is called from this ProbFunTree.
	 *  If elements have the same maximum probability of occurring, no elements will be removed.
	 *  If, after a removal, elements have the same maximum probability of occurring, no more elements will be removed.	 
	 *  If parentSize() == 1, no elements will be removed.
	 *  If parentSize() == 1 after a removal, no more elements will be removed.
	 */
	public void prune() {
		double min = this.probMap.values().stream().parallel().min(Double::compare).orElse(-1.0);
		double max = this.probMap.values().stream().parallel().max(Double::compare).orElse(-1.0);
		if(max == min) {
			return;
		}
		if(min == -1) {
			return;
		} else if(parentSize() == 1) {
			return;
		} else {
			Set<Entry<T, Double>> probabilities = this.probMap.entrySet();
			Iterator<Entry<T, Double>> it = probabilities.iterator();
			Entry<T, Double> e;
			while(it.hasNext()) {
				e = it.next();
				if(e.getValue() <= min+this.roundingError && e.getValue() < max-this.roundingError) {
					it.remove();
					modCount--;
					this.children.remove(e.getKey());
					if(parentSize() == 1) {
						scaleProbs();
						return;
					}
				}
			}
			scaleProbs();
		}
	}

	/** Removes elements with the lowest probability of occurring when fun() is called from this ProbFunTree.
	 *  If elements have the same maximum probability of occurring, no elements will be removed.
	 *  If, after a removal, elements have the same maximum probability of occurring, no elements will be removed.	
	 *  If a node has one element, no elements will be removed.
	 *  If a node has one element after a removal, no more elements will be removed.
	 */
	public void pruneAll() {
		prune();
		for(ProbFunTree<T> p : this.children.values()) {
			p.pruneAll();
		}
	}

	/**        Removes elements with probabilities less than or equal to percent chance of occurring 
	 *         when fun() is called from this ProbFunTree. 
	 *         If a node has one element, no elements will be removed.
	 *         If a node has one element after a removal, no more elements will be removed.
	 * @param  percent as the upper limit, inclusive, of the probability of elements being returned to be removed from this ProbFunTree.
	 * @throws IllegalArgumentException if percent is not between 0.0 and 1.0 (exclusive)
	 */
	public void prune(double percent) {
		if(percent >= 1.0 || percent <= 0.0) {
			throw new IllegalArgumentException("percent passed to prune() is not between 0.0 and 1.0 (exclusive)");
		}
		// Invariants secured
		double max = this.probMap.values().stream().parallel().max(Double::compare).orElse(-1.0);
		double min = this.probMap.values().stream().parallel().min(Double::compare).orElse(-1.0);
		if(parentSize() == 1 || (max <= percent && min == max)) {
			return;
		} else {
			Set<Entry<T, Double>> probabilities = this.probMap.entrySet();
			Iterator<Entry<T, Double>> it = probabilities.iterator();
			Entry<T, Double> e;
			while(it.hasNext()) {
				e = it.next();
				if(e.getValue() <= percent+this.roundingError && e.getValue() < max-this.roundingError) {
					it.remove();
					modCount--;
					this.children.remove(e.getKey());
					if(parentSize() == 1) {
						scaleProbs();
						return;
					}
				}
			}
			scaleProbs();
		}
	}

	/**        Removes elements with probabilities less than or equal to percent chance of occurring 
	 *         when fun() is called from this ProbFunTree and it's descendants. 
	 *         If a node has one element, no elements will be removed.
	 *         If a node has one element after a removal, no more elements will be removed.
	 * @param  percent as the upper limit, inclusive, of the probability of elements being returned to be removed from this ProbFunTree.
	 * @throws IllegalArgumentException if percent is not between 0.0 and 1.0 (exclusive)
	 */
	public void pruneAll(double percent) {
		if(percent >= 1.0 || percent <= 0.0) {
			throw new IllegalArgumentException("percent passed to pruge() is not between 0.0 and 1.0 (exclusive)");
		}
		// Invariants secured
		if(parentSize() != 1) {
			prune(percent);
		} 
		for(ProbFunTree<T> p : this.children.values()) {
			p.pruneAll(percent);
		}
	}

	/**        Appends elementToAdd to the node at the end of traversing nodePath. 
	 *         If there is no child node on the last node, a new child node will be created.
	 *         If nodePath is not part of this tree, elementToAdd will not be added.
	 * @param  nodePath as the List of Objects in the order to look for in this ProbFunTree.
	 * @param  elementToAppend as the element to add to a child node under the node containing the last element in nodePath.
	 * @throws NullPointerException if nodePath or elementToAdd are null.
	 * @throws IllegalArgumentException if nodePath is empty.
	 */
	public void appendToNodePath(List<T> nodePath, T elementToAppend) {
		Objects.requireNonNull(nodePath);
		Objects.requireNonNull(elementToAppend);
		if(nodePath.isEmpty()) {
			throw new IllegalArgumentException("Must have at least one entry in nodePath passed to appendToNodePath()");
		}
		// Invariants secured
		if(nodePath.size() == 1 && this.probMap.containsKey(nodePath.get(0))) {
			Set<T> s = new HashSet<>();
			s.add(elementToAppend);
			add(nodePath.get(0), s);
			return;
		}
		Iterator<T> it = nodePath.iterator();
		T t = it.next();
		ProbFunTree<T> pft = this.children.get(t);
		ProbFunTree<T> prevPFT = null;
		while(it.hasNext() && pft != null) {
			t = it.next();
			prevPFT = pft;
			pft = pft.children.get(t);
			if(!it.hasNext() && pft == null && prevPFT.probMap.containsKey(t)) {
				Set<T> s = new HashSet<>();
				s.add(elementToAppend);
				prevPFT.add(t, s);
				return;
			}
		}
		if(pft != null && prevPFT != null && !it.hasNext()) {
			Set<T> s = new HashSet<>();
			s.add(elementToAppend);
			prevPFT.add(t, s);
		}
	}

	/**        Removes elementToRemove from the node at the end of traversing nodePath. 
	 *         If nodePath is not part of this tree, elementToRemove will not be removed.
	 * @param  nodePath as the List of Objects in the order to look for in this ProbFunTree.
	 * @param  elementToRemove as the element toremove from the child node
	 *         under the node containing the last element in nodePath.
	 * @throws NullPointerException if nodePath or elementToAdd are null.
	 * @throws IllegalArgumentException if nodePath is empty.
	 */
	public void removeFromNodePath(List<T> nodePath, T elementToRemove) {
		Objects.requireNonNull(nodePath);
		Objects.requireNonNull(elementToRemove);
		if(nodePath.isEmpty()) {
			throw new IllegalArgumentException("Must have at least one entry in nodePath passed to removeFromNodePath()");
		}
		// Invariants secured
		if(nodePath.size() == 1 && this.probMap.containsKey(nodePath.get(0))) {
			children.remove(elementToRemove);
			return;
		}
		Iterator<T> it = nodePath.iterator();
		T t = it.next();
		ProbFunTree<T> pft = this.children.get(t);
		ProbFunTree<T> prevPFT = null;
		while(it.hasNext() && pft != null) {
			t = it.next();
			prevPFT = pft;
			pft = pft.children.get(t);
			if(!it.hasNext() && pft == null && prevPFT.probMap.containsKey(t)) {
				prevPFT.remove(elementToRemove);
				return;
			}
		}
		if(prevPFT != null && !it.hasNext()) {
			if(prevPFT.probMap.size() == 1) {
				Set<T> st = prevPFT.probMap.keySet();
				Iterator<T> itst = st.iterator();
				prevPFT.children.remove(itst.next());
			} else {
				prevPFT.remove(elementToRemove);
			}
		}
	}

	/**        Removes remove if the last element in nodePath is a leafNode from all branches in this ProbFunTree. 
	 *         If nodePath is not part of this tree, nothing will be removed.
	 * @param  nodePath as the List of Objects in the order to look for in this ProbFunTree.
	 * @param  remove as the element to remove if the last node from nodePath is a leaf node.
	 * @throws NullPointerException if nodePath or remove are null.
	 * @throws IllegalArgumentException if nodePath is empty.
	 */
	public void removeLeafNodesFromAll(List<T> nodePath, T remove) {
		Objects.requireNonNull(remove);
		Objects.requireNonNull(nodePath);
		if(nodePath.isEmpty()) {
			throw new IllegalArgumentException("Must have at least one entry in nodePath passed to removeLeafNodesFromAll()");
		}
		// Invariants secured
		removeLeafNodesFromAllHelper(nodePath, remove);
		untagParents();
	}

	private void untagParents() {
		for(ProbFunTree<T> t : children.values()) {
			t.untagParents();
		}		
		this.wasPruned = false;
	}

	private void removeLeafNodesFromAllHelper(List<T> nodePath, T remove) {
		for(ProbFunTree<T> t : children.values()) {
			t.removeLeafNodesFromAll(nodePath, remove);
		}		
		if(!wasPruned) {
			removeLeafNodes(nodePath, remove);
		}
	}

	/**        Removes remove if the last element in nodePath is a leafNode. 
	 *         If nodePath is not part of this tree, nothing will be removed.
	 * @param  nodePath as the List of Objects in the order to look for in this ProbFunTree.
	 * @param  remove as the element to remove if the last node from nodePath is a leaf node.
	 * @throws NullPointerException if nodePath or remove are null.
	 * @throws IllegalArgumentException if nodePath is empty.
	 */
	public void removeLeafNodes(List<T> nodePath, T remove) {
		Objects.requireNonNull(remove);
		Objects.requireNonNull(nodePath);
		if(nodePath.isEmpty()) {
			throw new IllegalArgumentException("Must have at least one entry in nodePath passed to removeLeafNodes()");
		}
		// Invariants secured
		if(nodePath.size() == 1 && this.probMap.containsKey(nodePath.get(0)) && 
				this.children.containsKey(remove) && this.children.get(remove).children.isEmpty()) {
			this.children.remove(remove);
			ProbFunTree<T> parent = this.parent;
			this.wasPruned = true;
			int k = 0;
			while(parent != null && k < nodePath.size()) {
				parent.wasPruned = true;
				parent = parent.parent;
				k++;
			}
			return;
		}
		Iterator<T> it = nodePath.iterator();
		T t = it.next();
		ProbFunTree<T> pft = this.children.get(t);
		ProbFunTree<T> prevPFT = null;
		while(it.hasNext() && pft != null) {
			t = it.next();
			prevPFT = pft;
			pft = pft.children.get(t);
		}
		if(pft != null && prevPFT != null && !it.hasNext() && pft.children.isEmpty()) {
			prevPFT.children.remove(remove);
			ProbFunTree<T> parent = this.parent;
			this.wasPruned = true;
			int k = 0;
			while(parent != null && k < nodePath.size()) {
				parent.wasPruned = true;
				parent = parent.parent;
				k++;
			}
		}
	}

	/**        Appends elementToAdd to the node at the end of traversing nodePath. 
	 *         If there is no child node on the last node, a new child node will be created.
	 *         If nodePath is not part of this tree, elementToAdd will not be added.
	 * @param  nodePath as the List of Objects in the order to look for in this ProbFunTree.
	 * @param  elementToAppend as the element to add to a child node under the node containing the last element in nodePath.
	 * @param  maxLayer as the maximum number of layers the ProbFunTree should contain. 
	 *         This will prevent extra layers with elementToAppend being added after the maximum number of layers.
	 * @throws NullPointerException if nodePath or elementToAdd are null.
	 * @throws IllegalArgumentException if nodePath is empty, or maxLayer is less than 1.
	 */
	public void appendToNodePath(List<T> nodePath, T elementToAppend, int maxLayer) {
		Objects.requireNonNull(nodePath);
		Objects.requireNonNull(elementToAppend);
		if(nodePath.isEmpty()) {
			throw new IllegalArgumentException("Must have at least one entry in nodePath passed to appendToNodePath()");
		}
		if(maxLayer < 1) {
			throw new IllegalArgumentException("maxLayer passed to appendToNodePath() must be at least 1");
		}
		// Invariants secured
		if(nodePath.size() == 1 && this.probMap.containsKey(nodePath.get(0)) && this.layer < maxLayer) {
			Set<T> s = new HashSet<>();
			s.add(elementToAppend);
			add(nodePath.get(0), s);
			return;
		}
		Iterator<T> it = nodePath.iterator();
		T t = it.next();
		ProbFunTree<T> pft = this.children.get(t);
		ProbFunTree<T> prevPFT = null;
		while(it.hasNext() && pft != null) {
			t = it.next();
			prevPFT = pft;
			pft = pft.children.get(t);
			if(!it.hasNext() && pft == null && prevPFT.probMap.containsKey(t)  && prevPFT.layer < maxLayer) {
				Set<T> s = new HashSet<>();
				s.add(elementToAppend);
				prevPFT.add(t, s);
				return;
			}
		}
		if(prevPFT != null && !it.hasNext()  && prevPFT.layer < maxLayer) {
			Set<T> s = new HashSet<>();
			s.add(elementToAppend);
			prevPFT.add(t, s);
		}
	}

	/**        Adds elementToAdd to the child node at the end of traversing nodePath from every node. 
	 *         If there is no child node for one instance of nodePath, a new child node will be created.
	 *         If nodePath is not part of this tree, elementToAdd will not be added.
	 * @param  nodePath as the List of Objects in the order to look for in this ProbFunTree.
	 * @param  elementToAppend as the element to add to a the child nodes under the nodes containing the last elements in nodePath. 
	 * @throws NullPointerException if nodePath or elementToAdd are null.
	 * @throws IllegalArgumentException if nodePath is empty.
	 */
	public void appendToNodePathToAll(List<T> nodePath, T elementToAppend) {
		Objects.requireNonNull(nodePath);
		Objects.requireNonNull(elementToAppend);
		if(nodePath.isEmpty()) {
			throw new IllegalArgumentException("Must have at least one entry in nodePath passed to appendToNodePathToAll()");
		}
		// Invariants secured
		for(ProbFunTree<T> t : children.values()) {
			t.appendToNodePathToAll(nodePath, elementToAppend);
		}
		appendToNodePath(nodePath, elementToAppend);
	}

	/**        Adds elementToAdd to the child node at the end of traversing nodePath from every node. 
	 *         If there is no child node for one instance of nodePath, a new child node will be created.
	 *         If nodePath is not part of this tree, elementToAdd will not be added.
	 * @param  nodePath as the List of Objects in the order to look for in this ProbFunTree.
	 * @param  elementToAppend as the element to add to a the child nodes under the nodes containing the last elements in nodePath.
	 * @param  maxLayer as the maximum number of layers the ProbFunTree should contain. 
	 *         This will prevent extra layers with elementToAppend being added after the maximum number of layers.	
	 * @throws NullPointerException if nodePath or elementToAdd are null.
	 * @throws IllegalArgumentException if nodePath is empty, or maxLayer is less than 1.
	 */
	public void appendToNodePathToAll(List<T> nodePath, T elementToAppend, int maxLayer) {
		Objects.requireNonNull(nodePath);
		Objects.requireNonNull(elementToAppend);
		if(nodePath.isEmpty()) {
			throw new IllegalArgumentException("Must have at least one entry in nodePath passed to appendToNodePathToAll()");
		}
		if(maxLayer < 1) {
			throw new IllegalArgumentException("maxLayer passed to appendToNodePathToAll() must be at least 1");
		}
		// Invariants secured
		for(ProbFunTree<T> t : children.values()) {
			t.appendToNodePathToAll(nodePath, elementToAppend, maxLayer);
		}
		appendToNodePath(nodePath, elementToAppend, maxLayer);
	}

	/**        Adjust the probability to make element more likely to be returned when fun() is called from this ProbFunTree.
	 * @param  element as the element to make appear more often
	 * @param  percent as the percentage between 0 and 1 (exclusive), 
	 *         of the probability of getting element to add to the probability.
	 * @return the adjusted probability.
	 * @throws NullPointerException if element is null.
	 * @throws IllegalArgumentException if the percent isn't between 0 and 1 exclusive.
	 */
	public double good(T element, double percent) {
		Objects.requireNonNull(element);
		if(percent >= 1.0 || percent <= 0.0) {
			throw new IllegalArgumentException("percent passed to good() is not between 0.0 and 1.0 (exclusive)");
		}
		// Invariants secured
		Double oldProb = this.probMap.get(element);
		if(oldProb == null) {
			return -1;
		}
		double add;
		if(oldProb > 0.5) 
			add = ((1.0-oldProb)*percent);
		else 
			add = (oldProb*percent);
		if(oldProb+add >= (maxPercent-this.roundingError))
			return oldProb;
		double goodProbability = oldProb+add;
		this.probMap.put(element, goodProbability);
		double leftover = 1.0-goodProbability;
		double sumOfLeftovers = probSum() - goodProbability;
		double leftoverScale = leftover/sumOfLeftovers;
		for(Entry<T, Double> e : this.probMap.entrySet()) {
			e.setValue(e.getValue()*leftoverScale);
		}
		this.probMap.put(element, goodProbability);
		modCount++;
		fixProbSum();
		return this.probMap.get(element);
	}

	/**        Adjust the probabilities to make the elements more likely to be returned when fun() is called
	 *         in the order they appear in elements.
	 * @param  elements as the elements to make appear more often in the order they should appear in.
	 * @param  percent as the percentage between 0 and 1 (exclusive), 
	 *         of the probabilities of getting the elements to add to the probabilities.
	 * @throws NullPointerException if elements is null.
	 * @throws IllegalArgumentException if the percent isn't between 0 and 1 exclusive
	 *         or elements is empty.
	 */
	public void good(List<T> elements, double percent) {
		Objects.requireNonNull(elements);
		if(elements.isEmpty()){
			throw new IllegalArgumentException("elements passed to good() must not be empty");
		}
		if(percent >= 1.0 || percent <= 0.0) {
			throw new IllegalArgumentException("percent passed to good() is not between 0.0 and 1.0 (exclusive)");
		}
		// Invariants secured
		List<T> l = new ArrayList<>();
		for(T t : elements) {
			l.add(t);
		}
		good(l.get(0), percent);
		if(l.size() == 1) {
			return;
		}
		this.children.get(l.remove(0)).good(l, percent);
	}

	/**        Adjust the probability to make element less likely to be returned when fun() is called from this ProbFunTree.
	 * @param  element as the element to make appear less often
	 * @param  percent as the percentage between 0 and 1 (exclusive), 
	 *         of the probability of getting element to subtract from the probability.
	 * @return the adjusted probability or -1 if this ProbFunTree didn;t contain the element.
	 * @throws NullPointerException if element is null.
	 * @throws IllegalArgumentException if the percent isn't between 0 and 1 exclusive.
	 */
	public double bad(T element, double percent) {
		Objects.requireNonNull(element);
		if(percent >= 1.0 || percent <= 0.0) {
			throw new IllegalArgumentException("percent passed to good() is not between 0.0 and 1.0 (exclusive)");
		}
		// Invariants secured
		Double oldProb = this.probMap.get(element);
		if(oldProb == null) {
			return -1;
		}
		double sub = (oldProb*percent);
		if(oldProb-sub <= this.roundingError)
			return oldProb;
		double badProbability = oldProb-sub;
		this.probMap.put(element, badProbability);
		double leftover = 1.0-badProbability;
		double sumOfLeftovers = probSum() - badProbability;
		double leftoverScale = leftover/sumOfLeftovers;
		for(Entry<T, Double> e : this.probMap.entrySet()) {
			e.setValue(e.getValue()*leftoverScale);
		}
		this.probMap.put(element, badProbability);
		modCount++;
		fixProbSum();
		return this.probMap.get(element);
	}

	/**        Adjust the probabilities to make the elements less likely to be returned when fun() is called
	 *         in the order they appear in elements.
	 * @param  elements as the elements to make appear less often in the order they should not appear in.
	 * @param  percent as the percentage between 0 and 1 (exclusive), 
	 *         of the probabilities of getting the elements to subtract from the probabilities.
	 * @throws NullPointerException if elements is null.
	 * @throws IllegalArgumentException if the percent isn't between 0 and 1 exclusive 
	 *         or elements is empty.
	 */
	public void bad(List<T> elements, double percent) {
		Objects.requireNonNull(elements);
		if(elements.isEmpty()){
			throw new IllegalArgumentException("elements passed to bad() must not be empty");
		}
		if(percent >= 1.0 || percent <= 0.0) {
			throw new IllegalArgumentException("percent passed to bad() is not between 0.0 and 1.0 (exclusive)");
		}
		// Invariants secured
		List<T> l = new ArrayList<>();
		for(T t : elements) {
			l.add(t);
		}
		bad(l.get(0), percent);
		if(l.size() == 1) {
			return;
		}
		this.children.get(l.remove(0)).bad(l, percent);
	}

	/**        Returns a randomly picked element from this ProbFunTree, based on the previously returned elements.
	 * @return a randomly picked element from this ProbFunTree.
	 *         Any changes in the element will be reflected in this ProbFunTree.
	 */
	public T fun() {
		ArrayList<T> previousElements = new ArrayList<T>();
		if(this.previousElement == null || this.children.isEmpty()) {
			return nextValue();
		}
		previousElements.add(previousElement);
		ProbFunTree<T> pf = this.children.get(this.previousElement);
		if(pf == null) {
			return null;
		}
		T t = pf.previousElement;
		if(t == null && pf != null) {
			return pf.nextValue();
		} 
		while(t != null) {
			previousElements.add(t);
			if(!pf.children.isEmpty()) {
				pf = pf.children.get(t);
				if(pf != null) {
					t = pf.previousElement;
				} else {
					return null;
				}
			} else {
				return null;
			}
		}
		Iterator<T> it = previousElements.iterator();
		if(it.hasNext()) {
			this.previousElement = it.next();
			pf = this.children.get(this.previousElement);
			while(it.hasNext()) {
				pf.previousElement = it.next();
				pf = pf.children.get(pf.previousElement);
			}
			if(pf == null) {
				return null;
			}
			return pf.fun();
		} 
		return null;
	}

	/**        For generating the next value.
	 * @return the next generated value.
	 */
	private T nextValue() {
		double randomChoice = ThreadLocalRandom.current().nextDouble();
		double sumOfProbabilities = 0.0;
		Iterator<Entry<T, Double>> entries = this.probMap.entrySet().iterator();
		T element = null;
		Entry<T, Double> e;
		while((randomChoice > sumOfProbabilities)) {
			e = entries.next();
			element = e.getKey();
			sumOfProbabilities += e.getValue();
		} 
		this.previousElement = element;	
		return element;
	}

	/**        Returns the number of elements in this ProbFunTree.
	 * @return the number of elements in this ProbFunTree.
	 */
	public int parentSize() {return this.probMap.size();}

	/**        Returns the number of elements in this whole ProbFunTree, 
	 *         which includes the number of elements in the parent plus the number of elements in every descendant.
	 * @return the number of elements in this whole ProbFunTree, 
	 *         which includes the number of elements in the parent plus the number of elements in every descendant.
	 */
	public int size() {
		int size = parentSize();
		for(ProbFunTree<T> p : this.children.values()) {
			size += p.size();
		}
		return size;
	}

	/**       Private copy constructor for clone
	 * @param probFunTree as the ProbFunTree to copy
	 */
	private ProbFunTree(ProbFunTree<T> probFunTree) {
		for(Entry<T, Double> s : probFunTree.probMap.entrySet()) {
			this.probMap.put(s.getKey(), s.getValue());
			modCount++;
		}
		for(Entry<T, ProbFunTree<T>> e : probFunTree.children.entrySet()) {
			this.children.put(e.getKey(), e.getValue().clone());
		}
		if(!this.children.isEmpty()){
			for(ProbFunTree<T> e : this.children.values()) {
				e.parent = this;
			}
		}
	}

	@Override
	public ProbFunTree<T> clone() {
		return new ProbFunTree<T>(this);
	}

	@Override
	public String toString() {
		if(this.id == 0) {
			this.id = System.identityHashCode(this);
		}
		StringBuilder sb = new StringBuilder();
		sb.append("PF:");
		sb.append(this.id);
		sb.append(": ");
		for(Entry<T, Double> e : this.probMap.entrySet()) {
			sb.append("[");
			sb.append(e.getKey());
			sb.append(" = ");
			sb.append(e.getValue()*100.0);
			sb.append("%],");
		}
		sb.delete(sb.length()-1, sb.length());
		if(!this.children.isEmpty()) {
			sb.append(";\n");
			sb.append("    Node: ");
			for(Entry<T, ProbFunTree<T>> e : this.children.entrySet()) {
				sb.append("[");
				sb.append(e.getKey());
				sb.append(" = ");
				sb.append(e.getValue());
				sb.delete(sb.length()-1, sb.length());
				sb.append("]");
			}
		} else{
		}
		sb.append("\n");
		return sb.toString();
	}

	@Override
	public int hashCode() {
		if(this.id == 0) {
			this.id = System.identityHashCode(this);
		}
		return this.id;
	}

	@SuppressWarnings("unchecked")
	@Override
	public int compareTo(T o) {
		if(this.id == 0) {
			this.id = System.identityHashCode(this);
		}
		return this.id-((ProbFunTree<T>)o).id;
	}

}