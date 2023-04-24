package texasScramble;


import java.util.*;

public class RoundOfTexasScramble {
    public static final int DELAY_BETWEEN_ACTIONS = 1000;  // number of milliseconds between game actions
    private static final int PENALTY = 3;
    private Player[] players;
    private BagOfTiles bag;
    private Dictionary dict;    //TODO create word dictionary
    private int numPlayers;
    private int button      = 0;
    private int smallBlind  = 1;
    private int bigBlind    = 2;


    public RoundOfTexasScramble(BagOfTiles bag, Player[] players, int smallBlind, int button) {
        this.players = players; //init players
        this.bag = bag; //init deck
        this.smallBlind = smallBlind;
        this.bigBlind = 2 * smallBlind;
        numPlayers = players.length; //get totalPlayers

        this.button = button;

        System.out.println("\n\nNew Deal:\n\n");
        deal();
    }

    public int getNumPlayers() {
        return numPlayers;

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
                if (getPlayer(index).isBankrupt())
                    removePlayer(index);
                else {

                    getPlayer(index).reset();
                    getPlayer(index).dealTo(bag);

                    System.out.println(getPlayer(index));
                }
            }
        }

        System.out.println("\n");
    }

    public void canOpen(PotOfMoney pot) {
        if (numPlayers <= 1) {
            return;
        }
        int i = 1;
        //Player to the left of the dealer posts the small blind
        while (players[(button + i) % numPlayers] != null && players[(button + i) % numPlayers].getBank() < bigBlind) {
            if (numPlayers <= 1) {
                System.exit(0);
                ;
                return;
            }
            //Player does not have enough chips to open
            System.out.println(players[(button + i) % numPlayers].getName() + "says: I cannot post the Small Blind. \n" + "I can't afford to play anymore");

            removePlayer((button + i) % numPlayers);
            pot.removePlayer(players[(button + i) % numPlayers]);
            i++;

        }
        players[(button + i) % numPlayers].postBlind(pot, smallBlind, "Small Blind");

        if (numPlayers <= 1) {
            return;
        }
        //Player to the left of the small blind posts the big blind
        while (players[(button + i + 1) % numPlayers] != null && players[(button + i + 1) % numPlayers].getBank() < bigBlind) {
            if (numPlayers <= 1) {
                System.exit(0);
                ;


                return;
            }
            //Player does not have enough chips to open
            System.out.println(players[(button + i + 1) % numPlayers].getName() + "says: I cannot post the Big Blind. . \n " + "I can't afford to play anymore");

            removePlayer((button + i + 1) % numPlayers);
            pot.removePlayer(players[(button + i) % numPlayers]);

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
            players[i].getHand().addCommunityTiles(list);
        }
    }

    public void play() {
        ArrayList<PotOfMoney> pots = new ArrayList<PotOfMoney>();

        ArrayList<Player> listPlayers = new ArrayList<>(Arrays.asList(players));
        PotOfMoney mainPot = new PotOfMoney(listPlayers);
        pots.add(mainPot);

        // Initialize bank and print the values for each player;
        Integer numActive = mainPot.getNumPlayers();
        Integer stake = -1;
        bag.reset();


        //roundOpen(mainPot, players[(button+1)%numPlayers], players[(button+2)%numPlayers]);
        canOpen(mainPot);
        //PRINTING PLAYER HAND
        printPlayerHand();

        // Game actions
        // (call, raise, fold);
        // Start betting sequence left of the big blind;
        preflop(mainPot);

        printPlayerHand();
        //Whilst there are >= 2 players are still active;
        flop(mainPot);

        printPlayerHand();
        // Turn 4th community card (turn) is turned while there are >= 2 players active.
        turn(mainPot);

        printPlayerHand();
        // Turn 5th community card (river) is turned if there are still >= 2 players active.
        river(mainPot);

        printPlayerHand();
        declareWords(mainPot);

        pots = newSidePots(mainPot);
        showdown(pots);
    }

    private void preflop(PotOfMoney mainPot) {

        System.out.println("---PREFLOP---");


        int playerStart = button + 3;    //3 becouse player left to big blind starts

        bettingCycle(mainPot, playerStart);
    }

    private void flop(PotOfMoney mainPot) {

        System.out.println("---FLOP---");

        // Turn 3 community (flop) cards
        dealCommunity(3);

        int playerStart = button + 1;    //3 becouse player left to big blind starts


        bettingCycle(mainPot, playerStart);

    }

    private void turn(PotOfMoney mainPot) {

        System.out.println("---TURN---");


        // Deal the turn
        dealCommunity(1);

        int playerStart = button + 1;    //3 becouse player left to big blind starts

        bettingCycle(mainPot, playerStart);
    }

    private void river(PotOfMoney mainPot) {

        System.out.println("---RIVER---");

        // Deal the river
        dealCommunity(1);

        int playerStart = button + 1;    //3 becouse player left to big blind starts

        bettingCycle(mainPot, playerStart);
    }

    private void declareWords(PotOfMoney mainPot) {

        System.out.println("---WORD REVEAL---");

        for (Player player: players) {
            player.chooseWord();
        }

        //for each player's word all other players have the option to challenge it
        for (Player player: players) {
            System.out.println(player.getName() + "'s word is " + player.getWord());
            for (Player challenger : players) {
                if (challenger.shouldChallenge(mainPot, player.getWord()) && player != challenger) {
                    challenge(player, challenger, mainPot);
                }
            }
        }
    }

    private void showdown(ArrayList<PotOfMoney> pots) {

        System.out.println("---SHOWDOWN---");

        int bestHandScore = 0, score = 0, bestPos = 0, potNum = 0;
        Player bestPlayer = null, currentPlayer = null;

        for (PotOfMoney pot : pots) {

            if (pots.size() > 1) {        //if there's more than 1 pot say which pot it is
                System.out.println("---For pot " + potNum + " ---");
            }


            bestPlayer = pot.getPlayer(0);
            for (int i = 0; i < pot.getNumPlayers(); i++) {
                currentPlayer = pot.getPlayer(i);
                if (currentPlayer.getName() == null || currentPlayer.hasFolded()) {
                    continue;
                }
                System.out.println("Player " + currentPlayer.getName() + "'s hand: " + currentPlayer.getHand().getBestHand());
                score = currentPlayer.getHand().getBestHandValue();
                if (score > bestHandScore) {
                    bestHandScore = score;
                    bestPlayer = currentPlayer;
                }
            }
            System.out.println(bestPlayer.getName() + " takes pot of " + pot.getTotal() + " chips!");

            bestPlayer.takePot(pot);
            potNum++;
        }
    }

    public void bettingCycle(PotOfMoney mainPot, int playerStart) {
        int indexCurrPot = 0;
        int stake = -1;
        int numActive = mainPot.getNumPlayers();

        while (stake < mainPot.getCurrentStake() && numActive > 1) {

            stake = mainPot.getCurrentStake();

            for (int i = 0; i < getNumPlayers(); i++) {
                Player currentPlayer = mainPot.getPlayer((playerStart + i) % mainPot.getNumPlayers());

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
     * Sorts the player's in ascending order of stake
     * for each player who's stake is less than the max player's stake
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
        for(Player player: pot.getPlayers()){

            if(player.getStake() == 0){				//player has excess stake not already in a pot
                remainingPlayers.remove(player);
                continue;
            }

            //new pot with remaining players
            ArrayList<Player> potPlayers = new ArrayList<>();
            potPlayers.addAll(remainingPlayers);
            PotOfMoney newPot = new PotOfMoney(potPlayers);

            newPot.setTotal(player.getStake()*remainingPlayers.size());

            int potStake = player.getStake();
            for(Player otherPlayer: remainingPlayers){
                otherPlayer.reduceStake(potStake);
            }

            sidePots.add(newPot);
            remainingPlayers.remove(player);
        }

        return sidePots;
    }

    //should this be here or in player?
    public void challenge(Player player, Player challenger, PotOfMoney pot){
        /*if(dict.contains(player.getWord())){        //if word valid - challenger loses penalty cost (to opposition or pot?)
            challenger.penalty(PENALTY, pot);
        } else {                        //if word invalid - player's score for round is 0
            player.getHand().setBestHandValueToZero();
        }*/
    }

    private void printPlayerHand() {

        System.out.println(">Your Cards : ");
        for (Tile tile : players[0].getHand().getHand()) {
            System.out.print(tile + " ");
        }
        System.out.println("");
    }

    private void delay(int numMilliseconds) {
        try {
            Thread.sleep(numMilliseconds);
        } catch (Exception e) {
        }
    }
}
