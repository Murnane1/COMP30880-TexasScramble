
package CardSharps.TexasHoldem;

// This package provides classes necessary for implementing a game system for playing poker

// A RoundOfPoker is a single round/deal in a game
// A GameOfPoker is a sequence of one or more RoundOfPoker's
import CardSharps.Poker.*;

import java.util.Arrays;
import java.util.Scanner;


public class GameOfTexasHoldem 
{	
	private Player[] players;
	
	private final DeckOfCards deck;
	
	private int numPlayers;

	private static final int INIT_SMALL_BLIND = 1;

	private static final double BLIND_INCREASE_PER_ROUND = 1.25;
	private static final int MAX_NUM_PLAYERS = 9;
	private static final int STARTING_BANK = 100;
	
	//--------------------------------------------------------------------//
	//--------------------------------------------------------------------//
	// Constructor
	//--------------------------------------------------------------------//
	//--------------------------------------------------------------------//
	
	public GameOfTexasHoldem(int numPlayers, int bank, String humanName) {
		players = new Player[numPlayers];
		this.numPlayers = numPlayers;

		CreateHoldemPlayers computerPlayers = new CreateHoldemPlayers(numPlayers-1 , bank, humanName);
		players[0] = new HumanHoldemPlayer(humanName, bank);
		for(int i=1; i < numPlayers; i++){
			players[i] = computerPlayers.getPlayer(i-1);
		}
		
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
		for (int i = 0; i < getNumPlayers(); i++) {
			if (getPlayer(i) != null && !getPlayer(i).isBankrupt())
				count++;
		}
		return count;
	}

	public int getNumPlayersMeetBlinds(int bigBlind) {
		// how many players can meet the blind for the upcoming round
		int count = 0;
		for (int i = 0; i < getNumPlayers(); i++) {
			if (getPlayer(i) != null && getPlayer(i).getBank() >= bigBlind)
				count++;
		}
		return count;
	}

	public void removePlayer(int num) {
		if (num >= 0 && num < numPlayers) {
			System.out.println("\n> " + players[num].getName() + " leaves the game.\n");
			players[num] = null;
		}
	}
	
	//--------------------------------------------------------------------//
	//--------------------------------------------------------------------//
	// Play Poker
	//--------------------------------------------------------------------//
	//--------------------------------------------------------------------//

	public void play()	{
		double blindTracker = INIT_SMALL_BLIND;
		int smallBlind = INIT_SMALL_BLIND;
		int bigBlind = smallBlind*2;
		int button = 0;


		while (getNumSolventPlayers() > 1) {
			for (int i = 0; i < numPlayers; i++) {                  //remove any player without bank to play round
				if (getPlayer(i) != null) {
					if (getPlayer(i).getBank() < bigBlind) {
						if(getPlayer(i) == getPlayer(0)){
							System.out.println("\n"+getPlayer(0).getName() + " you do not have sufficient funds to play anymore\nYour game is over");
							System.exit(0);
						}
						removePlayer(i);
					}
				}
			}

			RoundOfTexasHoldem round = new RoundOfTexasHoldem(deck, players, smallBlind, button);
			
			round.play();

			blindTracker    *=  BLIND_INCREASE_PER_ROUND;
			smallBlind      =   (int) Math.floor(blindTracker);
			bigBlind        =   smallBlind*2;
			button++;

			try {
				if(getNumPlayersMeetBlinds(bigBlind) < 2){
					System.out.println("The game is over. There is only one player able to meet the big blind of " + bigBlind);
					for (Player player : players){
						if (player != null && player.getBank() > bigBlind)
							System.out.println("\n\n***** "+player.getName() + " is the WINNER! *****\n");
					}
					return;
				}

				System.out.print("\n\nPlay another round? Press 'q' to terminate this game ... ");
				byte[] input = new byte[100];
				System.in.read(input);

				for (int i = 0; i < input.length; i++)
					if ((char)input[i] == 'q' || (char)input[i] == 'Q')
						return;
			}
			catch (Exception e) {e.printStackTrace();};
		}
	}


	//--------------------------------------------------------------------//
	//--------------------------------------------------------------------//
	// Launcher for application
	//--------------------------------------------------------------------//
	//--------------------------------------------------------------------//

	public static void main(String[] args) {
		String[] takenNames = {"Tom", "Dick", "Harry", "Sarah", "Maggie", "Andrew", "Zoe", "Sadbh", "Mark", "Sean"};
		
		System.out.println("\nWelcome to the Automated Texas Hold'em Machine ...\n\n");

		String humanName = null;
		while (humanName == null || humanName == "" || Arrays.asList(takenNames).contains(humanName)){
			System.out.print("\nWhat is your name?  ");
			try {
				Scanner scanName = new Scanner(System.in);
				humanName = scanName.nextLine();
				humanName = humanName.replaceAll("\\s", "");
				if(humanName == "" || humanName == null){
					System.out.println("You must have a name");
				} else if (Arrays.asList(takenNames).contains(humanName)) {
					System.out.println("The name " + humanName + " is already taken");
				}
			}
			catch (Exception e){e.printStackTrace();}
		}

		int numPlayers = 0;
		while (numPlayers < 2 || numPlayers > MAX_NUM_PLAYERS) {
			System.out.print("\nHow many players should be in the game?  ");
			Scanner scNP = new Scanner(System.in);
			numPlayers = scNP.nextInt();
			if(numPlayers < 2){
				System.out.println("You cannot play on your own! (The maximum number of players is " + MAX_NUM_PLAYERS + ")");
			} else if(numPlayers > MAX_NUM_PLAYERS){
				System.out.println("The maximum number of players for our game of Texas Scrabble is " + MAX_NUM_PLAYERS);
			}
		}

		System.out.println("\nLet's play SCRAMBLE ...\n\n");

		GameOfTexasHoldem game = new GameOfTexasHoldem(numPlayers, STARTING_BANK, humanName);

		game.play();
	}
	

}
