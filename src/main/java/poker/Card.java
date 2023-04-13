
package poker;

// This package provides classes necessary for implementing a game system for playing poker


public class Card
{
	private String suit		= "";	// "hearts", "clubs", "diamonds", "spades"
	private String name     = "";	// "Ace", "Deuce", "Three", ..., "Jack", "Queen", "King"
	
	private int    rank     = 0;	// Ace=1, Deuce=2, ..., King=13
	private int    value    = 0;    // Deuce=2, ..., King=13 but Ace=14
	
	
	//--------------------------------------------------------------------//
	//--------------------------------------------------------------------//
	// Constructors
	//--------------------------------------------------------------------//
	//--------------------------------------------------------------------//
	
	public Card(String name, String suit, int rank) {
		this(name, suit, rank, rank);
	}
	
	public Card(String name, String suit, int rank, int value) {
		this.name   = name;
		this.suit   = suit;
		this.rank   = rank;
		this.value  = value;
	}
	
	
	//--------------------------------------------------------------------//
	//--------------------------------------------------------------------//
	// Accessors
	//--------------------------------------------------------------------//
	//--------------------------------------------------------------------//
	
	public String getName() {
		return name;
	}
						  
	
	
	public int getRank() {
		return rank;
	}
	
	
	// the value of the card in poker, by default the number of the card
    // one exception is the Ace, whose number is 1 but whose value is the highest in poker
	
	public int getValue()  {  		     
		return value;
	}
	
		
	// is the card in the suit of hearts, diamonds, clubs, or spades?
	
	public String getSuit() {
		return suit;
	}
	
	
	public String toString() {
		return "(" + getName() + " of " + getSuit() +")";
	}
	
	
	//--------------------------------------------------------------------//
	//--------------------------------------------------------------------//
	// Useful predicates
	//--------------------------------------------------------------------//
	//--------------------------------------------------------------------//
	
	public boolean isFaceCard() {
		return this instanceof FaceCard;
	}
	
	
	public boolean isNumberCard() {
		return this instanceof NumberCard;
	}

	public boolean isAce() {
		return getName() == "Ace";
	}
	
	public boolean isTen() {
		return getName() == "Ten";
	}
	
	public boolean isJack() {
		return getName() == "Jack";
	}
	
	public boolean isQueen() {
		return getName() == "Queen";
	}
	
	public boolean isKing() {
		return getName() == "King";
	}
	
}
	
	



