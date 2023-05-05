package CardSharps.Blackjack;
import CardSharps.Poker.*;

public class BlackjackHand { //reference pokerhand.java in poker, references both dealer and player
    
    //static variables for cards etc
    private int numCards = 2;
    public static final int MAXCARDS = 11; //initially gets 2 card to then hit on (the dealer gets 1 card initially)
    
    
    private Card[] hand;
    public BlackjackDeck deck;

    public BlackjackHand(Card[] hand, BlackjackDeck deck){
        this.hand = hand;
        this.deck = deck;
    }

    public BlackjackHand(BlackjackDeck blackjackDeck){
        this.deck = blackjackDeck;
        hand = new Card[MAXCARDS]; //11 is max possible cards
        for (int i = 0; i < numCards; i++){
            setCard(i, blackjackDeck.dealNext());
        }
    }

    public String toString(){ //display hand
        String desc = "";
        for (int i = 0; i < numCards; i++){
            desc = desc + "\n  " + i + ":  " + getCard(i).toString();
        }
        return desc + "\n";
    }


    //getters
    public Card getCard(int num){
        if (num >= 0 && num < numCards){
            return hand[num];
        } else {
            return null;
        }
    }

    public Card[] getCards(){ //might need to test this
        return hand;
    }

    public int getValue(){ //gets value of card
        return getCard(0).getValue(); //index 0?
    }

    public int getNumCards(){
        return numCards;
    }

    public int getHandValue(){
        int handValue = 0;
        int numAces = 0;

        for (int i = 0; i < getNumCards(); i++){
            int cardValue = getCard(i).getRank();
            if (cardValue == 1){ //ace card
                numAces++;
                handValue += 11; //assuming that it is currently holding the value of 11
            } else {
                handValue += cardValue;
            }
        }

        while (handValue > 21 && numAces > 0) {
            handValue -= 10; //convert ace value to 1 instead of 11
            numAces--; //remove any ace.
        }
        return handValue;
    }


    //setters
    public void setCard(int num, Card card){
        if (num >= 0 && num < numCards){
            hand[num] = card;
        }
    }

    public void addCard(Card card){
        hand[numCards++] = card;
    }

    //hand classifier
    public boolean isBlackjack(){
        if (getNumCards() != 2){
            return false;
        }
        int handValue = getHandValue();
        if (handValue == 21){
            return true;
        }
        return false;
    }

    public boolean isBust(){
        int handValue = getHandValue();
        if (handValue > 21){
            return true;
        } else {
            return false;
        }
    }

    public boolean isSplit(){
        if (getNumCards() != 2){
            return false;
        }
        int cardValue1 = getCard(0).getValue();
        int cardValue2 = getCard(1).getValue();

        if (cardValue1 == cardValue2){
            return true;
        }
        return false;
    }
}
