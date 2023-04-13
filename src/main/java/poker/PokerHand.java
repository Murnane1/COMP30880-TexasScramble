
package poker;

// This package provides classes necessary for implementing a game system for playing poker

import java.lang.reflect.*;

public class PokerHand
{
	public static final int DEFAULT_RISK  		= 20;
	public static final int NUMCARDS      		= 5;  // number of cards in a hand of poker
	
	public static final int ROYALFLUSH_VALUE	= 2000000000;
	public static final int STRAIGHTFLUSH_VALUE = 1000000000;
	public static final int STRAIGHT_VALUE      = 100000000;
	public static final int FOURS_VALUE         = 10000000;
	public static final int FLUSH_VALUE		  	= 1000000;
	public static final int FULLHOUSE_VALUE     = 100000;
	public static final int THREES_VALUE		= 10000;
	public static final int TWOPAIR_VALUE		= 1000;
	public static final int PAIR_VALUE		  	= 100;
	public static final int HIGHCARD_VALUE		= 0;
	
	public static final int ROYALFLUSH_RISK  	= 0;
	public static final int STRAIGHTFLUSH_RISK  = 5;
	public static final int STRAIGHT_RISK       = 10;
	public static final int FOURS_RISK          = 15;
	public static final int FLUSH_RISK		  	= 20;
	public static final int FULLHOUSE_RISK      = 25;
	public static final int THREES_RISK			= 30;
	public static final int TWOPAIR_RISK		= 35;
	public static final int PAIR_RISK		  	= 40;
	public static final int HIGHCARD_RISK		= 90;

	
	private Card[] hand;  								// the actual sequence of cards
	
	private DeckOfCards deck; 							// the deck from which the hand is made
	
	private int discarded 						= 0; 	// the number of cards already discarded and redealt in this hand
	
	//--------------------------------------------------------------------//
	//--------------------------------------------------------------------//
	// Constructors
	//--------------------------------------------------------------------//
	//--------------------------------------------------------------------//
	
	public PokerHand(Card[] hand, DeckOfCards deck) {
		this.hand = hand;
		
		this.deck = deck;
	}
	
	
	public PokerHand(DeckOfCards deck) {
		this.deck = deck;
		
		hand      = new Card[NUMCARDS];
		
		for (int i = 0; i < NUMCARDS; i++)
			setCard(i, deck.dealNext());
		
		sortHand();
	}

	//--------------------------------------------------------------------//
	//--------------------------------------------------------------------//
	// What is the riskworthiness of this hand?
	//
	// Risk-worthiness is a measure of how good the hand is, and how much
	// it is worth taking a risk on (e.g., by playing on instead of folding)
	//--------------------------------------------------------------------//
	//--------------------------------------------------------------------//
	
	public int getRiskWorthiness() {
		return DEFAULT_RISK;  // override this value in specific extensions like FullHouse
	}

	//--------------------------------------------------------------------//
	//--------------------------------------------------------------------//
	// Display a hand
	//--------------------------------------------------------------------//
	//--------------------------------------------------------------------//
	
	public String toString() {
		String desc = "";
		
		for (int i = 0; i < NUMCARDS; i++)
			desc = desc + "\n      " + i + ":  " + getCard(i).toString();
		
		return desc + "\n";
	}

	//--------------------------------------------------------------------//
	//--------------------------------------------------------------------//
	// Modifiers
	//--------------------------------------------------------------------//
	//--------------------------------------------------------------------//
	
	public void setCard(int num, Card card) {
		if (num >= 0 && num < NUMCARDS)
			hand[num] = card;
	}
	

	
	//--------------------------------------------------------------------//
	//--------------------------------------------------------------------//
	// Accessors
	//--------------------------------------------------------------------//
	//--------------------------------------------------------------------//
	
	public Card getCard(int num) {
		if (num >= 0 && num < NUMCARDS)
			return hand[num];
		else
			return null;
	}
	
		
	//--------------------------------------------------------------------//
	// What is the value of this hand?
	//--------------------------------------------------------------------//
	
	public int getValue() {
		return getCard(0).getValue(); // simply return the value of the higest card
	}

	
	//--------------------------------------------------------------------//
	// Discard and redeal some cards
	//--------------------------------------------------------------------//
	
	protected void throwaway(int pos) {
		if (pos < 0 || pos >= NUMCARDS) return;  // already discarded or out of bounds
		
		Card next = deck.dealNext();
		
		if (next != null) {
			setCard(pos, next);
			discarded++;
		}
	}

		
	public PokerHand discard() {
		// discard some cards to try and make a better hand
		
		return this;  // by default do not discard anything
	}
		
	
	public PokerHand discard(int pos) {
		if (discarded > 0) return this;  // already discarded
		
		throwaway(pos);
		
		return categorize();
	}
	
	
	public PokerHand discard(int pos1, int pos2) {
		if (discarded > 0) return this;  // already discarded
		
		throwaway(pos1);
		
		throwaway(pos2);
		
		return categorize();
	}

	
	
