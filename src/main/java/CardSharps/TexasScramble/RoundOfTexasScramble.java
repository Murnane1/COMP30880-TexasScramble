package CardSharps.TexasScramble;


import java.util.*;

public class RoundOfTexasScramble {
    public static final int DELAY_BETWEEN_ACTIONS = 1000;  // number of milliseconds between game actions
    public static final int PENALTY = 3;
    private Player[] players;
    private BagOfTiles bag;
    private ScrabbleDictionary dictionary;
    private int numPlayers;
    private int button      = 0;
    private int smallBlind  = 1;
    private int bigBlind    = 2;


    public RoundOfTexasScramble(BagOfTiles bag, Player[] players, int smallBlind, int button, ScrabbleDictionary dictionary) {
        this.players = players; //init players
        this.bag = bag; //init deck
        this.smallBlind = smallBlind;
        this.bigBlind = 2 * smallBlind;
        numPlayers = players.length; //get totalPlayers

        this.button = button;
        this.dictionary = dictionary;

        System.out.println("\n\nPlayer Stacks:\n");
        for (Player player: players) {
            if(player != null) {
                System.out.println(player.getName() + " has " + player.addCount(player.getBank(), "chip", "chips") + " in the bank");
            }
        }
        System.out.println("\nNew Deal:\n\n");
        deal();
    }

    public int getNumPlayers() {
        return numPlayers;

    }

    public ArrayList<Player> getActivePlayers(ArrayList<Player> players){
        ArrayList<Player> activePlayers = new ArrayList<>(players);
        for(Player player: players){
            if(player == null || player.hasFolded()){
                activePlayers.remove(player);
            }
        }
        return activePlayers;
    }

    public Player getPlayer(int num) {
        if (num >= 0 && num <= numPlayers)
            return players[num];
        else
            return null;
    }

    public void removePlayer(int num) {
        if (num >= 0 && num < numPlayers) {
            System.out.println("\n> " + players[num].getName() + " leaves the game.\n");

            Player[] newPlayers = new Player[players.length - 1];
            int count = 0;
            for (int i = 0; i < players.length; i++) {
                if (i != num) {
                    newPlayers[count] = players[i];
                    count++;
                }
            }
            players = newPlayers;
            numPlayers = players.length;
        }
    }

    public void deal() {
        for (int i = 0; i < getNumPlayers(); i++) {
            int index = (button + i + 1) % getNumPlayers();

            if (getPlayer(index) != null) {
                if (getPlayer(index).getBank() < bigBlind)
                    removePlayer(index);
                else {
                    getPlayer(index).reset();
                    getPlayer(index).dealTo(bag, dictionary);
                }
            }
        }
    }

    public void openBlinds(PotOfMoney pot) {
        if (numPlayers <= 1) {
            System.out.println("Not enough players left to play " + players[0].getName() + " is the winner");
            return;
        }

        int i = 1;
        while (players[(button + i) % numPlayers] == null || players[(button + i) % numPlayers].getBank() < bigBlind) {         //Player to the left of the dealer posts the small blind
            i++;
        }
        players[(button + i) % numPlayers].postBlind(pot, smallBlind, "Small Blind");

        while (players[(button + i + 1) % numPlayers] == null || players[(button + i + 1) % numPlayers].getBank() < bigBlind) { //Player to the left of the small blind posts the big blind
            i++;
        }
        players[(button + i + 1) % numPlayers].postBlind(pot, bigBlind, "Big Blind");
    }

    private void dealCommunity(int numCards) {
        List<Tile> list = new ArrayList<>(); //define community cards as an array of cards for reference

        for (int j = 0; j < numCards; j++) {
            list.add(bag.dealNext());
        }

        System.out.println(list);

        for (int i = 0; i < getNumPlayers(); i++) {
            if(players[i] != null) {
                players[i].getHand().addCommunityTiles(list);
            }
        }
    }

    public void play() {
        ArrayList<PotOfMoney> pots = new ArrayList<>();

        ArrayList<Player> listPlayers = new ArrayList<>(Arrays.asList(players));
        PotOfMoney mainPot = new PotOfMoney(getActivePlayers(listPlayers));
        pots.add(mainPot);

        bag.reset();

        openBlinds(mainPot);
        printPlayerHand();

        System.out.println("---PREFLOP---");
        bettingCycle(mainPot, button +3);   //3 is left of big blind
        printPlayerHand();

        System.out.println("---FLOP---");
        dealCommunity(3);
        bettingCycle(mainPot, button + 1);
        printPlayerHand();

        System.out.println("---TURN---");
        dealCommunity(1);
        bettingCycle(mainPot, button+1);
        printPlayerHand();

        System.out.println("---RIVER---");
        dealCommunity(1);
        bettingCycle(mainPot, button+1);
        printPlayerHand();

        pots = newSidePots(mainPot);
        declareWords(pots.get(0));
        showdown(pots);
    }



