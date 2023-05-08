
package CardSharps.TexasHoldem;
import java.util.ArrayList;
import java.util.Arrays;;
import java.util.List;

import CardSharps.Poker.*;

public class RoundOfTexasHoldem extends RoundOfTexas {
	private final DeckOfCards deck;


	//--------------------------------------------------------------------//
	//--------------------------------------------------------------------//
	// Constructor
	//--------------------------------------------------------------------//
	//--------------------------------------------------------------------//
	
	public RoundOfTexasHoldem(DeckOfCards deck, HoldemPlayer[] players, int smallBlind, int button) {
		super(players, smallBlind, button);
		this.deck    = deck; //init deck

		deal();
	}

	public void deal() {
		for (int i = 0; i < getNumPlayers(); i++) {
			int index = (getButton()+ i+ 1) % getNumPlayers();

			if (getPlayer(index) != null) {
				if (getPlayer(index).getBank() < getBigBlind())
					removePlayer(index);
				else {
					getPlayer(index).reset();
					( (HoldemPlayer) getPlayer(index)).dealTo(deck);
				}
			}
		}
	}
	//--------------------------------------------------------------------//
	//--------------------------------------------------------------------//
	// Play a round of Texas Hold'em
	//--------------------------------------------------------------------//
	//--------------------------------------------------------------------//

	public void play(){

		ArrayList<PotTexasHoldem> pots = new ArrayList<>();

		ArrayList<Player> listPlayers = new ArrayList<>(Arrays.asList(getPlayers()));
		PotTexasHoldem mainPot = new PotTexasHoldem(getActivePlayers(listPlayers));
		pots.add(mainPot);
		deck.reset();

		openBlinds(mainPot);
		printPlayerHand();

		System.out.println("---PREFLOP---");
		bettingCycle(mainPot, getButton() +3);   //3 is left of big blind
		printPlayerHand();

		System.out.println("---FLOP---");
		dealCommunity(3);
		bettingCycle(mainPot, getButton() + 1);
		printPlayerHand();

		System.out.println("---TURN---");
		dealCommunity(1);
		bettingCycle(mainPot, getButton()+1);
		printPlayerHand();

		System.out.println("---RIVER---");
		dealCommunity(1);
		bettingCycle(mainPot, getButton()+1);
		printPlayerHand();

		pots = newSidePots(mainPot);
		showdown(pots);
	}



	//Deal only to players still in game
	private void dealCommunity(int numCards){
		List<Card> list = new ArrayList<>(); //define community cards as an array of cards for reference
		
		for(int j = 0; j < numCards; j++){
			list.add(deck.dealNext());
		}

		System.out.println(list);

		for(int i = 0; i < getNumPlayers();i++){
			if(getPlayer(i) != null) {
				((HoldemPlayer) getPlayer(i)).getHand().addCommunityCards(list);
			}
		}
	}

	public void showdown(ArrayList<PotTexasHoldem> pots) {
		System.out.println("---SHOWDOWN---");
		printPlayerHand();
		printCommunityCards();

		ArrayList<HoldemPlayer> bestPlayer = new ArrayList<>();
		HoldemPlayer currentPlayer = null;
		int potNum = 0;

		for (PotTexasHoldem pot : pots) {
			if (pots.size() > 1) {        //if there's more than 1 pot say which pot it is
				System.out.println("---For pot " + potNum + " ---");
			}

			int bestHandScore = 0;
			int score;

			for (int i = 0; i < pot.getNumPlayers(); i++) {

				currentPlayer = (HoldemPlayer) pot.getPlayer(i);

				if (currentPlayer.getName() == null || currentPlayer.hasFolded()) {
					continue;
				}

				score = currentPlayer.getHand().getBestHandValue();
				if (score > bestHandScore) {
					bestHandScore = score;
					bestPlayer.clear();
					bestPlayer.add(currentPlayer);
				}
				else if (score == bestHandScore) {
					bestPlayer.add(currentPlayer);
				}
			}

			int numWinners = bestPlayer.size();
			if (numWinners == 1) {
				HoldemPlayer potWinner = bestPlayer.get(0);
				potWinner.takePot(pot);
			}
			else if (numWinners > 1) {
				for (HoldemPlayer player: bestPlayer) {
					player.sharePot(pot, numWinners);
				}
				int remainder = pot.getTotal() % numWinners;
				if(remainder > 0) {
					System.out.println("The remainder of " + currentPlayer.addCount(remainder, "chip", "chips") + " goes to the house");
				}
				pot.clearPot();
			}
			potNum++;
		}
	}

	public void bettingCycle(PotTexasHoldem mainPot, int playerStart) {
		int numActive = mainPot.getNumPlayers();
		for(Player player: mainPot.getPlayers()){
			if(player == null || player.hasFolded() || player.isAllIn()){
				numActive--;
			}
		}

		int stake = -1;
		int numPotPlayers = mainPot.getNumPlayers();
		ArrayList<Player> potPlayers = new ArrayList<>(mainPot.getPlayers());

		while (stake < mainPot.getCurrentStake() && numActive > 1) {
			stake = mainPot.getCurrentStake();

			for (int i = playerStart; i < numPotPlayers + playerStart; i++) {
				Player currentPlayer = potPlayers.get(i % numPotPlayers);

				if (currentPlayer == null || currentPlayer.hasFolded() || currentPlayer.isAllIn()) {
					continue;
				}

				if(numActive == 1){
					return;             //if only one player can still make bets end betting round
				} else {
					delay(DELAY_BETWEEN_ACTIONS);
					currentPlayer.nextAction(mainPot);
				}

				if (currentPlayer.hasFolded() || currentPlayer.isAllIn()) {                 //actions after player's move
					numActive--;
				}
			}
		}
	}

	private void printPlayerHand(){
		System.out.print("> Your Cards : \t");
		for (Card card : ( (HoldemPlayer) getPlayer(0)).getHand().getHand()) {
			System.out.print(card + " ");
		}
		System.out.println();
	}

	private void printCommunityCards(){
		System.out.print("> Community Cards : \t");
		for (Card card : ( (HoldemPlayer) getPlayer(0)).getHand().getCommunityCards()) {
			System.out.print(card + " ");
		}
		System.out.println();
	}

	//--------------------------------------------------------------------//
	//--------------------------------------------------------------------//
	// Some small but useful helper routines
	//--------------------------------------------------------------------//
	//--------------------------------------------------------------------//

	private void delay(int numMilliseconds) {
		try {
			Thread.sleep(numMilliseconds);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}  