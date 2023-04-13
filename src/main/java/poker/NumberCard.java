
package poker;

// This package provides classes necessary for implementing a game system for playing poker


public class NumberCard extends Card {
	
	//--------------------------------------------------------------------//
	//--------------------------------------------------------------------//
	// Constructors
	//--------------------------------------------------------------------//
	//--------------------------------------------------------------------//
	
	public NumberCard(String name, String suit, int rank) {
		this(name, suit, rank, rank);
	}
	
	public NumberCard(String name, String suit, int rank, int value) {
		super(name, suit, rank, value);
	}
	
}