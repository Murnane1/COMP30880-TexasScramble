package CardSharps.TexasHoldem;
import java.util.ArrayList;

import CardSharps.Poker.PotOfMoney;

public class PotTexasHoldem extends PotOfMoney{
    
    private ArrayList<Player> players;
    int maxStake = Integer.MAX_VALUE;       //no max until a player goes all-in
    
    public PotTexasHoldem(ArrayList<Player> potPlayers){
        super();
        players = potPlayers;
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

    public void addPlayer(Player player){
        players.add(player);
    }

    public void removePlayer(Player player){
        players.remove(player);
    }

    public void removePlayer(int i){
        players.remove(i);
    }

    public void addPlayers(Player[] newPlayers){
        for(int i = 0 ; i< newPlayers.length;i++){
            players.add(newPlayers[i]);
        }
    }

    public void removeFromPot(int subtraction) {
        addToPot( -subtraction);
    }

    public void setTotal(int amount) {
        clearPot();
        addToPot(amount);
    }


    public int getMaxStake() {
        return maxStake;
    }

    public void setMaxStake(int maxStake) {
        this.maxStake = maxStake;
    }

    public void newPotStake(int stake) {
        raiseStake(stake);
        removeFromPot(stake);
    }
}
