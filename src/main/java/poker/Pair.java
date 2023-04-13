
package poker;

// This package provides classes necessary for implementing a game system for playing poker


public class Pair extends PokerHand {
	
	//--------------------------------------------------------------------//
	//--------------------------------------------------------------------//
	// Constructors
	//--------------------------------------------------------------------//
	//--------------------------------------------------------------------//
	
	public Pair(Card[] hand, DeckOfCards deck) {
		super(hand, deck);
	}

	
	//--------------------------------------------------------------------//
	//--------------------------------------------------------------------//
	// What is the riskworthiness of this hand?
	//--------------------------------------------------------------------//
	//--------------------------------------------------------------------//
	
	public int getRiskWorthiness() {
		return 40; 
	}

	//--------------------------------------------------------------------//
	//--------------------------------------------------------------------//
	// What is the value of this hand?
	//--------------------------------------------------------------------//
	//--------------------------------------------------------------------//
	
	public int getValue() {
		if (getCard(0).getRank() == getCard(1).getRank())
			return PokerHand.PAIR_VALUE + getCard(0).getValue()*10 + getCard(2).getValue();
		else
		if (getCard(1).getRank() == getCard(2).getRank())
			return PokerHand.PAIR_VALUE + getCard(1).getValue()*10 + getCard(0).getValue();
		else
		if (getCard(2).getRank() == getCard(3).getRank())
			return PokerHand.PAIR_VALUE + getCard(2).getValue()*10 + getCard(0).getValue();
		else
			return PokerHand.PAIR_VALUE + getCard(3).getValue()*10 + getCard(0).getValue();
	}
	
	//--------------------------------------------------------------------//
	//--------------------------------------------------------------------//
	// Discard and redeal some cards
	//--------------------------------------------------------------------//
	//--------------------------------------------------------------------//
	
	public PokerHand discard() {
		if (getCard(0).getRank() == getCard(1).getRank())
			return discard(2,3,4);
		else
		if (getCard(1).getRank() == getCard(2).getRank())
			return discard(0,3,4);
		else
		if (getCard(2).getRank() == getCard(3).getRank())
			return discard(0,1,4);
		else
			return discard(0,1,2);
	}


	//--------------------------------------------------------------------//
	//--------------------------------------------------------------------//
	// Display
	//--------------------------------------------------------------------//
	//--------------------------------------------------------------------//
	
	public String toString() {
		if (getCard(0).getRank() == getCard(1).getRank())
			return "Pair of " + getCard(0).getName() + "s" + super.toString();
		else
		if (getCard(1).getRank() == getCard(2).getRank())
			return "Pair of " + getCard(1).getName() + "s" + super.toString();
		else
		if (getCard(2).getRank() == getCard(3).getRank())
			return "Pair of " + getCard(2).getName() + "s" + super.toString();
		else
			return "Pair of " + getCard(4).getName() + "s" + super.toString();
	}


}
