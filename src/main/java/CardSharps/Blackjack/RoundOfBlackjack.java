package CardSharps.Blackjack;

import static java.lang.System.exit;

public class RoundOfBlackjack {
    private BlackjackPlayer[] players;
    private BlackjackPlayer dealer;
    private static final int DEALERMIN = 17;

    private BlackjackDeck deck;
    private int numPlayers;

    public RoundOfBlackjack(BlackjackDeck deck, BlackjackPlayer[] players, BlackjackPlayer dealer){
        this.players = players;
        this.dealer = dealer;
        this.deck = deck;
        numPlayers = players.length;
        deck.reset();
        openRound();
        System.out.println("============New round:============");
        deal();
    }

    public int getNumPlayers(){
        return numPlayers;
    }

    public BlackjackPlayer getPlayer(int num){
        if (num >= 0 && num <= numPlayers)
            return players[num];
        else
            return null;
    }

    public void removePlayer(int num){
        if (num >= 0 && num < numPlayers){
            players[num] = null;
        }
    }

    public void deal(){
        for (int i = 0; i < getNumPlayers(); i++){
            if (getPlayer(i) != null){
                if (getPlayer(i).isBankrupt()){
                    removePlayer(i);
                } else {
                    getPlayer(i).reset();
                    getPlayer(i).dealTo(deck, i);
                    System.out.println(getPlayer(i) + " =\t" + getPlayer(i).getName());
                }
            }
        }
        System.out.print("\n");
    }

    public void openRound(){
        BlackjackPlayer player = null;
        for (int i = 0; i < numPlayers; i++){
            player = getPlayer(i);
            if (player == null || player.isBankrupt()){
                System.out.println("Ran out of money! Thank you for playing.");
                exit(1);
            }
        }
        System.out.println("Round has opened!");
    }

    public void play(){
        //1)Each player place bet
        for (int i = 0; i < numPlayers; i++) {
            BlackjackPlayer player = getPlayer(i);
            if (player == null || player.isBankrupt()){
                continue;
            }
            if (player instanceof BlackjackHumanPlayer) {
                //player selects stake
                player.openBettingHuman(); //every human player puts an initial stake
            } else if (player instanceof BlackjackComputerPlayer){
                player.openBettingAI();
            }
        }
        System.out.print("\n");

        //2)Each player gets 2 cards face up
        for (BlackjackPlayer player: players) {
            if (player == null || player.isBankrupt()){
                continue;
            }
            player.dealTo(deck, player.getStake(0));
            System.out.println(player.getName() + " has:\t" + player.getCard(0,0).toString()
                    + " & " + player.getCard(0,1).toString() + "\n = " + player.getHand(0).getHandValue());
        }

        //3)Dealer gets two cards
        dealer.dealTo(deck, 0);
        System.out.print("\nThe Dealers initial card is : " + dealer.getCard(0,0).toString() + "\n" + "= " + dealer.getHand(0).getCard(0).getValue() + "\n");

        int count = 0;
        while (count < numPlayers) {
            for (int i = 0; i < players[count].getNumOfHands(); i++) {
                while (!players[count].isBust(i) && !players[count].isStand(i)) {
                    players[count].nextAction(deck, i, dealer.getCard(0, 0));
                }
                System.out.println("\n");
            }
            count++;
        }

        //5) Dealer turns over hole card
        System.out.println("Dealer's hole card is : " + dealer.getCard(0,1).toString() + "\n" + "Dealers hand value = "
                + dealer.getHand(0).getHandValue() + "\n");

        //6) Dealer must hit until >= 17
        while (dealer.getHand(0).getHandValue() < DEALERMIN && !dealer.isBust(0)) {
            dealer.hit(deck, 0);
            System.out.println("Dealer HITS and draws " + dealer.getHand(0).getCard(dealer.getHand(0).getNumCards()-1).toString()); //check getCard(0,0) unsure if this right card to display.
            if (dealer.isBust(0)) {
                dealer.setBust(0);
                System.out.println("Dealer is BUST!");
            }
        }

        //7) Winners calculated & winnings added to bank
        int dealerScore = dealer.getHand(0).getHandValue();
        System.out.println("Dealer hand value: " + dealerScore + "\n");
        for (int x = 0; x < numPlayers; x++) {
            for (int j = 0; j < players[x].getNumOfHands(); j++) {
                BlackjackHand playerHand = players[x].getHand(j);
                int playerScore = playerHand.getHandValue();
                if (playerHand.isBlackjack() && !dealer.getHand(0).isBlackjack()) { // player has blackjack and dealer doesn't
                    players[x].bank += (2 * players[x].getStake(j));
                    System.out.println("Congratulations, " + players[x].getName() + " you WON with BLACKJACK and get " + (2 * players[x].getStake(j)) + " chips!");
                    break;
                } else if (playerHand.isBlackjack() && dealer.getHand(0).isBlackjack()) { // both have blackjack (draw condition)
                    players[x].bank += (players[x].getStake(j));; // player gets the stake back
                    System.out.println("It's a DRAW, " + players[x].getName() + " you get your stake of " + players[x].getStake(j) + " chips back! (Draw as both dealer and player have blackjack)");
                    break;
                } else if (dealerScore > 21 && !playerHand.isBust()) { // dealer busts
                    players[x].bank += (2*players[x].getStake(j)); // player wins
                    System.out.println("Congratulations, " + players[x].getName() + " you WON " + (players[x].getStake(j)) + " chips! (dealer busts and player is not bust)");
                    break;
                } else if (dealerScore < playerScore && !playerHand.isBust()) { // player has higher score than dealer
                    players[x].bank += (2*players[x].getStake(j));// player wins
                    System.out.println("Congratulations, " + players[x].getName() + " you WON " + (players[x].getStake(j)) + " chips! (player has higher score than dealer and is not bust");
                    break;
                } else if (dealerScore == playerScore && !playerHand.isBust()) { // draw
                    players[x].bank += (2*players[x].getStake(j)); // player gets the stake back
                    System.out.println("It's a DRAW " + players[x].getName() + " you get your stake of " + players[x].getStake(j) + " chips back!");
                    break;
                } else { // player loses
                    System.out.println(players[x].getName() + " you LOST!");
                }
            }
            System.out.println("Your total bank is " + players[x].bank + " chips!\n");
        }
    }
}
