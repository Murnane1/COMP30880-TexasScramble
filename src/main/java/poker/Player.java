
package poker;

// This package provides classes necessary for implementing a game system for playing poker

// A Player is an object that can make decisions in a game of poker

// There are two extension classes: ComputerPlayer, in which decisions are made using algorithms
//								and HumanPlayer, in which decisions are made using menus


abstract class Player {	
	private int bank       		= 0;		 // the total amount of money the player has left, not counting his/her
									    	 // stake in the pot
	
	private int stake      		= 0;		 // the amount of money the player has thrown into the current pot 
	
	private String name    		= "Player";  // the unique identifying name given to the player
	
	private PokerHand hand 		= null;      // the hand dealt to this player
	
	private boolean folded 		= false;     // set to true when the player folds (gives up)
	
	//--------------------------------------------------------------------//
	//--------------------------------------------------------------------//
	// Constructor
	//--------------------------------------------------------------------//
	//--------------------------------------------------------------------//
	
	public Player(String name, int money)	{
		this.name = name;
		
		bank      = money;
		
		reset();
	}
		
	//--------------------------------------------------------------------//
	//--------------------------------------------------------------------//
	// Reset internal state for start of new hand of poker  
	//--------------------------------------------------------------------//
	//--------------------------------------------------------------------//

	public void reset() {
		folded = false;
		
		stake  = 0;
	}
	
	
	//--------------------------------------------------------------------//
	//--------------------------------------------------------------------//
	// Display Behaviour 
	//--------------------------------------------------------------------//
	//--------------------------------------------------------------------//
	
	public String toString() {
		if (hasFolded())
			return "> " + getName() + " has folded, and has " + addCount(getBank(), "chip", "chips") + " in the bank.";
		else
			return "> " + getName() + " has  " + addCount(getBank(), "chip", "chips") + " in the bank";
	}
	
	
	//--------------------------------------------------------------------//
	//--------------------------------------------------------------------//
	// Accessors
	//--------------------------------------------------------------------//
	//--------------------------------------------------------------------//
	
	public PokerHand getHand() {
		return hand;
	}
	
	public int getBank() {
		return bank;
	}
	
	
	public int getStake() {
		return stake;
	}

	
	public String getName() {
		return name;
	}
	
	
	public boolean isBankrupt() {
		// no more money left
	
		return bank == 0;
	}
	
	
	public boolean hasFolded() {
	    // has given up on the current hand
	
		return folded;	
	}
	
	//--------------------------------------------------------------------//
	//--------------------------------------------------------------------//
	// Modifiers
	//--------------------------------------------------------------------//
	//--------------------------------------------------------------------//
	
	public void reorganizeHand() {
		hand = hand.categorize();
	}
	
	
	
	public void dealTo(DeckOfCards deck) {
		hand = deck.dealHand();
	}
	
	
	public void throwaway(int cardPos, boolean recategorize) {
		hand.throwaway(cardPos);
		
		if (recategorize)
			reorganizeHand();
	}
	
	
	public void discard() {
		hand = hand.discard();
		
		System.out.println(getName() + " discards " + addCount(hand.getNumDiscarded(), "card", "cards") + "\n");
	}
	
	
	public void takePot(PotOfMoney pot) {
	    // when the winner of a hand takes the pot as his/her winnings
	
		System.out.println("\n> " + getName() + " says: I WIN " + addCount(pot.getTotal(), "chip", "chips") + "!\n");
		System.out.println(hand.toString());
		
		bank += pot.takePot();
		
		System.out.println(this);
	}
	

	//--------------------------------------------------------------------//
	//--------------------------------------------------------------------//
	// Allowable player actions in Poker
	//--------------------------------------------------------------------//
	//--------------------------------------------------------------------//
		
	public void fold() {			    
	    // make this player give up
	
		if (!folded)
			System.out.println("\n> " + getName() + " says: I fold!\n");
		
		folded = true;
	}
	
	
	public void openBetting(PotOfMoney pot) {
		if (bank == 0) return;
		
		stake++;
		bank--; 
		
		pot.raiseStake(1);
		
		System.out.println("\n> " + getName() + " says: I open with one chip!\n");
	}

	
	public void seeBet(PotOfMoney pot) {
		int needed  = pot.getCurrentStake() - getStake();
		
		if (needed == 0 || needed > getBank())
			return;
		
		stake += needed;
		bank  -= needed; 
		
		pot.addToPot(needed);
		
		System.out.println("\n> " + getName() + " says: I see that " + addCount(needed, "chip", "chips") + "!\n");
	}

	
	public void raiseBet(PotOfMoney pot) {
		if (getBank() == 0) return;
		
		stake++;
		bank--; 
		
		pot.raiseStake(1);
		
		System.out.println("\n> " + getName() + " says: and I raise you 1 chip!\n");
	}

	
	//--------------------------------------------------------------------//
	//--------------------------------------------------------------------//
	// Key decisions a player must make
	//--------------------------------------------------------------------//
	//--------------------------------------------------------------------//
	
	abstract boolean shouldOpen(PotOfMoney pot);

	abstract boolean shouldSee(PotOfMoney pot);

	abstract boolean shouldRaise(PotOfMoney pot);
	

	//--------------------------------------------------------------------//
	//--------------------------------------------------------------------//
	// Game actions are scheduled here
	//--------------------------------------------------------------------//
	//--------------------------------------------------------------------//
	
	public void nextAction(PotOfMoney pot) {
		if (hasFolded()) return;  // no longer in the game
		
		if (isBankrupt() || pot.getCurrentStake() - getStake() > getBank()) {
			// not enough money to cover the bet
		
			System.out.println("\n> " + getName() + " says: I'm out!\n"); 	
			
			fold();
			
			return;
		}
		
		if (pot.getCurrentStake() == 0) {
			// first mover of the game
		
			if (shouldOpen(pot))  // will this player open the betting?
				openBetting(pot);	
			else
				fold();
		}
		else {
			if (pot.getCurrentStake() > getStake()) {
				// existing bet must be covered	
			
				if (shouldSee(pot)) {
					seeBet(pot);
					
					if (shouldRaise(pot))
						raiseBet(pot);
				}
				else
					fold();
			}
		}
	}		

	//--------------------------------------------------------------------//
	//--------------------------------------------------------------------//
	// Some small but useful helper routines
	//--------------------------------------------------------------------//
	//--------------------------------------------------------------------//

	public String addCount(int count, String singular, String plural) {
		if (count == 1 || count == -1)
			return count + " " + singular;
		else
			return count + " " + plural;
	}
}