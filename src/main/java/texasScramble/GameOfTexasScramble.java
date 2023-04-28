package texasScramble;

import java.io.IOException;
import java.net.URL;
import java.util.Scanner;

public class GameOfTexasScramble {
    private Player[] players;
    private BagOfTiles bag;
    private ScrabbleDictionary dictionary;
    private WordFrequencyDictionary wordFrequencyDictionary;
    private int numPlayers;
    private final static int INIT_SMALL_BLIND = 1;

    private final static int SMALL_BLIND_INCREASE_PER_ROUND = 1;

    //--------------------------------------------------------------------//
    //--------------------------------------------------------------------//
    // Constructor
    //--------------------------------------------------------------------//
    //--------------------------------------------------------------------//

    public GameOfTexasScramble(String[] names, int bank) {
        numPlayers = names.length;
        players = new Player[numPlayers];

        //TODO make possible for multiple human players
        for (int i = 0; i < numPlayers; i++) {
            if (i == 0){
                players[i] = new HumanScramblePlayer(names[i].trim(), bank);
            }
            else {
                players[i] = new FrequencyComputerPlayer(names[i].trim(), bank, 0, 100, wordFrequencyDictionary);
            }
        }

        Scanner sc = new Scanner(System.in);
        System.out.println("Enter e to play in English" +
                "\n or F to play in French");
        char language = sc.next().charAt(0);
        bag = bagOfLanguage(language);
        dictionary = getDictionary(language);
        wordFrequencyDictionary = getWordFrequencyDictionary(language);
    }


    //--------------------------------------------------------------------//
    //--------------------------------------------------------------------//
    // Accessors
    //--------------------------------------------------------------------//
    //--------------------------------------------------------------------//

    public BagOfTiles bagOfLanguage(char languageChar) {
        BagOfTiles newBag = null;
        try {
            if (languageChar == 'e' || languageChar == 'E') {
                newBag = new BagOfTiles("ENGLISH");
            } else if (languageChar == 'f' || languageChar == 'F') {
                newBag = new BagOfTiles("FRENCH");
            }
        }
        catch (Exception e){System.out.println("Error selecting language");};

        return newBag;
    }

    public ScrabbleDictionary getDictionary(char languageChar){
        ScrabbleDictionary newDict = null;
        try {
            String filename = null;
            if (languageChar == 'e' || languageChar == 'E') {
                filename = "usEnglishScrabbleWordlist.txt";
            } else if (languageChar == 'f' || languageChar == 'F') {
                filename = "FrenchScrabbleWordlist.txt";
            }
            URL url = ScrabbleDictionary.class.getResource("/WordLists/" + filename);
            String filepath = url.getPath();
            newDict = new ScrabbleDictionary(filepath);
        }
        catch (Exception e){
            System.out.println("Error selecting dictionary");
            e.printStackTrace();
        }

        return newDict;
    }

    public WordFrequencyDictionary getWordFrequencyDictionary(char languageChar) {
        WordFrequencyDictionary newFrequencies;
        try {
            String filename = null;
            if (languageChar == 'e' || languageChar == 'E') {
                filename = "englishWordFrequency.txt";
            } else if (languageChar == 'f' || languageChar == 'F') {
                //filename = "FrenchScrabbleWordlist.txt";
                System.out.println("FRENCH FREQUENCIES NOT AVAILABLE");     //TODO find french frequencies
            }
            URL url = ScrabbleDictionary.class.getResource("/WordFrequencies/" + filename);
            String filepath = url.getPath();
            newFrequencies = new WordFrequencyDictionary(filepath);
        }
        catch (Exception e){
            System.out.println("Error selecting word frequency");
            e.printStackTrace();
        }
        return null;
    }

    public int getNumPlayers() {
        return numPlayers;

    }

    public int getSmallBlindIncreasePerRound(){
        return SMALL_BLIND_INCREASE_PER_ROUND;
    }


    public Player getPlayer(int num) {
        if (num >= 0 && num <= numPlayers)
            return players[num];
        else
            return null;
    }


    public int getNumSolventPlayers() {
        // how many players still have money left?

        int count = 0;

        for (int i = 0; i < getNumPlayers(); i++)
            if (getPlayer(i) != null && !getPlayer(i).isBankrupt())
                count++;

        return count;
    }


    //--------------------------------------------------------------------//
    //--------------------------------------------------------------------//
    // Play Poker
    //--------------------------------------------------------------------//
    //--------------------------------------------------------------------//

    public void play()	{
        int smallBlind = INIT_SMALL_BLIND;
        int button = 0;
        while (getNumSolventPlayers() > 1) {
            RoundOfTexasScramble round = new RoundOfTexasScramble(bag, players, smallBlind, button, dictionary);

            round.play();

            smallBlind += SMALL_BLIND_INCREASE_PER_ROUND;
            button++;
            try {
                System.out.print("\n\nPlay another round? Press 'q' to terminate this game ... ");

                byte[] input = new byte[100];

                System.in.read(input);

                for (int i = 0; i < input.length; i++)
                    if ((char)input[i] == 'q' || (char)input[i] == 'Q')
                        return;
            }
            catch (Exception e) {e.printStackTrace();};
        }
    }


    //--------------------------------------------------------------------//
    //--------------------------------------------------------------------//
    // Launcher for application
    //--------------------------------------------------------------------//
    //--------------------------------------------------------------------//

    public static void main(String[] args) {
        String[] names = {"Human", "Tom", "Dick", "Harry"};

        System.out.println("\nWelcome to the Automated Texas Scramble Machine ...\n\n");

        System.out.print("\nWhat is your name?  ");

        byte[] input = new byte[100];

        try {
            System.in.read(input);

            names[0] = new String(input);
        }
        catch (Exception e){};

        int startingBank = 10;

        System.out.println("\nLet's play SCRAMBLE ...\n\n");

        GameOfTexasScramble game = new GameOfTexasScramble(names, startingBank);

        game.play();
    }
}
