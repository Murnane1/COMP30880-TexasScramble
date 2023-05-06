package CardSharps.TexasScramble;
// This package provides classes necessary for implementing a game system for playing poker


import java.util.ArrayList;

public class PotOfMoney
{
    private int total = 0; // the total amount of money in the table, waiting to be won

    private int stake = 0; // the current highest stake expected
    // of each player to stay in the game

    private ArrayList<Player> players;

    //--------------------------------------------------------------------//
    //--------------------------------------------------------------------//
    // Constructor
    //--------------------------------------------------------------------//
    //--------------------------------------------------------------------//

    public PotOfMoney(ArrayList<Player> players) {
        this.players = players;
    }

    public PotOfMoney() {}

    //--------------------------------------------------------------------//
    //--------------------------------------------------------------------//
    // Display Behaviour
    //--------------------------------------------------------------------//
    //--------------------------------------------------------------------//

    public String toString() {
        return "There is a pot of " + total + " chip(s) on the table";
    }


    //--------------------------------------------------------------------//
    //--------------------------------------------------------------------//
    // Accessors
    //--------------------------------------------------------------------//
    //--------------------------------------------------------------------//

    public int getTotal() {
        return total;
    }
    public int getCurrentStake() {
        return stake;
    }

    public ArrayList<Player> getActivePlayers(){
        ArrayList<Player> activePlayers = new ArrayList<>();
        for (Player player: getPlayers()) {
            if(player == null || player.hasFolded()){
                continue;
            }
            activePlayers.add(player);
        }
        return activePlayers;
    }

    public double getAverageStakeToBankRatio() {
        double commitment = 0;
        for (Player player: getActivePlayers()) {
            commitment += player.getStakeToBankRatio();
        }
        return commitment / getActivePlayers().size();
    }

    //--------------------------------------------------------------------//
    //--------------------------------------------------------------------//
    // Modifiers
    //--------------------------------------------------------------------//
    //--------------------------------------------------------------------//

    public void raiseStake(int addition) {
        stake += addition;
        addToPot(addition);
    }

    public void addToPot(int addition) {
        total += addition;
    }

    public void addBlind(int blindAmt) {
        stake = blindAmt;
        total += blindAmt;
    }

    public void removeFromPot(int subtraction) { total -= subtraction; }
    public void clearPot() {
        total = 0;
    }


    public int takePot() {
        // when the winner of a hand takes the pot as his/her winnings
        int winnings = getTotal();
        clearPot();
        return winnings;
    }

    public int sharePot(int numWinners){
        int remainder = getTotal() % numWinners;        //house takes remainder
        int allPlayerWinnings = getTotal() - remainder;
        int share = allPlayerWinnings / numWinners;
        return share;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getNumPlayers(){
        return players.size();
    }

    public Player getPlayer(int i){
        return players.get(i);
    }

    public ArrayList<Player> getPlayers() {
        return players;
    }

    public void removePlayer(Player player){
        players.remove(player);
    }

    public void removePlayer(int i){
        players.remove(i);
    }

    public void addPlayer(Player player){
        players.add(player);
    }

    public void addPlayers(Player[] newPlayers){
        for(int i = 0 ; i< newPlayers.length;i++){
            players.add(newPlayers[i]);
        }
    }
}