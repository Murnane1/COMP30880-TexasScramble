
package CardSharps.TexasHoldem;

// This package provides classes necessary for implementing a game system for playing poker

// A RoundOfPoker is a single round/deal in a game
// A GameOfPoker is a sequence of one or more RoundOfPoker's
import CardSharps.Poker.*;


public class GameOfTexasHoldem 
{	
	private Player[] players;
	
	private DeckOfCards deck;
	
	private int numPlayers;

	private final static int INIT_SMALL_BLIND = 1;
	
	//--------------------------------------------------------------------//
	//--------------------------------------------------------------------//
	// Constructor
	//--------------------------------------------------------------------//
	//--------------------------------------------------------------------//
	
	public GameOfTexasHoldem(String[] names, int bank) {
		numPlayers = names.length;
		players = new Player[numPlayers];
		
		for (int i = 0; i < numPlayers; i++)
			if (i == 0)
				players[i] = new HumanHoldemPlayer(names[i].trim(), bank);
			else
				players[i] = new ComputerHoldemPlayer(names[i].trim(), bank);
		
		deck  = new DeckOfCards();
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

	
	public int getNumSolventPlayers() { 
		// how many players still have money left?
	
		int count = 0;
		
		for (int i = 0; i < getNumPlayers(); i++)
			if (getPlayer(i) != null && !getPlayer(i).isBankrupt())
				count++;
		
		return count;
	}

	
	//--------------------------------------------------------------------//
	//--------------------------------------------------------------------//
	// Play Poker
	//--------------------------------------------------------------------//
	//--------------------------------------------------------------------//

	public void play()	{
		int smallBlind = INIT_SMALL_BLIND;
		int button = 0;
		while (getNumSolventPlayers() > 1) {
			RoundOfTexasHoldem round = new RoundOfTexasHoldem(deck, players, smallBlind, button);
			
			round.play();

			smallBlind++;
			button++;
			try {
				System.out.print("\n\nPlay another round? Press 'q' to terminate this game ... ");
				
				byte[] input = new byte[100];
				
				System.in.read(input);
				
				for (int i = 0; i < input.length; i++)
					if ((char)input[i] == 'q' || (char)input[i] == 'Q')
						return;
			}
			catch (Exception e) {};
		}
	}


	//--------------------------------------------------------------------//
	//--------------------------------------------------------------------//
	// Launcher for application
	//--------------------------------------------------------------------//
	//--------------------------------------------------------------------//

	public static void main(String[] args) {
		String[] names = {"Human", "Tom", "Dick", "Harry"};
		
		System.out.println("\nWelcome to the Automated Texas Hold'em Machine ...\n\n");
		
		System.out.print("\nWhat is your name?  ");
		
		byte[] input = new byte[100];
		
		try {
			System.in.read(input);
	
			names[0] = new String(input);
		}
		catch (Exception e){};
							 
		int startingBank = 10;
		
		System.out.println("\nLet's play POKER ...\n\n");
		
		GameOfTexasHoldem game = new GameOfTexasHoldem(names, startingBank);
	
		game.play();
	}
	

}
