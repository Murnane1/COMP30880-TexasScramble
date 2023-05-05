package CardSharps.Blackjack;


import CardSharps.Poker.*;
import java.util.Random;

public class BlackjackComputerPlayer extends BlackjackPlayer{ // error caused by abstract methods such as shouldSplit etc
    
    private static char ERROR_MOVE = 'E';
    private static char STAND_MOVE = 'S';
    private static char DOUBLE_MOVE = 'D';
    private static char HIT_MOVE = 'H';
    char[][] DECISION_MATRIX;

    //constructor
    public BlackjackComputerPlayer(String name, int money){
        super(name, money);

        DECISION_MATRIX = new char[][]{
                /*
                *   E = Error
                *   S = Stand
                *   D = Double
                *   H = Hold
                 */
                // Dealer's hand in range of values 0-A
                {'E','E','E','E','E','E','E','E','E','E','E','E'}, //computer's hand = 0
                {'E','E','E','E','E','E','E','E','E','E','E','E'}, //computer's hand = 1
                {'E','E','E','E','E','E','E','E','E','E','E','E'}, //computer's hand = 2
                {'E','E','E','E','E','E','E','E','E','E','E','E'}, //computer's hand = 3
                {'E','E','H','H','H','H','H','H','H','H','H','H'}, //computer's hand = 4
                {'E','E','H','H','H','H','H','H','H','H','H','H'}, //computer's hand = 5
                {'E','E','H','H','H','H','H','H','H','H','H','H'}, //computer's hand = 6
                {'E','E','H','H','H','H','H','H','H','H','H','H'}, //computer's hand = 7
                {'E','E','H','H','H','H','H','H','H','H','H','H'}, //computer's hand = 8
                {'E','E','H','D','D','D','D','H','H','H','H','H'}, //computer's hand = 9
                {'E','E','D','D','D','D','D','D','D','D','H','H'}, //computer's hand = 10
                {'E','E','D','D','D','D','D','D','D','D','D','D'}, //computer's hand = 11
                {'E','E','H','H','S','S','S','H','H','H','H','H'}, //computer's hand = 12
                {'E','E','S','S','S','S','S','H','H','H','H','H'}, //computer's hand = 13
                {'E','E','S','S','S','S','S','H','H','H','H','H'}, //computer's hand = 14
                {'E','E','S','S','S','S','S','H','H','H','H','H'}, //computer's hand = 15
                {'E','E','S','S','S','S','S','H','H','H','H','H'}, //computer's hand = 16
                {'E','E','S','S','S','S','S','S','S','S','S','S'}, //computer's hand = 17
                {'E','E','S','S','S','S','S','S','S','S','S','S'}, //computer's hand = 18
                {'E','E','S','S','S','S','S','S','S','S','S','S'}, //computer's hand = 19
                {'E','E','S','S','S','S','S','S','S','S','S','S'}, //computer's hand = 20
                {'S','S','S','S','S','S','S','S','S','S','S','S'}, //computer's hand = 21

        };
    }

    //always false computer never splits
    boolean shouldSplit(BlackjackDeck deck, int handIndex, Card dealerCard) {
        return false;
    }

    //hit, double, stand according to table
    boolean shouldHit(BlackjackDeck deck, int handIndex, Card dealerCard) {
        if(HIT_MOVE == DECISION_MATRIX[this.getHand(handIndex).getHandValue()][dealerCard.getValue()]) {
            return true;
        }
        else {
            return false;
        }
    }

    boolean shouldDouble(BlackjackDeck deck, int handIndex, Card dealerCard) {
        if(DOUBLE_MOVE == DECISION_MATRIX[this.getHand(handIndex).getHandValue()][dealerCard.getValue()]) {
            return true;
        }
        else {
            return false;
        }
    }

    boolean shouldStand(BlackjackDeck deck, int handIndex, Card dealerCard) {
        if(STAND_MOVE == DECISION_MATRIX[this.getHand(handIndex).getHandValue()][dealerCard.getValue()]) {
            return true;
        }
        else {
            return false;
        }
    }

    public String makeChoice(BlackjackDeck deck, int handIndex, Card dealerCard) {
        BlackjackHand hand = getHand(handIndex);
        int handValue = hand.getHandValue();
        boolean canSplit = canSplit(handIndex);
        boolean canDouble = canDouble(handIndex);

        // If the hand value is less than 12, always hit.
        if (handValue < 12) {
            hit(deck, handIndex);
            return "HIT";
        }

        // If the hand value is greater than or equal to 17, always stand.
        if (handValue >= 17) {
            return "STAND";
        }

        // If the dealer's up card is 7 or higher and the hand value is between 12 and 16, stand.
        if (dealerCard.getValue() >= 7 && handValue >= 12 && handValue <= 16) {
            return "STAND";
        }

        // If the dealer's up card is 6 or lower and the hand value is between 12 and 16, hit.
        if (dealerCard.getValue() <= 6 && handValue >= 12 && handValue <= 16) {
            hit(deck, handIndex);
            return "HIT";
        }

        // If the hand can be split and it's a good idea to do so, split.
        if (canSplit && shouldSplit(deck, handIndex, dealerCard)) {
            split(deck, handIndex);
            return "SPLIT";
        }

        // If the hand can be doubled and it's a good idea to do so, double.
        if (canDouble && shouldDouble(deck, handIndex, dealerCard)) {
            doubleDown(deck, handIndex);
            return "DOUBLE";
        }

        // If none of the above conditions apply, hit.
        hit(deck, handIndex);
        return "HIT";
    }
}
