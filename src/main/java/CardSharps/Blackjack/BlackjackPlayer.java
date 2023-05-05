package CardSharps.Blackjack;

import java.util.Arrays;
import CardSharps.Poker.*;
import CardSharps.Blackjack.BlackjackHumanPlayer;
import java.util.ArrayList;
import java.util.List;

abstract class BlackjackPlayer { //reference player.java, humanplayer.java and computerplayer.java
    private int bank = 0; //total money player has left (without stake)
    
    private BlackjackHand[] hand = new BlackjackHand[4]; //hands dealt to player
    private int[] stake = new int[4] ; //stakes of hands 
    private boolean[] stand = new boolean[4]; //flag for each hand
    private boolean[] split = new boolean[4];
    private boolean[] bust = new boolean[4]; //flag for bust
    private boolean[] won = new boolean[4]; //flag for winning hand
    private boolean[] draw = new boolean[4]; //flag for draw
    
    private int numOfHands = 1; 
    private String name = ""; //UID for player name

    public BlackjackPlayer(String name, int money){
        this.name = name;
        this.bank = money;

        reset();
    }

    public void reset(){
        Arrays.fill(this.stake, 0);
        Arrays.fill(bust,  false);
        Arrays.fill(stand, false);
        Arrays.fill(split, false);
        hand =  new BlackjackHand[4];
    }

    //getters
    public int getNumOfHands() {
        return numOfHands;
    }

    public BlackjackHand getHand(int handIndex){
        return hand[handIndex];
    }

    public Card getCard(int handIndex, int index){
        return hand[handIndex].getCard(index);
    }

    public int getBank(){
        return bank;
    }

    public int getStake(int handIndex){
        return stake[handIndex];
    }

    public void resetStake(int handIndex) {
        for (int i = 0; i < stake.length; i++) {
            stake[handIndex] = 1;
        }
    }

    public String getName(){
        return name;
    }
    public void setBust(int handIndex){
        bust[handIndex] = true;
    }
    public boolean isBankrupt(){
        return bank == 0;
    }

    public boolean isBust(int handIndex){ //If go over 21
        return bust[handIndex];
    }

    public boolean isStand(int handIndex){ //player stands
        return stand[handIndex];
    }

    public boolean isSplit(int handIndex){ return split[handIndex];}

    public boolean isWon(int handIndex){ //condition for win.
        return won[handIndex];
    }

    public boolean isDraw(int handIndex){
        return draw[handIndex];
    }

    //setters
    public void dealTo(BlackjackDeck deck, int originalStake){
        //Only Give 2 Cards when first dealt a hand
        if(originalStake>bank || isBankrupt()){
            //Need to do error handeling TODO
        }

        hand[0] = deck.dealHand();
        stake[0] = originalStake;
    }


    public void addBank(int addition){
        bank += addition;
    }

    public boolean hit(BlackjackDeck deck, int handIndex){
        //implement hit functionality
        System.out.println("> " + getName() + " says: HIT!");
        hand[handIndex].addCard(deck.dealNext());
        System.out.println(hand[handIndex]);
        return true;
    }

    public void stand(int handIndex){
        //nothing
        if(!stand[handIndex]){
            stand[handIndex] = true;
            System.out.println("> " + getName() + " says: STAND!");
        }
    }


    public void doubleDown(BlackjackDeck deck, int handIndex){
        if (bank < stake[handIndex]) {
            System.out.println("> " + getName() + " says: I can't afford to DOUBLE DOWN!");
            return;
        }

        bank -= stake[handIndex];
        stake[handIndex] *= 2;

        System.out.println("> " + getName() + " says: DOUBLE DOWN!");
        hand[handIndex].addCard(deck.dealNext());
        if (hand[handIndex].getHandValue() > 21){
            System.out.println("> " + getName() + " says: I BUST!");
            bust[handIndex] = true;
        } else {
            stand(handIndex);
        }
        System.out.println(hand[handIndex]);
    }

    public boolean canSplit(int handIndex) {
        return hand[handIndex].isSplit() && bank >= stake[handIndex];
    }

    public boolean canDouble(int handIndex) {
        return !hand[handIndex].isBust() && hand[handIndex].getCards().length == 2 && bank >= stake[handIndex];
    }
   

    public boolean split(BlackjackDeck deck, int handIndex){
        if (!canSplit(handIndex)){
            return false;
        }
        bank -= stake[handIndex];

        Card card = hand[handIndex].getCards()[1];
        
        hand[handIndex].setCard(1, deck.dealNext());   //Set second card of hirst hand to null
        hand[numOfHands] = new BlackjackHand(new Card[]{card, deck.dealNext()}, deck); //Add the card to new hand
                
        numOfHands++;
        return true;
    }

    public void openBetting(){ //Add player input for amount of the bet
        if (bank == 0){
            return;
        }
        //need player input TODO
        stake[0]++;
        bank--;
        System.out.println("> " + getName() + " says: I bet ONE chip!");
    }
   
    abstract boolean shouldSplit(BlackjackDeck deck, int handIndex, Card dealerCard);
    abstract boolean shouldHit(BlackjackDeck deck, int handIndex, Card dealerCard);
    abstract boolean shouldDouble(BlackjackDeck deck, int handIndex, Card dealerCard);
    abstract boolean shouldStand(BlackjackDeck deck, int handIndex, Card dealerCard);

    abstract String makeChoice(BlackjackDeck deck, int handIndex, Card dealerCard);

    //game decisions
    public void nextAction(BlackjackDeck deck, int handIndex, Card dealerCard) {
        if (hand[handIndex].isBlackjack()) {
            System.out.println("> " + getName() + " says: I have BLACKJACK!");
            won[handIndex] = true;
            stand[handIndex] = true;
            return;
        }

        if (hand[handIndex].isBust()) {
            System.out.println("> " + getName() + " says: I have BUST!");
            bust[handIndex] = true;
            return;
        }

        String choice = makeChoice(deck, handIndex, dealerCard);
        if (choice.equals("HIT")) {
            // continue playing
        } else if (choice.equals("STAND")) {
            stand[handIndex] = true;
        } else if (choice.equals("SPLIT")) {
            split[handIndex] = true;
           //continue playing
        } else if (choice.equals("DOUBLE")) {
            // continue playing
        }
    }
}
