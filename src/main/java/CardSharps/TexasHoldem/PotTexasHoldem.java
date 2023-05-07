package CardSharps.TexasHoldem;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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

    public List<Player> getPlayers() {
        return players;
    }

    public List<Player> getActivePlayers(){
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

    public void removeFromPot(int subtraction) {
        addToPot( -subtraction);
    }

    public void setTotal(int amount) {
        clearPot();
        addToPot(amount);
    }

    public int sharePot(int numWinners){
        int remainder = getTotal() % numWinners;        //house takes remainder
        int allPlayerWinnings = getTotal() - remainder;
        return allPlayerWinnings / numWinners;
    }
}
