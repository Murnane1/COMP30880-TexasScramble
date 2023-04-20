package texasScramble;


/*
 * TAKE IN LANGUAGE PASS IT TO BAG
 */
public class GameOfTexasScramble {
    private Player[] players;

    private BagOfTiles bag;


    private int numPlayers;

    private final static int INIT_SMALL_BLIND = 1;

    //--------------------------------------------------------------------//
    //--------------------------------------------------------------------//
    // Constructor
    //--------------------------------------------------------------------//
    //--------------------------------------------------------------------//

    public GameOfTexasScramble(String[] names, int bank) {
        numPlayers = names.length;
        players = new Player[numPlayers];

        //TODO make possible for multiple human players
        for (int i = 0; i < numPlayers; i++)
            if (i == 0)
                players[i] = new HumanScramblePlayer(names[i].trim(), bank);
            else
                players[i] = new ComputerScramblePlayer(names[i].trim(), bank);
        
        bag = new BagOfTiles();
    }


    //--------------------------------------------------------------------//
    //--------------------------------------------------------------------//
    // Accessors
    //--------------------------------------------------------------------//
    //--------------------------------------------------------------------//

    public int getNumPlayers() {
        return numPlayers;

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
            RoundOfTexasScramble round = new RoundOfTexasScramble(bag, players, smallBlind, button);

            round.play();

            smallBlind++;
            button++;
            try {
                System.out.print("\n\nPlay another round? Press 'q' to terminate this game ... ");

                byte[] input = new byte[100];

                System.in.read(input);

                for (int i = 0; i < input.length; i++)
                    if ((char)input[i] == 'q' || (char)input[i] == 'Q')
                        return;
            }
            catch (Exception e) {};
        }
    }


    //--------------------------------------------------------------------//
    //--------------------------------------------------------------------//
    // Launcher for application
    //--------------------------------------------------------------------//
    //--------------------------------------------------------------------//

    public static void main(String[] args) {
        String[] names = {"Human", "Tom", "Dick", "Harry"};

        System.out.println("\nWelcome to the Automated Texas Hold'em Machine ...\n\n");

        System.out.print("\nWhat is your name?  ");

        byte[] input = new byte[100];

        try {
            System.in.read(input);

            names[0] = new String(input);
        }
        catch (Exception e){};

        int startingBank = 10;

        System.out.println("\nLet's play POKER ...\n\n");

        GameOfTexasScramble game = new GameOfTexasScramble(names, startingBank);

        game.play();
    }
}
