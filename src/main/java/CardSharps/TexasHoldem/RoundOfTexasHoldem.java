
package CardSharps.TexasHoldem;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

import CardSharps.Poker.*;

// This package provides classes necessary for implementing a game system for playing poker

// A RoundOfPoker is a single round/deal in a game
// A PokerGame is a sequence of RoundOfPoker's


public class RoundOfTexasHoldem {	
	public static final int DELAY_BETWEEN_ACTIONS	=	1000;  // number of milliseconds between game actions
	private PlayerInterface[] players;
	private DeckOfCards deck;
	private int numPlayers;
	
	private int button = 0; // Player starts as the dealer;
	private int smallBlind = 1;
	private int bigBlind = 2;

	//--------------------------------------------------------------------//
	//--------------------------------------------------------------------//
	// Constructor
	//--------------------------------------------------------------------//
	//--------------------------------------------------------------------//
	
	public RoundOfTexasHoldem(DeckOfCards deck, PlayerInterface[] players, int smallBlind, int button) {
		this.players = players; //init players
		this.deck    = deck; //init deck
		this.smallBlind = smallBlind;
		this.bigBlind = 2*smallBlind;
		numPlayers   = players.length; //get totalPlayers

		this.button = button;
		System.out.println("\n\nNew Deal:\n\n");
		deal();
	}
		

	//--------------------------------------------------------------------//
	//--------------------------------------------------------------------//
	// Accessors
	//--------------------------------------------------------------------//
	//--------------------------------------------------------------------//

