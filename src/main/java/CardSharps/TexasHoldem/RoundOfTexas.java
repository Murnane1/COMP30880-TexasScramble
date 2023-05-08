package CardSharps.TexasHoldem;

import CardSharps.TexasScramble.ScramblePlayer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public abstract class RoundOfTexas {
    public static final int DELAY_BETWEEN_ACTIONS	=	1000;  // number of milliseconds between game actions
    private Player[] players;
    private int numPlayers;
    private int button = 0;     //Player starts as the dealer;
    private int smallBlind = 1;
    private int bigBlind = 2;

    public RoundOfTexas(Player[] players, int smallBlind, int button) {
        this.smallBlind = smallBlind;
        this.bigBlind = 2*smallBlind;
        this.players = players;
        numPlayers   = players.length; //get totalPlayers
        this.button = button;

        System.out.println("\n\nPlayer Stacks:\n");
        for (Player player: players) {
            if(player != null) {
                System.out.println(player.getName() + " has " + player.addCount(player.getBank(), "chip", "chips") + " in the bank");
            }
        }

        System.out.println("\n\nNew Deal:\n\n");
    }




    //--------------------------------------------------------------------//
    // Accessors
    //--------------------------------------------------------------------//


    public Player[] getPlayers() {
        return players;
    }

    public int getButton() {
        return button;
    }

    public int getSmallBlind() {
        return smallBlind;
    }

    public int getBigBlind() {
        return bigBlind;
    }

    public int getNumPlayers() {
        return numPlayers;

    }

    public ArrayList<Player> getActivePlayers(List<Player> players){
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
        if (num >= 0 && num < numPlayers) {
            System.out.println("\n> " + players[num].getName() + " leaves the game.\n");

            Player[] newPlayers = new ScramblePlayer[players.length - 1];
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

    public void openBlinds(PotTexasHoldem pot) {
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

    public void bettingCycle(PotTexasHoldem mainPot, int playerStart) {
        int numActive = mainPot.getNumPlayers();
        for (Player player : mainPot.getPlayers()) {
            if (player == null || player.hasFolded() || player.isAllIn()) {
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

                if (numActive == 1) {
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


    /*
     * Sorts the player's in ascending order of stake
     * for each player whose stake is less than the max player's stake
     *		if the player has no stake it is already fully contained in the last pot
     *		otherwise create a new pot with this player and all remaining players (those with greater or equal stake)
     * 		pot total is this players stake * number of remaining players
     * 		remove this player's stake from every other remaining player's stake
     * 		add to list of side pots
     */
    public ArrayList<PotTexasHoldem> newSidePots(PotTexasHoldem pot) {
        ArrayList<PotTexasHoldem> sidePots = new ArrayList<>();
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
                PotTexasHoldem newPot = new PotTexasHoldem(potPlayers);

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

    private void delay(int numMilliseconds) {
        try {
            Thread.sleep(numMilliseconds);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
