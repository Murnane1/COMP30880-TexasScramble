
package poker;

// This package provides classes necessary for implementing a game system for playing poker


public class ThreeOfAKind extends PokerHand {
	
	//--------------------------------------------------------------------//
	//--------------------------------------------------------------------//
	// Constructors
	//--------------------------------------------------------------------//
	//--------------------------------------------------------------------//
	
	public ThreeOfAKind(Card[] hand, DeckOfCards deck) {
		super(hand, deck);
	}

	
	//--------------------------------------------------------------------//
	//--------------------------------------------------------------------//
	// What is the riskworthiness of this hand?
	//--------------------------------------------------------------------//
	//--------------------------------------------------------------------//
	
	public int getRiskWorthiness() {
		return 100 - PokerHand.THREES_RISK; 
	}

	//--------------------------------------------------------------------//
	//--------------------------------------------------------------------//
	// What is the value of this hand?
	//--------------------------------------------------------------------//
	//--------------------------------------------------------------------//
	
	public int getValue() {
		if (getCard(0).getRank() == getCard(2).getRank())
			return PokerHand.THREES_VALUE + getCard(0).getValue()*10 + getCard(3).getValue();
		else
		if (getCard(1).getRank() == getCard(3).getRank())
			return PokerHand.THREES_VALUE + getCard(1).getValue()*10 + getCard(0).getValue();
		else
			return PokerHand.THREES_VALUE + getCard(2).getValue()*10 + getCard(0).getValue();			
	}
	
	
	//--------------------------------------------------------------------//
	//--------------------------------------------------------------------//
	// Discard and redeal some cards
	//--------------------------------------------------------------------//
	//--------------------------------------------------------------------//
	
	public PokerHand discard() {
		if (getCard(0).getRank() == getCard(2).getRank())
			return discard(3, 4);
		else
		if (getCard(1).getRank() == getCard(3).getRank())
			return discard(0, 4);
		else
			return discard(0, 1);			
	}

	
	
	//--------------------------------------------------------------------//
	//--------------------------------------------------------------------//
	// Display
	//--------------------------------------------------------------------//
	//--------------------------------------------------------------------//
	
	public String toString() {
		if (getCard(0).getRank() == getCard(2).getRank())
			return "Three " + getCard(0).getName() + "s" + super.toString();
		else
		if (getCard(1).getRank() == getCard(3).getRank())
			return "Three " + getCard(1).getName() + "s" + super.toString();
		else
			return "Three " + getCard(2).getName() + "s" + super.toString();	
	}

	
}

