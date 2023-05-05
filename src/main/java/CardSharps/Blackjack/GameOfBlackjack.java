package CardSharps.Blackjack;

import CardSharps.Poker.*;
import CardSharps.TexasHoldem.RoundOfTexasHoldem;

public class GameOfBlackjack { //reference Gameofpoker.java in poker
    private BlackjackPlayer[] players;
    private BlackjackPlayer dealer;
    private BlackjackDeck deck;
    private int numPlayers;

    public GameOfBlackjack(String[] names, int bank){
        numPlayers = names.length;
        players = new BlackjackPlayer[numPlayers];
        for (int i = 0; i < numPlayers; i++){
            if (i == 0){
                players[i] = new BlackjackHumanPlayer(names[i].trim(), bank);
            } else {
                players[i] = new BlackjackComputerPlayer(names[i].trim(), bank);
            }
        }
        dealer = new BlackjackHumanPlayer("Dealer", bank);
        deck = new BlackjackDeck();
    }

    public int getNumPlayers(){
        return numPlayers;
    }

    public void play(){
        while (getNumSolventPlayers() > 1) {
            RoundOfBlackjack round = new RoundOfBlackjack(deck, players, dealer);
            round.play();

            try {
                System.out.print("\n\nPlay another round by pressing ENTER.\nPress 'q' to terminate this game ... ");
                byte[] input = new byte[100];
                System.in.read(input);
                System.in.skip(System.in.available());

                for (int i = 0; i < input.length; i++)
                    if ((char)input[i] == 'q' || (char)input[i] == 'Q')
                        return;
            } catch (Exception e) {
            }
        }
    }

    public BlackjackPlayer getPlayer(int num) {
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


    public static void main(String[] args) {
         String[] names = { "Human", "Tom", "Dick", "Harry"}; //define names
         System.out.println("\nWelcome to the Blackjack ...\n");
         System.out.print("What is your name?: ");
         byte[] input = new byte[100]; //get user input for name
         try {
         System.in.read(input);
         names[0] = new String(input);
          } catch (Exception e) {
          };
          int startingBank = 50;
          System.out.println("Let's play Blackjack ...\n");
          GameOfBlackjack game = new GameOfBlackjack(names, startingBank);
          game.play();
    }
}
