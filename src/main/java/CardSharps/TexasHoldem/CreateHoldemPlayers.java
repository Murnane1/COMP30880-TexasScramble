package CardSharps.TexasHoldem;

import java.util.ArrayList;
import java.util.Collections;

public class CreateHoldemPlayers {
    private final Player[] players;
    private final ArrayList<Player> availablePlayers = new ArrayList<>();
    private int bank;
    /*
     * Puts a random selection of our predefined players into an array of players
     * Can then use the getPlayers methods to assign the array of players in the game class
     */
    CreateHoldemPlayers(int numPlayers, int bank, String humanName){
        this.bank = bank;
        players = new Player[numPlayers];

        generatePlayers(bank);

        Collections.shuffle(availablePlayers);

        for (int i = 0; i < numPlayers; i++) {
            players[i] = availablePlayers.get(i);
        }
    }

    //full list of possible computer players a human can face
    private void generatePlayers(int bank) {
        ComputerHoldemPlayer Tom = new ComputerHoldemPlayer("Tom", bank, 70);
        ComputerHoldemPlayer Dick = new ComputerHoldemPlayer("Dick", bank, 60);
        ComputerHoldemPlayer Harry = new ComputerHoldemPlayer("Harry", bank,  20);
        ComputerHoldemPlayer Sarah = new ComputerHoldemPlayer("Sarah", bank,  90);
        ComputerHoldemPlayer Maggie = new ComputerHoldemPlayer("Maggie", bank,  10);
        ComputerHoldemPlayer Andrew = new ComputerHoldemPlayer("Andrew", bank, 30);
        ComputerHoldemPlayer Zoe = new ComputerHoldemPlayer("Zoe", bank,  50);
        ComputerHoldemPlayer Sadbh = new ComputerHoldemPlayer("Sadbh", bank, 40);
        ComputerHoldemPlayer Mark = new ComputerHoldemPlayer("Mark", bank,  55);
        ComputerHoldemPlayer Sean = new ComputerHoldemPlayer("Sean", bank,  45);

        availablePlayers.add(Tom);
        availablePlayers.add(Dick);
        availablePlayers.add(Harry);
        availablePlayers.add(Sarah);
        availablePlayers.add(Maggie);
        availablePlayers.add(Andrew);
        availablePlayers.add(Zoe);
        availablePlayers.add(Sadbh);
        availablePlayers.add(Mark);
        availablePlayers.add(Sean);
    }


    public Player[] getPlayers() {
        return players;
    }

    public Player getPlayer(int index){
        return players[index];
    }
}