	public PokerHand discard(int pos1, int pos2, int pos3) {
		if (discarded > 0) return this;  // already discarded
		
		throwaway(pos1);
		
		throwaway(pos2);

		throwaway(pos3);

		return categorize();
	}

	
	
	public int getNumDiscarded() {
		return discarded;
	}
	
	
	public void setNumDiscarded(int num) {
		discarded = num;
	}
	
	//--------------------------------------------------------------------//
	//--------------------------------------------------------------------//
	// Card Sorter
	//--------------------------------------------------------------------//
	//--------------------------------------------------------------------//
	
	// sort the cards in the hand from left to right, with highest values on the right
	
	public void sortHand() {
		int maxValue = 0, maxPosition = 0;
		
		Card palm = null;
		
		// consider every position in the hand
		
		for (int i = 0; i < NUMCARDS; i++) {
			maxPosition = i;
			maxValue    = getCard(i).getValue();
					
			// consider every other position to the left of this position
			
			for (int j = i+1; j < NUMCARDS; j++)  { // is there a higher card to the left?
				if (getCard(j).getValue() > maxValue) {
					maxPosition = j;					// yes, so remember where
					maxValue    = getCard(j).getValue();
				}
			}
			
			// swap card i with the highest value card to the left of it
			
			if (maxPosition > i)  {
				palm = getCard(i);
				setCard(i,	getCard(maxPosition));
				setCard(maxPosition, palm);
			}
		}
		
		if (getCard(4).getName() == "Ace") reorderStraight();
	}

	
	private void reorderStraight() {
		// Check to see if ace should be sorted as a low value (1) rather than a high value (14)
		
		if (hand[4].getName() == "Ace" &&
				hand[0].getRank() == 2 && hand[1].getRank() == 3 && hand[2].getRank() == 4 && hand[3].getRank() == 5) {
			
			Card ace = hand[4];
			
			hand[4] = hand[3]; 
			hand[3] = hand[2];
			hand[2] = hand[1];
			hand[1] = hand[0];
			hand[0] = ace;
		}
	}
	
	//--------------------------------------------------------------------//
	//--------------------------------------------------------------------//
	// Hand Classifier
	//--------------------------------------------------------------------//
	//--------------------------------------------------------------------//
	
	public boolean isFourOfAKind() 	{
		return (getCard(0).getRank() == getCard(3).getRank() 	||
				getCard(1).getRank() == getCard(4).getRank());
	}

	
	public boolean isFullHouse() {
		return ((getCard(0).getRank() == getCard(2).getRank()  &&   // triple + pair
			     getCard(3).getRank() == getCard(4).getRank()) ||
				(getCard(0).getRank() == getCard(1).getRank()  &&   // pair + triple
				 getCard(2).getRank() == getCard(4).getRank()));    
	}

	
	// A straight involving an ace can have two forms: ace-first (12345) or ace-last (10JQKA)
	
	public boolean isStraight() {
		return (getCard(0).getRank() == getCard(1).getRank() + 1 && // ordered by rank
				getCard(1).getRank() == getCard(2).getRank() + 1 &&
				getCard(2).getRank() == getCard(3).getRank() + 1 &&	
				getCard(3).getRank() == getCard(4).getRank() + 1)
				||
			   (getCard(0).getValue()  == getCard(1).getValue() - 1 &&  // ordered by game value
				getCard(1).getValue()  == getCard(2).getValue() - 1 &&
				getCard(2).getValue()  == getCard(3).getValue() - 1 &&	
				getCard(3).getValue()  == getCard(4).getValue() - 1);
	}
	
	
	public boolean isRoyalStraight() {
		return (getCard(4).isAce() &&
				getCard(0).isKing() &&
				getCard(1).isQueen() &&
				getCard(2).isJack() &&
				getCard(3).isTen());
	}

	
	public boolean isFlush() {
		return (getCard(0).getSuit() == getCard(1).getSuit() &&
				getCard(1).getSuit() == getCard(2).getSuit() &&
				getCard(2).getSuit() == getCard(3).getSuit() &&
				getCard(3).getSuit() == getCard(4).getSuit());
	}
		
	
	
