
package CardSharps.Poker;

// This package provides classes necessary for implementing a game system for playing poker


public class NumberCard extends Card implements Comparable<Card>{
	
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

	@Override
	public int compareTo(Card other) {
		if (this.getRank() < other.getRank()) {
			return -1;
		} else if (this.getRank() > other.getRank()) {
			return 1;
		} else {
			return 0;
		}
	}
	
}