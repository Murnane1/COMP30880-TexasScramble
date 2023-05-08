package CardSharps.TexasScramble;


import CardSharps.TexasHoldem.Player;
import CardSharps.TexasHoldem.PotTexasHoldem;
import CardSharps.TexasHoldem.RoundOfTexas;

import java.util.*;

public class RoundOfTexasScramble extends RoundOfTexas {
    public static final int PENALTY = 3;
    private final BagOfTiles bag;
    private final ScrabbleDictionary dictionary;


    public RoundOfTexasScramble(BagOfTiles bag, ScramblePlayer[] players, int smallBlind, int button, ScrabbleDictionary dictionary) {
        super(players, smallBlind, button);
        this.bag = bag; //init deck
        this.dictionary = dictionary;

        deal();
    }



    public void deal() {
        for (int i = 0; i < getNumPlayers(); i++) {
            int index = (getButton() + i + 1) % getNumPlayers();

            if (getPlayer(index) != null) {
                if (getPlayer(index).getBank() < getBigBlind())
                    removePlayer(index);
                else {
                    getPlayer(index).reset();
                    ( (ScramblePlayer) getPlayer(index)).dealTo(bag, dictionary);
                }
            }
        }
    }

    public void play() {
        ArrayList<PotTexasHoldem> pots = new ArrayList<>();

        ArrayList<Player> listPlayers = new ArrayList<>(Arrays.asList(getPlayers()));
        PotTexasHoldem mainPot = new PotTexasHoldem(getActivePlayers(listPlayers));
        pots.add(mainPot);

        bag.reset();

        openBlinds(mainPot);
        printPlayerHand();

        System.out.println("---PREFLOP---");
        bettingCycle(mainPot, getButton()+3);   //3 is left of big blind
        printPlayerHand();

        System.out.println("---FLOP---");
        dealCommunity(3);
        bettingCycle(mainPot, getButton()+1);
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
        declareWords(pots.get(0));
        showdown(pots);
    }

    private void dealCommunity(int numCards) {
        List<Tile> list = new ArrayList<>(); //define community cards as an array of cards for reference

        for (int j = 0; j < numCards; j++) {
            list.add(bag.dealNext());
        }

        System.out.println(list);

        for (int i = 0; i < getNumPlayers(); i++) {
            if(getPlayer(i) != null) {
                ( (ScramblePlayer) getPlayer(i)).getHand().addCommunityTiles(list);
            }
        }
    }

    public void declareWords(PotTexasHoldem mainPot) {
        ArrayList<ScramblePlayer> activePlayers = new ArrayList<>();
        for (Player player: getActivePlayers(mainPot.getPlayers())) {
            activePlayers.add(((ScramblePlayer) player));
        }

        if(activePlayers.size() == 1) {
            System.out.println("As " + activePlayers.get(0).getName() + " is the only remaining player there is no word reveal");
            return;
        }
        System.out.println("---WORD CHOICE---");

        for (ScramblePlayer player: activePlayers) {
            player.chooseWord();
        }

        System.out.println("---WORD REVEAL---");

        ScramblePlayer player;
        for(int i=0; i < activePlayers.size(); i++){
            player = activePlayers.get((getButton() + i) % activePlayers.size());
            System.out.println(player.getName() + "'s word is \"" + player.getWord() + "\"");

            for (ScramblePlayer challenger: activePlayers) {
                if(!player.getName().equals(challenger.getName()) /*&& challenger.getBank() > PENALTY*/){
                    if (challenger.shouldChallenge(mainPot, player.getWord())) {
                        challenge(player, challenger, mainPot);
                        break;
                    }
                }
            }
        }
    }

    public void printPlayerHand() {
        System.out.print("> Your Cards: ");
        for (Tile tile : ( (ScramblePlayer) getPlayer(0)).getHand().getHand()) {
            System.out.print(tile + " ");
        }
        System.out.println("");
    }

    public void showdown(ArrayList<PotTexasHoldem> pots) {
        System.out.println("---SHOWDOWN---");

        ArrayList<ScramblePlayer> bestPlayer = new ArrayList<>();
        ScramblePlayer currentPlayer = null;
        int potNum = 0;

        for (PotTexasHoldem pot : pots) {
            if (pots.size() > 1) {        //if there's more than 1 pot say which pot it is
                System.out.println("---For pot " + potNum + " ---");
            }

            int bestHandScore = 0;
            int score;

            for (int i = 0; i < pot.getNumPlayers(); i++) {

                currentPlayer = (ScramblePlayer) pot.getPlayer(i);
                if (currentPlayer.getName() == null || currentPlayer.hasFolded()) {
                    continue;
                }
                score = currentPlayer.getWordScore();
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
                ScramblePlayer potWinner = bestPlayer.get(0);
                potWinner.takePot(pot);
            }
            else if (numWinners > 1) {
                for (ScramblePlayer player: bestPlayer) {
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

    public void challenge(ScramblePlayer player, ScramblePlayer challenger, PotTexasHoldem pot){
        System.out.println(challenger.getName() + " challenges " + player.getName() + "'s word \"" + player.getWord() + "\"");
        if(dictionary.contains(player.getWord())){        //if word valid - challenger loses penalty cost (to opposition or pot?)
            challenger.penalty(PENALTY, pot);
            System.out.println("According to the scrabble dictionary \"" + player.getWord() + "\" is a valid word \n "
                    + "The challenger " + challenger.getName() + " pays a penalty of " + PENALTY + " into the pot");
        } else {                        //if word invalid - player's score for round is 0
            player.setWordScore(0);
            System.out.println("According to the scrabble dictionary \"" + player.getWord() + "\" is NOT a valid word \n"
                    + player.getName() + " gets a score of " + player.getWordScore() + " for this round");
        }
    }
}