	public int getNumPlayers() {
		return numPlayers;	
		
	}
	
	
	public PlayerInterface getPlayer(int num) {
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

			PlayerInterface[] newPlayers = new PlayerInterface[players.length-1];
			int count = 0;
			for(int i = 0; i<players.length; i++){
				if( i!=num){
					newPlayers[count] = players[i];
					count++;
				}
			}
			players = newPlayers;
			numPlayers = players.length;
		}
	}	
	

		
	//--------------------------------------------------------------------//
	//--------------------------------------------------------------------//
	// Restart all the players and deal them into the game
	//--------------------------------------------------------------------//
	//--------------------------------------------------------------------//

	public void deal() {

		for (int i = 0; i < getNumPlayers(); i++) {
			int index = (button+ i+ 1) % getNumPlayers();

			if (getPlayer(index) != null) {
				if (getPlayer(index).isBankrupt())
					removePlayer(index);
				else {
					
					getPlayer(index).reset();
					getPlayer(index).dealTo(deck);

					System.out.println(getPlayer(index));
				}
			}
		}
		
		System.out.println("\n");
	}

	//--------------------------------------------------------------------//
	//--------------------------------------------------------------------//
	// See if we can open a round (at least one player must have atg least
	// a pair)
	//--------------------------------------------------------------------//
	//--------------------------------------------------------------------//


	public void canOpen(PotTexasHoldem pot) {
		if(numPlayers<=1){
			return;
		}
		int i = 1;
		//Player to the left of the dealer posts the small blind
		while( players[(button+i)%numPlayers]!=null && players[(button+i)%numPlayers].getBank()<bigBlind ){
			if(numPlayers<=1){
				System.exit(0);;
				return;
			}
			//Player does not have enough chips to open

			System.out.println(players[(button+i)%numPlayers].getName() + "says: I cannot post the Small Blind. \n" + "I can't afford to play anymore");
			
			// removePlayer((button + i)%numPlayers);
			pot.removePlayer(players[(button + i)%numPlayers]);
			i++;
			
		}
		players[(button+i)%numPlayers].postBlind(pot, smallBlind, "Small Blind");

		if(numPlayers<=1){

			return;
		}
		//Player to the left of the small blind posts the big blind
		while( players[(button+i+1)%numPlayers]!=null && players[(button+i+1)%numPlayers].getBank()<bigBlind ){
			if(numPlayers<=1){
				System.exit(0);;


				return;
			}
			//Player does not have enough chips to open
				System.out.println(players[(button+i+1)%numPlayers].getName() + "says: I cannot post the Big Blind. . \n " +"I can't afford to play anymore");
				
				// removePlayer((button+i+1)%numPlayers);
				pot.removePlayer(players[(button + i)%numPlayers]);

				i++;
			

		}
		players[(button+i+1)%numPlayers].postBlind(pot, bigBlind, "Big Blind");


	}

	
	
	//--------------------------------------------------------------------//
	//--------------------------------------------------------------------//
	// Play a round of Texas Hold'em
	//--------------------------------------------------------------------//
	//--------------------------------------------------------------------//


	ArrayList<PotTexasHoldem> pots = new ArrayList<PotTexasHoldem>();

	public ArrayList<PotTexasHoldem> getPots() {
		return pots;
	}

	public void setPots(ArrayList<PotTexasHoldem> pots) {
		this.pots = pots;
	}

	public void play(){

		ArrayList<PlayerInterface> listPlayers = new ArrayList<>(Arrays.asList(players));
		PotTexasHoldem mainPot = new PotTexasHoldem(listPlayers);
		pots.add(mainPot);

		// Initialize bank and print the values for each player;
		Integer numActive = mainPot.getNumPlayers();
		Integer stake = -1;
		deck.reset();


		//roundOpen(mainPot, players[(button+1)%numPlayers], players[(button+2)%numPlayers]);
		canOpen(pots.get(0));
		//PRINTING PLAYER HAND
		printPlayerHand();

		// Game actions
		// (call, raise, fold);
		// Start betting sequence left of the big blind;
		preflop(pots.get(0));

		printPlayerHand();
		//Whilst there are >= 2 players are still active;
		flop(pots.get(0));

		printPlayerHand();
		// Turn 4th community card (turn) is turned while there are >= 2 players active.
		turn(pots.get(0));

		printPlayerHand();
		// Turn 5th community card (river) is turned if there are still >= 2 players active.
		river(pots.get(0));

		printPlayerHand();
		showdown();
	}



	//TODO
	//Deal only to players still in game
	private void dealCommunity(int numCards, PotTexasHoldem pot){
		List<Card> list = new ArrayList<>(); //define community cards as an array of cards for reference
		
		for(int j = 0; j < numCards; j++){
			list.add(deck.dealNext());
		}

		System.out.println(list);

		for(int i = 0; i < pot.getPlayers().size();i++){
			pot.getPlayer(i).addCommunityCards(list);
		}

	}

	private void preflop(PotTexasHoldem mainPot) {

		System.out.println("---PREFLOP---");


		int playerStart = button+3;	//3 becouse player left to big blind starts

		bettingCycle(mainPot, playerStart);
	}

	private void flop(PotTexasHoldem mainPot){

		System.out.println("---FLOP---");

		// Turn 3 community (flop) cards
		dealCommunity(3, mainPot);

		int playerStart = button+1;	//3 becouse player left to big blind starts


		bettingCycle(mainPot, playerStart);

	}

	private void turn(PotTexasHoldem mainPot){

		System.out.println("---TURN---");


		// Deal the turn card card
		dealCommunity(1,mainPot);

		int playerStart = button+1;	//3 becouse player left to big blind starts

		bettingCycle(mainPot, playerStart);
	}

	private void river(PotTexasHoldem mainPot){


		System.out.println("---RIVER---");


		// Deal the river card
		dealCommunity(1,mainPot);

		int playerStart = button+1;	//3 becouse player left to big blind starts

		bettingCycle(mainPot, playerStart);
	}

	private void showdown(){

		System.out.println("---SHOWDOWN---");

		int bestHandScore = 0, score = 0, bestPos = 0, potNum =0;
		PlayerInterface bestPlayer = null, currentPlayer = null;

		for (PotTexasHoldem pot: pots) {

			if(pots.size() > 1) {		//if there's more than 1 pot say which pot it is
				System.out.println("---For pot " + potNum + " ---");
			}


			bestPlayer = pot.getPlayer(0);
			for (int i = 0; i < pot.getNumPlayers(); i++) {
				currentPlayer = pot.getPlayer(i);
				if (currentPlayer.getName() == null || currentPlayer.hasFolded()){
					continue;
				}
				System.out.println("Player " + currentPlayer.getName() + "'s hand: " + currentPlayer.getHand().getBestHand());
				score = currentPlayer.getHand().getBestHandValue();
				if (score > bestHandScore) {
					bestPos = i;
					bestHandScore = score;
					bestPlayer = currentPlayer;
				}
			}
			System.out.println(bestPlayer.getName() + " takes pot of " + pot.getTotal() + " chips!");

			bestPlayer.takePot(pot);
			potNum++;
		}
	}

	public void bettingCycle(PotTexasHoldem mainPot, int playerStart) {
		int indexCurrPot = 0;
		int stake = -1;
		int numActive = mainPot.getNumPlayers();

		while (stake < mainPot.getCurrentStake() && numActive > 1) {

			stake = mainPot.getCurrentStake();

			for (int i = 0; i < getNumPlayers(); i++) {
				PlayerInterface currentPlayer = mainPot.getPlayer((playerStart + i) % mainPot.getNumPlayers());

				if (currentPlayer == null || currentPlayer.hasFolded() || currentPlayer.isAllIn()) {
					continue;
				}

				delay(DELAY_BETWEEN_ACTIONS);

				currentPlayer.nextAction(mainPot);

				//actions after player's move
				if (currentPlayer.isAllIn() || currentPlayer.hasFolded()) {
					numActive--;
				}
			}
		}
	}

	/*
	*
	* Sorts the player's in ascending order of stake
	* for each player who's stake is less than the max player's stake
	*
	*
	* */
	ArrayList<PlayerInterface> newPotPlayers;
	public ArrayList<PotTexasHoldem> newSidePots(PotTexasHoldem pot) {
		ArrayList<PotTexasHoldem> sidePots = new ArrayList<>();
		sidePots.add(pot);

		newPotPlayers = new ArrayList<>(pot.getPlayers());

		Collections.sort(newPotPlayers, new Comparator<PlayerInterface>() {
			@Override
			public int compare(PlayerInterface p1, PlayerInterface p2) {
				return p1.getStake() - p2.getStake();
			}
		});

		int maxPlayerStake = newPotPlayers.get(newPotPlayers.size() - 1).getStake();

		System.out.println(newPotPlayers);

		for (int playerIndex = 0; playerIndex < newPotPlayers.size(); playerIndex++) {
			//if(newPotPlayers.get(playerIndex).getStake() < maxPlayerStake  &&  newPotPlayers.get(playerIndex).getStake() > 0) {
			if(newPotPlayers.get(playerIndex).isAllIn()){
				addNewSidePot(newPotPlayers, sidePots, playerIndex);
			}
		}

		return sidePots;
	}

	public void addNewSidePot(ArrayList<PlayerInterface> XXXXnewPotPlayers, ArrayList<PotTexasHoldem> sidePots, int allInPlayer) {
		int currPot = sidePots.size() - 1;

		int potMax = newPotPlayers.get(allInPlayer).getStake();    //max value allowed for each player in current pot
		sidePots.get(currPot).setMaxStake(potMax);

		//ArrayList<PlayerInterface> nextPotPlayers = new ArrayList<>(newPotPlayers);
		newPotPlayers.remove(allInPlayer);

		PotTexasHoldem newPot = new PotTexasHoldem(newPotPlayers);

		for (int playerIndex = allInPlayer; playerIndex < newPotPlayers.size(); playerIndex++) {
			int overflow = newPotPlayers.get(playerIndex).getStake() - potMax;
			newPot.addToPot(overflow);
			newPotPlayers.get(playerIndex).reduceStake(overflow);
			sidePots.get(currPot).removeFromPot(overflow);
		}

		//if(newPot.getTotal() > 0){
			sidePots.add(newPot);
		//}
	}
	private void printPlayerHand(){
		
		System.out.println(">Your Cards : ");
		for (Card card : players[0].getHand().getHand()) {
			System.out.print(card + " ");
		}
		System.out.println("");
		
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