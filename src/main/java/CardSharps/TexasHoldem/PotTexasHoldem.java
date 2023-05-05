package CardSharps.TexasHoldem;
import java.util.ArrayList;

import CardSharps.Poker.PotOfMoney;

public class PotTexasHoldem extends PotOfMoney{
    
    private ArrayList<PlayerInterface> players;
    int maxStake = Integer.MAX_VALUE;       //no max until a player goes all-in
    
    public PotTexasHoldem(ArrayList<PlayerInterface> potPlayers){
        super();
        players = potPlayers;
    }
    
    public int getNumPlayers(){
        return players.size();
    }

    public PlayerInterface getPlayer(int i){
        return players.get(i);
    }

    public ArrayList<PlayerInterface> getPlayers() {
        return players;
    }

    public void addPlayer(PlayerInterface player){
        players.add(player);
    }

    public void removePlayer(PlayerInterface player){
        players.remove(player);
    }

    public void removePlayer(int i){
        players.remove(i);
    }

    public void addPlayers(PlayerInterface[] newPlayers){
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
