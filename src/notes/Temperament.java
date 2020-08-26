package notes;

import java.util.ArrayList;

/**
@author Alexander Johnston 
        Copyright 2019 
        A class for musical temperaments
 */
abstract public class Temperament {

	// Holds the notes
	public ArrayList<Note> notes = new ArrayList<Note>();

	// Middle A index
	public int middleAIndex;

	// Last index below 20hz
	public int subHertzIndex;
	
}