package CardSharps.Blackjack;

import CardSharps.Poker.*;

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
        RoundOfBlackjack round = new RoundOfBlackjack(deck, players, dealer);
        round.play();

        try {
            System.out.print("\n\nPlay another round? Press 'q' to terminate this game ... ");
            byte[] input = new byte[100];
            System.in.read(input);
            for (int i = 0; i < input.length; i++)
                if ((char) input[i] == 'q' || (char) input[i] == 'Q')
                    return;
        } catch (Exception e) {
        };
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
          int startingBank = 10;
          System.out.println("Let's play Blackjack ...\n");
          GameOfBlackjack game = new GameOfBlackjack(names, startingBank);
          game.play();
    }
}
