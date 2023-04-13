
package poker;

// This package provides classes necessary for implementing a game system for playing poker

// A Player is an object that can make decisions in a game of poker

// There are two extension classes: ComputerPlayer, in which decisions are made using algorithms
//								and HumanPlayer, in which decisions are made using menus


import java.util.Random;


public class ComputerPlayer extends Player {	
	public static final int VARIABILITY		= 50;
	
	private int riskTolerance				= 0;  // willingness of a player to take risks and bluff
		
	private Random dice						= new Random(System.currentTimeMillis());

	//--------------------------------------------------------------------//
	//--------------------------------------------------------------------//
	// Constructor
	//--------------------------------------------------------------------//
	//--------------------------------------------------------------------//
	
	public ComputerPlayer(String name, int money){
		super(name, money);
		
		riskTolerance = Math.abs(dice.nextInt())%VARIABILITY 
						- VARIABILITY/2;  
		
		// this gives a range of tolerance between -VARIABILITY/2 to +VARIABILITY/2
	}
		

	//--------------------------------------------------------------------//
	//--------------------------------------------------------------------//
	// Accessors
	//--------------------------------------------------------------------//
	//--------------------------------------------------------------------//

	// a negative risk tolerance means the player is averse to risk (nervous)
	// a positive risk tolerance means the player is open to risk   (adventurous)
	
	public int getRiskTolerance() {
		return riskTolerance - getStake(); // tolerance drops as stake increases
	}
	
	
	//--------------------------------------------------------------------//
	//--------------------------------------------------------------------//
	// Key decisions a player must make
	//--------------------------------------------------------------------//
	//--------------------------------------------------------------------//
	
	public boolean shouldOpen(PotOfMoney pot) {
		return true;
	}
	

	public boolean shouldSee(PotOfMoney pot) {
		if (getStake() == 0)
			return true;
		else
			return Math.abs(dice.nextInt())%100 < getHand().getRiskWorthiness() + 
											  getRiskTolerance();
	}
	

	public boolean shouldRaise(PotOfMoney pot) {
		return Math.abs(dice.nextInt())%80 < getHand().getRiskWorthiness() + 
											  getRiskTolerance();
	}
}
