
package poker;

// This package provides classes necessary for implementing a game system for playing poker

import java.util.Random;


public class DeckOfCards {
	public static final String[] suits 	= {"hearts", "diamonds", "clubs", "spades"};
	
	public static final int NUMCARDS 	= 52;  // number of cards in a deck
	

	private Card[] deck 				= new Card[NUMCARDS];  // the actual sequence of cards
	
	private int next 					= 0; // the next card to be dealt
	
	private Random dice     			= new Random(System.currentTimeMillis());
	
	
	//--------------------------------------------------------------------//
	//--------------------------------------------------------------------//
	// Constructors
	//--------------------------------------------------------------------//
	//--------------------------------------------------------------------//
	
	public DeckOfCards() {
		for (int i = 0; i < suits.length; i++) {
			deck[next++] = new NumberCard("Ace", suits[i], 1, 14);
			deck[next++] = new NumberCard("Deuce", suits[i], 2);
			deck[next++] = new NumberCard("Three", suits[i], 3); 
			deck[next++] = new NumberCard("Four", suits[i], 4); 
			deck[next++] = new NumberCard("Five", suits[i], 5);  
			deck[next++] = new NumberCard("Six", suits[i], 6);   
			deck[next++] = new NumberCard("Seven", suits[i], 7); 
			deck[next++] = new NumberCard("Eight", suits[i], 8);  
			deck[next++] = new NumberCard("Nine", suits[i], 9); 
			deck[next++] = new NumberCard("Ten", suits[i], 10); 
			deck[next++] = new FaceCard("Jack", suits[i], 11);
			deck[next++] = new FaceCard("Queen", suits[i], 12);
			deck[next++] = new FaceCard("King", suits[i], 13);
		}
		
		reset();
	}

		
	//--------------------------------------------------------------------//
	//--------------------------------------------------------------------//
	// Reset internal state for start of new hand of poker  
	//--------------------------------------------------------------------//
	//--------------------------------------------------------------------//

	public void reset()	{
		next = 0;
		
		shuffle();
	}
	
	//--------------------------------------------------------------------//
	//--------------------------------------------------------------------//
	// Card Shuffler
	//--------------------------------------------------------------------//
	//--------------------------------------------------------------------//
	
	public void shuffle() {
		Card palm = null;
		
		int alpha = 0, beta = 0;
		
		for (int i = 0; i < NUMCARDS*NUMCARDS; i++) {
			alpha       = Math.abs(dice.nextInt())%NUMCARDS;
			beta        = Math.abs(dice.nextInt())%NUMCARDS;
			
			palm        = deck[alpha];
			deck[alpha] = deck[beta];
			deck[beta]  = palm;
		}
	}

	
	//--------------------------------------------------------------------//
	//--------------------------------------------------------------------//
	// Accessors
	//--------------------------------------------------------------------//
	//--------------------------------------------------------------------//
	
	public Card dealNext() {
		if (next >= NUMCARDS)
			return new FaceCard("Joker", "no suit", 0);  // deck is empty
		else
			return deck[next++];
	}
	
	
	public PokerHand dealHand() {
		PokerHand hand = new PokerHand(this);
		
		return hand.categorize();
	}

	//--------------------------------------------------------------------//
	//--------------------------------------------------------------------//
	// Test Harness
	//--------------------------------------------------------------------//
	//--------------------------------------------------------------------//
	
	public static void main(String[] args) {
		DeckOfCards deck = new DeckOfCards();
		
		PokerHand hand1  = deck.dealHand();
		PokerHand hand2  = deck.dealHand();
		PokerHand hand3  = deck.dealHand();
		
		System.out.println("\nPlayer 1: " + hand1 + "Value = " + hand1.getValue());
		System.out.println("\nPlayer 2: " + hand2 + "Value = " + hand2.getValue());
		System.out.println("\nPlayer 3: " + hand3 + "Value = " + hand3.getValue());
		

		hand1 = hand1.discard();
		System.out.println("\nPlayer 1 discards " + hand1.getNumDiscarded() + " card(s)\n");
		
		hand2 = hand2.discard();
		System.out.println("\nPlayer 2 discards " + hand2.getNumDiscarded() + " card(s)\n");

		hand3 = hand3.discard();
		System.out.println("\nPlayer 3 discards " + hand3.getNumDiscarded() + " card(s)\n");

		
		System.out.println("\n\nAfter Discarding:\n");
		
		System.out.println("\nPlayer 1: " + hand1 + "New Value = " + hand1.getValue());
		System.out.println("\nPlayer 2: " + hand2 + "New Value = " + hand2.getValue());
		System.out.println("\nPlayer 3: " + hand3 + "New Value = " + hand3.getValue());
		
		try {
			System.out.print("\nPress ENTER to terminate ...");
			System .in.read();
		}
		catch (Exception e) {};
	}

}