
package poker;

// This package provides classes necessary for implementing a game system for playing poker

// A RoundOfPoker is a single round/deal in a game
// A PokerGame is a sequence of RoundOfPoker's


public class RoundOfPoker {	
	public static int DELAY_BETWEEN_ACTIONS	=	1000;  // number of milliseconds between game actions
	
	private Player[] players;
	
	private DeckOfCards deck;
	private int numPlayers;
	
	
	//--------------------------------------------------------------------//
	//--------------------------------------------------------------------//
	// Constructor
	//--------------------------------------------------------------------//
	//--------------------------------------------------------------------//
	
	public RoundOfPoker(DeckOfCards deck, Player[] players) {
		this.players = players;
		
		this.deck    = deck;
		
		numPlayers   = players.length;
	
		System.out.println("\n\nNew Deal:\n\n");
		
		deal();
		
		while (!canOpen()) deal();  // continue to redeal until some player can open
		
		openRound();

		discard();
	}
		

	//--------------------------------------------------------------------//
	//--------------------------------------------------------------------//
	// Accessors
	//--------------------------------------------------------------------//
	//--------------------------------------------------------------------//

	public int getNumPlayers() {
		return numPlayers;	
		
	}
	
	
	public Player getPlayer(int num) {
		if (num >= 0 && num <= numPlayers)
			return players[num];
		else
			return null;
	}
	
								
	
	public int getNumActivePlayers() {
	    // how many players have not folded yet?

		int count = 0;
		
		for (int i = 0; i < getNumPlayers(); i++)
			if (getPlayer(i) != null && !getPlayer(i).hasFolded() && !getPlayer(i).isBankrupt())
				count++;
		
		return count;
	}

	//--------------------------------------------------------------------//
	//--------------------------------------------------------------------//
	// Modifiers
	//--------------------------------------------------------------------//
	//--------------------------------------------------------------------//

	public void removePlayer(int num) {
		if (num >= 0 && num < numPlayers)
		{
			System.out.println("\n> " + players[num].getName() + " leaves the game.\n");
			
			players[num] = null;
		}
	}	
	

	//--------------------------------------------------------------------//
	//--------------------------------------------------------------------//
	// Find the player with the best hand
	//--------------------------------------------------------------------//
	//--------------------------------------------------------------------//

	public int getNumBestPlayer(boolean display) {
		int bestHandScore = 0, score = 0, bestPos = 0;
			
		Player bestPlayer = null, currentPlayer = null;
			
		for (int i = 0; i < getNumPlayers(); i++) {
			currentPlayer = getPlayer(i);
				
			if (currentPlayer == null || currentPlayer.hasFolded())
				continue;
				
			score = currentPlayer.getHand().getValue();
				
			if (score > bestHandScore) {
				if (display) {
					if (bestHandScore == 0)
						System.out.println("> " + currentPlayer.getName() + " goes first:\n" + 
											 currentPlayer.getHand());
					else
						System.out.println("> " + currentPlayer.getName() + " says 'Read them and weep:'\n" + 
											 currentPlayer.getHand());
				}
				
				bestPos		  = i;
				bestHandScore = score;
				bestPlayer    = currentPlayer;
			}
			else
			if (display)
				System.out.println("> " + currentPlayer.getName() + " says 'I lose':\n" + 
										  currentPlayer.getHand());
		}

		return bestPos;
	}
		
	//--------------------------------------------------------------------//
	//--------------------------------------------------------------------//
	// Restart all the players and deal them into the game
	//--------------------------------------------------------------------//
	//--------------------------------------------------------------------//

	public void deal() {
		for (int i = 0; i < getNumPlayers(); i++) {
			if (getPlayer(i) != null) {
				if (getPlayer(i).isBankrupt())
					removePlayer(i);
				else {
					getPlayer(i).reset();
					getPlayer(i).dealTo(deck);
					
					System.out.println(getPlayer(i));
				}
			}
		}
		
		System.out.println("\n");
	}

	//--------------------------------------------------------------------//
	//--------------------------------------------------------------------//
	// Allow each player to discard some cards and receive new ones
	//--------------------------------------------------------------------//
	//--------------------------------------------------------------------//

	public void discard() {
		for (int i = 0; i < getNumPlayers(); i++) {
			if (getPlayer(i) != null)
				getPlayer(i).discard();
		}
	}

	//--------------------------------------------------------------------//
	//--------------------------------------------------------------------//
	// See if we can open a round (at least one player must have atg least
	// a pair)
	//--------------------------------------------------------------------//
	//--------------------------------------------------------------------//

	public boolean canOpen() {
		PokerHand hand = getPlayer(getNumBestPlayer(false)).getHand();
		
		if (hand instanceof High) // not good enough
			return false;
		else
			return true;
	}

	//--------------------------------------------------------------------//
	//--------------------------------------------------------------------//
	// Open this round of poker
	//--------------------------------------------------------------------//
	//--------------------------------------------------------------------//
	
	public void openRound()	{
		Player player = null;
		
		System.out.println("");
		
		for (int i = 0; i < numPlayers; i++) {
			player = getPlayer(i);
			
			if (player == null || player.isBankrupt()) 
				continue;
			
			if (player.getHand() instanceof High)
				System.out.println("> " + player.getName() + " says: I cannot open.");
			else
				System.out.println("> " + player.getName() + " says: I can open.");
		}
	}
	
	//--------------------------------------------------------------------//
	//--------------------------------------------------------------------//
	// Play a round of poker
	//--------------------------------------------------------------------//
	//--------------------------------------------------------------------//

	public void play() {
		PotOfMoney pot = new PotOfMoney();
		
		int numActive = getNumActivePlayers();
		
		int stake = -1;
		
		Player currentPlayer = null;
		
		deck.reset();
		
		// while the stakes are getting bigger and there is at least one active player,
		// then continue to go around the table and play
		
		while (stake < pot.getCurrentStake() && numActive > 0) {
			stake = pot.getCurrentStake();
			
			for (int i = 0; i < getNumPlayers(); i++) {
				currentPlayer = getPlayer(i);
				
				if (currentPlayer == null || currentPlayer.hasFolded())
					continue;   
				
				delay(DELAY_BETWEEN_ACTIONS);
				
				if (numActive == 1) {
					// this must be the last player
				
					currentPlayer.takePot(pot);
					return;
				}
				else
					currentPlayer.nextAction(pot);
				
				if (currentPlayer.hasFolded()) // must have just folded
					numActive--;
			}
		}
		
		if (numActive == 0) {
			// no player is left in the game
			
			System.out.println("\nNo Players left in the game.\n");
			return;
		}
				
		Player bestPlayer = getPlayer(getNumBestPlayer(true));
			
		if (bestPlayer != null)
			bestPlayer.takePot(pot);
	}
	
	
	
	//--------------------------------------------------------------------//
	//--------------------------------------------------------------------//
	// Some small but useful helper routines
	//--------------------------------------------------------------------//
	//--------------------------------------------------------------------//

	private void delay(int numMilliseconds) {
		try {
			Thread.sleep(numMilliseconds);
		} catch (Exception e) {}
	}
}  