	public boolean isStraightFlush() {
		return isFlush() && isStraight();
	}
	
	
	public boolean isRoyalFlush() {
		return isRoyalStraight() && isFlush();
	}
	
	
	public boolean isThreeOfAKind() {
		return (getCard(0).getRank() == getCard(2).getRank() ||
				getCard(1).getRank() == getCard(3).getRank() ||
				getCard(2).getRank() == getCard(4).getRank());
	}
	
	
		
	public boolean isTwoPair() {
		return ((getCard(0).getRank() == getCard(1).getRank() && 
				 getCard(2).getRank() == getCard(3).getRank()) ||
				(getCard(0).getRank() == getCard(1).getRank() && 
				 getCard(3).getRank() == getCard(4).getRank()) ||
				(getCard(1).getRank() == getCard(2).getRank() && 
				 getCard(3).getRank() == getCard(4).getRank()));
	}
	
	
	public boolean isPair() {
		return (getCard(0).getRank() == getCard(1).getRank() || 
				getCard(1).getRank() == getCard(2).getRank() ||
				getCard(2).getRank() == getCard(3).getRank() ||
				getCard(3).getRank() == getCard(4).getRank());
	}
	
	
	public boolean isHigh() {
		return true;
	}

	//--------------------------------------------------------------------//
	//--------------------------------------------------------------------//
	// Return a specific instance of the most valuable poker hand we
	// can find in these cards
	// 
	// Use Reflection API
	//--------------------------------------------------------------------//
	//--------------------------------------------------------------------//
		
	public PokerHand categorize() {
		sortHand();
		
		Method[] preds = PokerHand.class.getDeclaredMethods(); // all methods in PokerHand	
		
		PokerHand best = this;
		
		for (int i = 0; i < preds.length; i++) {
			try {
				
				// Test all predicative methods of form  boolean isX()
				
				if (preds[i].getName().startsWith("is") && preds[i].getParameterTypes().length == 0 &&
					preds[i].getReturnType() == boolean.class)
				{
					Boolean test = (Boolean)preds[i].invoke(this, (Object[])null);
					
					if (test.booleanValue())  // the predicate says 'yes' to this hand
					{
						// Attempt to construct the hand described by the predicate
						
						PokerHand reconsider	=  categorizeAs(preds[i].getName().substring(2));
						
						// Is it a better hand than the way we organized it previously?
						 
						if (reconsider.getRiskWorthiness() > best.getRiskWorthiness())
							best = reconsider;
					}
				}
			}
			catch (Exception e) {};
		}
		
		return best;
	}

	
	//--------------------------------------------------------------------//
	//--------------------------------------------------------------------//
	// Given a hand type (such as ThreeOfAKind), convert this hand into
	// a hand of the given type
	//--------------------------------------------------------------------//
	//--------------------------------------------------------------------//


	public PokerHand categorizeAs(String assessment) {
		if (assessment == null)
			return this;			// no recategorization
		
		try {
			Class promotion			= Class.forName("poker." + assessment);
			
			Constructor[] builders	= promotion.getDeclaredConstructors();
			
			Object[] promotionArgs	= new Object[2];
			
			promotionArgs[0]		= hand;
			promotionArgs[1]		= deck;
			
			for (int i = 0; i < builders.length; i++)
				if (builders[i].getParameterTypes().length == 2) {
					PokerHand category = (PokerHand)builders[i].newInstance(promotionArgs);
					
					category.setNumDiscarded(getNumDiscarded()); // track how many cards discarded
					
					return category;
				}	
		}
		catch (Exception e) {};
							  
		return this;
	}

	
	//--------------------------------------------------------------------//
	//--------------------------------------------------------------------//
	// Return a specific instance of the most valuable poker hand we
	// can find in these cards
	//
	// This is how categorization would work without Reflection
	// We need to enumerate all the options
	//--------------------------------------------------------------------//
	//--------------------------------------------------------------------//
	
	public PokerHand recategorize() {
		sortHand();
		
		PokerHand category = this;
		
		if (isRoyalFlush())
			category = new RoyalFlush(hand, deck);
		else
		if (isStraightFlush())
			category = new StraightFlush(hand, deck);
		else
		if (isStraight())
			category = new Straight(hand, deck);
		else
		if (isFourOfAKind())
			category = new FourOfAKind(hand, deck);
		else
		if (isFlush())
			category = new Flush(hand, deck);
		else
		if (isFullHouse())
			category = new FullHouse(hand, deck);
		else
		if (isThreeOfAKind())
			category = new ThreeOfAKind(hand, deck);
		else
		if (isTwoPair())
			category = new TwoPair(hand, deck);
		else
		if (isPair())
			category = new Pair(hand, deck);
		else
			category = new High(hand, deck);
		
		category.setNumDiscarded(getNumDiscarded()); // track how many cards discarded
		
		return category;
	}
		
}

