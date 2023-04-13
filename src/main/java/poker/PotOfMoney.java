
package poker;

// This package provides classes necessary for implementing a game system for playing poker


public class PotOfMoney
{	
	private int total = 0; // the total amount of money in the table, waiting to be won
	
	private int stake = 0; // the current highest stake expected 
						   // of each player to stay in the game
	
	//--------------------------------------------------------------------//
	//--------------------------------------------------------------------//
	// Constructor
	//--------------------------------------------------------------------//
	//--------------------------------------------------------------------//
	
	public PotOfMoney() {}

		
	//--------------------------------------------------------------------//
	//--------------------------------------------------------------------//
	// Display Behaviour 
	//--------------------------------------------------------------------//
	//--------------------------------------------------------------------//
	
	public String toString() {
		return "There is a pot of " + total + " chip(s) on the table";
	}
	
	
	//--------------------------------------------------------------------//
	//--------------------------------------------------------------------//
	// Accessors
	//--------------------------------------------------------------------//
	//--------------------------------------------------------------------//
	
	public int getTotal() {
		return total;
	}
	
	public int getCurrentStake() {
		return stake;
	}
	
	//--------------------------------------------------------------------//
	//--------------------------------------------------------------------//
	// Modifiers
	//--------------------------------------------------------------------//
	//--------------------------------------------------------------------//
	
	public void raiseStake(int addition) {
		stake += addition;
		
		addToPot(addition);
	}
	
	
	public void addToPot(int addition) {
		total += addition;
	}
	
	
	public void clearPot() {
		total = 0;
	}
	
	
	public int takePot() {
	    // when the winner of a hand takes the pot as his/her winnings  
		
		int winnings = getTotal();
		
		clearPot();
		
		return winnings;
	}
	
}