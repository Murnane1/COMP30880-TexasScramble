package texasScramble;

import java.util.ArrayList;
import java.util.Collections;

public class CreateComputerPlayers {
    private Player[] players;
    private ArrayList<Player> availablePlayers = new ArrayList<>();
    private int bank;
    private WordFrequencyDictionary wordFrequencyDictionary;

    /*
    * Puts a random selection of our predefined players into an array of players
    * Can then use the getPlayers methods to assign the array of players in the game class
    */
    CreateComputerPlayers(WordFrequencyDictionary wordFrequencyDictionary, int numPlayers, int bank, String humanName){
        this.wordFrequencyDictionary = wordFrequencyDictionary;
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
        FrequencyComputerPlayer Tom = new FrequencyComputerPlayer("Tom", bank, 100000, 70, wordFrequencyDictionary);
        FrequencyComputerPlayer Dick = new FrequencyComputerPlayer("Dick", bank, 0, 60, wordFrequencyDictionary);
        FrequencyComputerPlayer Harry = new FrequencyComputerPlayer("Harry", bank, 1, 20, wordFrequencyDictionary);
        FrequencyComputerPlayer Sarah = new FrequencyComputerPlayer("Sarah", bank, 1000, 90, wordFrequencyDictionary);
        FrequencyComputerPlayer Maggie = new FrequencyComputerPlayer("Maggie", bank, 50000, 10, wordFrequencyDictionary);
        FrequencyComputerPlayer Andrew = new FrequencyComputerPlayer("Andrew", bank, 8000, 30, wordFrequencyDictionary);
        FrequencyComputerPlayer Zoe = new FrequencyComputerPlayer("Zoe", bank, 4000, 50, wordFrequencyDictionary);
        FrequencyComputerPlayer Sadbh = new FrequencyComputerPlayer("Sadbh", bank, 8000, 40, wordFrequencyDictionary);
        FrequencyComputerPlayer Mark = new FrequencyComputerPlayer("Mark", bank, 100, 55, wordFrequencyDictionary);
        FrequencyComputerPlayer Sean = new FrequencyComputerPlayer("Sean", bank, 30000, 45, wordFrequencyDictionary);

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
