package CardSharps.TexasScramble;

import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.Scanner;

public class GameOfTexasScramble {
    private Player[] players;
    private BagOfTiles bag;
    private ScrabbleDictionary dictionary;
    private WordFrequencyDictionary wordFrequencyDictionary;
    private int numPlayers;
    private final static int INIT_SMALL_BLIND = 1;

    private final static int SMALL_BLIND_INCREASE_PER_ROUND = 1;
    private final static int MAX_NUM_PLAYERS = 9;

    //--------------------------------------------------------------------//
    //--------------------------------------------------------------------//
    // Constructor
    //--------------------------------------------------------------------//
    //--------------------------------------------------------------------//

    public GameOfTexasScramble(int numPlayers, int bank, String humanName) {
        players = new Player[numPlayers];
        this.numPlayers = numPlayers;

        char language = 0;
        while (language != 'e' && language != 'E' && language != 'f' && language != 'F'){
            Scanner sc = new Scanner(System.in);
            System.out.println("Enter e to play in English" +
                    "\n or F to play in French");
            language = sc.next().charAt(0);
            if(language != 'e' && language != 'E' && language != 'f' && language != 'F'){
                System.out.println("You must enter the character of a valid language. \n");
            }
        }
        bag = bagOfLanguage(language);
        dictionary = getDictionary(language);
        wordFrequencyDictionary = getWordFrequencyDictionary(language);

        //TODO make possible for multiple human players
        CreateComputerPlayers computerPlayers = new CreateComputerPlayers(wordFrequencyDictionary, numPlayers-1 , bank, humanName);
        players[0] = new HumanScramblePlayer(humanName, bank);
        for(int i=1; i < numPlayers; i++){
            players[i] = computerPlayers.getPlayer(i-1);
        }
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
        WordFrequencyDictionary newFrequencies = null;
        try {
            String filename = null;
            if (languageChar == 'e' || languageChar == 'E') {
                filename = "englishWordFrequency.txt";
            } else if (languageChar == 'f' || languageChar == 'F') {
                //filename = "frenchWordFrequency.txt";
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
        return newFrequencies;
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
        for (int i = 0; i < getNumPlayers(); i++) {
            if (getPlayer(i) != null && !getPlayer(i).isBankrupt())
                count++;
        }
        return count;
    }

    public int getNumPlayersMeetBlinds(int bigBlind) {
        // how many players can meet the blind for the upcoming round
        int count = 0;
        for (int i = 0; i < getNumPlayers(); i++) {
            if (getPlayer(i) != null && getPlayer(i).getBank() >= bigBlind)
                count++;
        }
        return count;
    }

    public void removePlayer(int num) {
        if (num >= 0 && num < numPlayers) {
            System.out.println("\n> " + players[num].getName() + " leaves the game.\n");
            players[num] = null;
        }
    }

    //--------------------------------------------------------------------//
    //--------------------------------------------------------------------//
    // Play Poker
    //--------------------------------------------------------------------//
    //--------------------------------------------------------------------//

    public void play()	{
        int smallBlind = INIT_SMALL_BLIND;
        int bigBlind = smallBlind*2;
        int button = 0;
        while (getNumSolventPlayers() > 1) {

            for (int i = 0; i < numPlayers; i++) {                  //remove any player without bank to play round
                if (getPlayer(i) != null) {
                    if (getPlayer(i).getBank() < bigBlind) {
                        removePlayer(i);
                    }
                }
            }

            RoundOfTexasScramble round = new RoundOfTexasScramble(bag, players, smallBlind, button, dictionary);
            round.play();

            smallBlind += SMALL_BLIND_INCREASE_PER_ROUND;
            bigBlind = smallBlind*2;
            button++;
            try {
                if(getNumPlayersMeetBlinds(bigBlind) < 2){
                    System.out.println("The game is over. There is only one player able to meet the big blind of " + bigBlind);
                    for (Player player : players){
                        if (player != null && player.getBank() > bigBlind)
                            System.out.println(player.getName() + " is the WINNER!");
                    }
                    return;
                }

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

        System.out.println("\nWelcome to the Automated Texas Scramble Machine ...\n\n");
        String[] takenNames = {"Tom", "Dick", "Harry", "Sarah", "Maggie", "Andrew", "Zoe", "Sadbh", "Mark", "Sean"};

        String humanName = null;
        while (humanName == null || humanName == "" || Arrays.asList(takenNames).contains(humanName)){
            System.out.print("\nWhat is your name?  ");
            try {
                Scanner scanName = new Scanner(System.in);
                humanName = scanName.nextLine();
                humanName = humanName.replaceAll("\\s", "");
                if(humanName == "" || humanName == null){
                    System.out.println("You must have a name");
                } else if (Arrays.asList(takenNames).contains(humanName)) {
                    System.out.println("The name " + humanName + " is already taken");
                }
            }
            catch (Exception e){e.printStackTrace();}
        }

        int numPlayers = 0;
        while (numPlayers < 2 || numPlayers > MAX_NUM_PLAYERS) {
            System.out.print("\nHow many players should be in the game?  ");
            Scanner scNP = new Scanner(System.in);
            numPlayers = scNP.nextInt();
            if(numPlayers < 2){
                System.out.println("You cannot play on your own! (The maximum number of players is " + MAX_NUM_PLAYERS + ")");
            } else if(numPlayers > MAX_NUM_PLAYERS){
                System.out.println("The maximum number of players for our game of Texas Scrabble is " + MAX_NUM_PLAYERS);
            }
            //scNP.close();
        }

        int startingBank = 10;

        System.out.println("\nLet's play SCRAMBLE ...\n\n");

        GameOfTexasScramble game = new GameOfTexasScramble(numPlayers, startingBank, humanName);

        game.play();
    }
}