    public void bettingCycle(PotOfMoney mainPot, int playerStart) {
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
                    //delay(DELAY_BETWEEN_ACTIONS);                 //TODO put back in in final submission
                    currentPlayer.nextAction(mainPot);
                }

                if (currentPlayer.hasFolded() || currentPlayer.isAllIn()) {                 //actions after player's move
                    numActive--;
                }
            }
        }
    }


    public void declareWords(PotOfMoney mainPot) {
        ArrayList<Player> activePlayers = getActivePlayers(mainPot.getPlayers());
        if(activePlayers.size() == 1) {
            System.out.println("As " + activePlayers.get(0).getName() + " is the only remaining player there is no word reveal");
            return;
        }
        System.out.println("---WORD REVEAL---");

        for (Player player: activePlayers) {
            player.chooseWord();
        }

        Player player;
        for(int i=0; i < activePlayers.size(); i++){
            player = activePlayers.get((button + i) % activePlayers.size());
            System.out.println(player.getName() + "'s word is \"" + player.getWord() + "\"");

            for (Player challenger: activePlayers) {
                if(!player.getName().equals(challenger.getName()) /*&& challenger.getBank() > PENALTY*/){
                    if (challenger.shouldChallenge(mainPot, player.getWord())) {
                        challenge(player, challenger, mainPot);
                        break;
                    }
                }
            }
        }
    }


    public void showdown(ArrayList<PotOfMoney> pots) {
        System.out.println("---SHOWDOWN---");

        ArrayList<Player> bestPlayer = new ArrayList<>();
        Player currentPlayer = null;
        int potNum = 0;

        for (PotOfMoney pot : pots) {
            if (pots.size() > 1) {        //if there's more than 1 pot say which pot it is
                System.out.println("---For pot " + potNum + " ---");
            }

            int bestHandScore = 0;
            int score;

            for (int i = 0; i < pot.getNumPlayers(); i++) {

                currentPlayer = pot.getPlayer(i);
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
                Player potWinner = bestPlayer.get(0);
                potWinner.takePot(pot);
            }
            else if (numWinners > 1) {
                for (Player player: bestPlayer) {
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

    /*
     * Sorts the player's in ascending order of stake
     * for each player whose stake is less than the max player's stake
     *		if the player has no stake it is already fully contained in the last pot
     *		otherwise create a new pot with this player and all remaining players (those with greater or equal stake)
     * 		pot total is this players stake * number of remaining players
     * 		remove this player's stake from every other remaining player's stake
     * 		add to list of side pots
     */
    public ArrayList<PotOfMoney> newSidePots(PotOfMoney pot) {
        ArrayList<PotOfMoney> sidePots = new ArrayList<>();
        Collections.sort(pot.getPlayers(), Comparator.comparingInt(Player::getStake));			//sort the players by increasing pot size

        ArrayList<Player> remainingPlayers = new ArrayList<>(pot.getPlayers());
        int foldedMoney = 0;
        for(Player player: pot.getPlayers()){
            if(player.getStake() == 0){				//player has excess stake not already in a pot
                remainingPlayers.remove(player);
                continue;
            }

            if(player.hasFolded()){         //if folded player don't create another pot yet
                foldedMoney += player.getStake();
                remainingPlayers.remove(player);
            } else {                        //new pot with excess since last allIn player
                ArrayList<Player> potPlayers = new ArrayList<>();
                potPlayers.addAll(remainingPlayers);
                PotOfMoney newPot = new PotOfMoney(potPlayers);

                newPot.setTotal(player.getStake() * remainingPlayers.size());
                newPot.addToPot(foldedMoney);
                foldedMoney = 0;

                int potStake = player.getStake();
                for (Player otherPlayer : remainingPlayers) {
                    otherPlayer.reduceStake(potStake);
                }

                sidePots.add(newPot);
                remainingPlayers.remove(player);
            }
        }
        return sidePots;
    }

    public void challenge(Player player, Player challenger, PotOfMoney pot){
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

    private void printPlayerHand() {
        System.out.print("> Your Cards: ");
        for (Tile tile : players[0].getHand().getHand()) {
            System.out.print(tile + " ");
        }
        System.out.println("");
    }

    private void delay(int numMilliseconds) {
        try {
            Thread.sleep(numMilliseconds);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
