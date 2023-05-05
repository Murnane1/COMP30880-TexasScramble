package CardSharps.Blackjack;

public class RoundOfBlackjack {
    public static int DELAY_BETWEEN_ACTIONS = 1000; //ms
    private BlackjackPlayer[] players;
    private BlackjackPlayer dealer;
    private static final int DEALERMIN = 17;
    public static final int GOALSCORE = 21;

    private BlackjackDeck deck;
    private int numPlayers;

    public RoundOfBlackjack(BlackjackDeck deck, BlackjackPlayer[] players, BlackjackPlayer dealer){
        this.players = players;
        this.dealer = dealer;
        this.deck = deck;
        numPlayers = players.length;
        deck.reset();
        System.out.println("============New round:============");
        openRound();
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

    public int getNumActivePlayers(){
        int count = 0;
        for (int i = 0; i < getNumPlayers(); i++){
            if (getPlayer(i) != null && !getPlayer(i).isBankrupt()){ //conditions for split>?
                count++;
            }
        }
        return count;
    }

    public void removePlayer(int num){
        if (num >= 0 && num < numPlayers){
            players[num] = null;
        }
    }

    public int getPlayerScore(){
        int score = 0;
        //players and dealer
        for (int i = 0; i < getNumPlayers(); i++){
            BlackjackPlayer currentPlayer = getPlayer(i);
            if (currentPlayer == null){
                continue;
            }
            //check for split.
            score = currentPlayer.getHand(i).getValue();
        }
        return score;
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
        System.out.println("Round has opened!");
        for (int i = 0; i < numPlayers; i++){
            player = getPlayer(i);
            if (player == null || player.isBankrupt()){
                continue;
            }
        }
    }

    public boolean allPlayersDone() {
        for (int i = 0; i < numPlayers; i++) {
            if (!players[i].isBust(0) || !players[i].isStand(0)) {
                return false;
            }
        }
        return true;
    }

    public void play(){
        //1)Each player place bet
        for (int i = 0; i < numPlayers; i++) {
            BlackjackPlayer player = getPlayer(i);
            if (player == null || player.isBankrupt()){
                continue;
            }
            //player selects stake
            player.openBetting(); //every player puts an initial stake
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
            // player.getCard(0,0).toString()
            // dealer.getHand(0).getCard(dealer.getHand(0).getNumCards()-1).getName()
            System.out.println("Dealer HITS and draws " + dealer.getHand(0).getCard(dealer.getHand(0).getNumCards()-1).toString()); //check getCard(0,0) unsure if this right card to display.
            if (dealer.isBust(0)) {
                dealer.setBust(0);
                System.out.println("Dealer is BUST!");
            }
        }

        //7) Winners calculated & winnings added to bank
        int dealerScore = dealer.getHand(0).getHandValue();
        System.out.println("Dealer hand value: " +dealerScore +"\n");
        for (int x = 0; x < numPlayers; x++) {
            for (int j = 0; j < players[x].getNumOfHands(); j++) {
                BlackjackHand playerHand = players[x].getHand(j);
                int playerScore = playerHand.getHandValue();
                players[x].resetStake(j);
                if (players[x].getHand(j).isBlackjack() && dealerScore != 21) { // player has blackjack and dealer
                                                                                // doesn't
                    players[x].addBank((2 * players[x].getStake(j))); // pay x2
                    System.out.println("Congratulations, " + players[x].getName() + " you WON with BLACKJACK and get "
                            +(2 * players[x].getStake(j)) + " chips!");
                } else if (players[x].getHand(j).isBlackjack() && dealerScore == 21) { // both have blackjack (draw
                                                                                       // condition)
                    System.out.println("It's a DRAW, " + players[x].getName() + " you get your stake of "
                            + players[x].getStake(j) + " chips back!");
                } else if (dealerScore > 21 && !players[x].isBust(j)) { // dealer busts
                    players[x].addBank(2*players[x].getStake(j)); // player wins
                    System.out.println("Congratulations, " + players[x].getName() + " you WON "
                            + 2*players[x].getStake(j) + " chips!");
                } else if (dealerScore < playerScore && !players[x].isBust(j)) { // player has higher score than dealer
                    players[x].addBank(2 * players[x].getStake(j)); // player wins
                    System.out.println("Congratulations, " + players[x].getName() + " you WON "
                            + 2*players[x].getStake(j) + " chips!");
                } else if (dealerScore == playerScore && !players[x].isBust(j)) { // draw
                    players[x].addBank(players[x].getStake(j)); // player gets the stake back
                    System.out.println("It's a DRAW " + players[x].getName() + " you get your stake of "
                            + players[x].getStake(j) + " chips back!");
                } else { // player loses
                    System.out.println(players[x].getName() + " you LOST!");
                }
            }
            System.out.println("Your total bank is " + players[x].getBank() + " chips!\n");
        }
    }

    private void delay(int numMilliseconds) {
        try {
            Thread.sleep(numMilliseconds);
        } catch (Exception e) {}
    }
}
