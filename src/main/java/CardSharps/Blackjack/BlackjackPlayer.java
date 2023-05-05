package CardSharps.Blackjack;

import java.util.Arrays;
import CardSharps.Poker.*;
import java.util.Scanner;
import java.util.InputMismatchException;

abstract class BlackjackPlayer { //reference player.java, humanplayer.java and computerplayer.java
    public int bank = 0; //total money player has left (without stake)
    
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
        stake = new int[1];
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
        return this.bank;
    }

    public int getStake(int handIndex){
        return stake[handIndex];
    }

    public int setStake(int handIndex, int amount){
        return stake[handIndex] = amount;
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
        }

        for (int i = 0; i < numOfHands; i++) {
            hand[i] = deck.dealHand();
            stake[i] = originalStake;
        }
    }


    public void addBank(int addition){
        this.bank += addition;
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

    public void openBettingHuman(){
        if (bank == 0){
            return;
        }
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter your bet amount:");
        int playerBet = 0;
        boolean isValidInput = false;
        while (!isValidInput) {
            try {
                playerBet = scanner.nextInt();
                if (playerBet > bank) {
                    System.out.println("You cannot bet more than what you have in the bank.");
                    System.out.println("Enter your bet amount:");
                } else {
                    isValidInput = true;
                    break;
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input, Please enter a number.");
                scanner.nextLine();
                System.out.println("Enter your bet amount:");
            }
        }
        setStake(0, playerBet);
        System.out.println("> " + getName() + " says: I bet " + playerBet + " chips!");

        addBank(-playerBet);
    }

    public void openBettingAI() {
        if (bank == 0) {
            return;
        }
        int maxBet = bank / 2; // maximum bet is less than half of bank
        int minBet = 2; // minimum bet is 2
        int playerBet = (int) (Math.random() * (maxBet - minBet + 1) + minBet); // random bet between minBet and maxBet
        setStake(0, playerBet);
        System.out.println("> " + getName() + " says: I bet " + playerBet + " chips!");

        addBank(-playerBet);
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
