package CardSharps.TexasHoldem;


import CardSharps.Poker.*;
import java.util.*;

public class HoldemHand /*implements Handable*/ {
    //enum for cardvalue, suit, risk and handvalue
    public enum CardValue{
        ACE(1, "Ace"),
        DEUCE(2, "Two"),
        THREE(3, "Three"),
        FOUR(4, "Four"),
        FIVE(5, "Five"),
        SIX(6, "Six"),
        SEVEN(7, "Seven"),
        EIGHT(8, "Eight"),
        NINE(9, "Nine"),
        TEN(10, "Ten"),
        JACK(11, "Jack"),
        QUEEN(12, "Queen"),
        KING(13, "King");

        private final int value;
        private final String name;
     

        private CardValue(int value, String name){
            this.value = value;
            this.name = name;
        }

        private int getCardValue() {
            return value;
        }

        @Override
        public String toString() {
            return name;
        }
    }

    public enum Suit {
        HEARTS("Hearts"),
        DIAMONDS("Diamonds"),
        CLUBS("Clubs"),
        SPADES("Spades");

        private final String cardSuit;

        private Suit(String cardSuit){
            this.cardSuit = cardSuit;
        }

        public String getSuit(){
            return cardSuit;
        }

        @Override
        public String toString(){
            return cardSuit;
        }
    }

    public enum HandValue {
        ROYALFLUSH_VALUE(40000000),
        STRAIGHTFLUSH_VALUE(30000000),
        FOURS_VALUE(20000000),
        FULLHOUSE_VALUE(1000000),
        FLUSH_VALUE(100000),
        STRAIGHT_VALUE(10000),
        THREES_VALUE(1000),
        TWOPAIR_VALUE(100),
        PAIR_VALUE(10),
        HIGHCARD_VALUE(0);

        private final int value;

        HandValue(int value){
            this.value = value;
        }

        public int getHandValue(){
            return value;
        }
    }

    public enum RiskWorthiness{
        ROYALFLUSH_RISK(0),
        STRAIGHTFLUSH_RISK(5),
        STRAIGHT_RISK(10),
        FOURS_RISK(15),
        FLUSH_RISK(20),
        FULLHOUSE_RISK(25),
        THREES_RISK(30),
        TWOPAIR_RISK(35),
        PAIR_RISK(40),
        HIGHCARD_RISK(90),
        DEFAULT_RISK(20);

        private final int risk;

        RiskWorthiness(int risk){
            this.risk = risk;
        }

        public int getRiskValue(){
            return risk;
        }
    }

    public static final int INIT_PLAYER_CARDS = 2; // total number of cards to be determined
    public static final int NUM_COMMUNITY_CARDS = 5;
    public static final int TOTAL_CARDS = INIT_PLAYER_CARDS+NUM_COMMUNITY_CARDS;
    
    private List<Card> communityCards; //define community cards as an array of cards for reference
    private List<Card> playerHand; //define playerHand as an array of cards
    private int bestHandValue; //used to get players best hand to determine winner

    public HoldemHand(DeckOfCards deck){
        this.playerHand = new ArrayList<>();
        this.playerHand.add(deck.dealNext());
        this.playerHand.add(deck.dealNext());
        
        this.communityCards = new ArrayList<>();
        this.bestHandValue = 0;
    }

    public void addCommunityCards(List<Card> cards){
        this.communityCards.addAll(cards);
    }



    public List<Card> getBestHand(){ //returns the best hand a player can have - gets a value from evaluatehand
        List<Card> cards = new ArrayList<>();
        cards.addAll(playerHand);

        if(!communityCards.isEmpty() && communityCards!=null){
            cards.addAll(communityCards);

            List<List<Card>> possibleHands = generatePossibleHands(cards);
            List<Card> bestHand = null;

            for (List<Card> hand : possibleHands){
                int handValue = evaluateHand(hand.subList(0, 5));
                if (handValue > bestHandValue) {
                    bestHandValue = handValue;
                    bestHand = hand.subList(0, 5);
                }
            }
            return bestHand;
        }
        
        return cards;
    }

  
    //Permutations of all possible hands
    public static List<List<Card>> generatePossibleHands(List<Card> list) {
        if (list.isEmpty()) {
            List<List<Card>> result = new ArrayList<List<Card>>();
            result.add(new ArrayList<Card>());
            return result;
        }
    
        List<List<Card>> possibleHands = new ArrayList<List<Card>>();
        Card firstElement = list.remove(0);
        List<List<Card>> recursiveReturn = generatePossibleHands(list);
        for (List<Card> li : recursiveReturn) {
    
            for (int index = 0; index <= li.size(); index++) {
                List<Card> temp = new ArrayList<Card>(li);
                temp.add(index, firstElement);
                possibleHands.add(temp);
            }
        }
        return possibleHands;
    }

    public int evaluateHand(List<Card> hand) { //returns hand value from all possible hands
        sortHand(hand);
        if (isRoyalFlush(hand)){
            return HandValue.ROYALFLUSH_VALUE.getHandValue();
        } else if (isStraightFlush(hand)){
            return HandValue.STRAIGHTFLUSH_VALUE.getHandValue() + hand.get(4).getValue(); 
        } else if (isFourOfAKind(hand)){
            return HandValue.FOURS_VALUE.getHandValue() + hand.get(2).getValue();
        } else if (isFullHouse(hand)){
            return HandValue.FULLHOUSE_VALUE.getHandValue() + hand.get(2).getValue();
        } else if (isFlush(hand)){
            return HandValue.FLUSH_VALUE.getHandValue() + hand.get(1).getValue() + hand.get(2).getValue();
        } else if(isStraight(hand)){
            return HandValue.STRAIGHT_VALUE.getHandValue() + hand.get(4).getValue();
        } else if (isThreeOfAKind(hand)){
            return HandValue.THREES_VALUE.getHandValue() + hand.get(2).getValue();
        } else if (isTwoPair(hand)){
            return HandValue.TWOPAIR_VALUE.getHandValue() + hand.get(1).getValue() + hand.get(3).getValue() + hand.get(4).getValue();
        } else if (isPair(hand)){
            return HandValue.PAIR_VALUE.getHandValue() + hand.get(2).getValue();
        } else {
            return HandValue.HIGHCARD_VALUE.getHandValue() + (hand.get(4).getRank() == 1 ? 14 : hand.get(0).getValue());
        }
    }

    public void sortHand(List<Card> hand) { //sorts in descending order
        Collections.sort(hand, Collections.reverseOrder());
    }

    //Display a hand
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Player Hand: ");
        for (Card card : playerHand) {
            sb.append(card.toString()).append(", ");
        }
        sb.delete(sb.length() - 2, sb.length()).append("\n");
        sb.append("Community Cards: ");
        for (Card card : communityCards) {
            sb.append(card.toString()).append(", ");
        }
        sb.delete(sb.length() - 2, sb.length()).append("\n");
        return sb.toString();
    }


    //modifiers
    public void setCard(int num, Card card){
        if (num >= 0 && num < TOTAL_CARDS)
            playerHand.set(num, card);
    }

    //Accesors
    public Card getCard(int num){ //get card at index
        if (num >= 0 && num < TOTAL_CARDS)
            return playerHand.get(num);
        else
            return null;
    }

    public List<Card> getCommunityCards(){ //may make things easier with this
        return communityCards;
    }

    public int getValue(){
        return getCard(0).getValue(); // simply return the value of the higest card
    }

    public List<Card> getHand(){ //get the players list of cards
        return new ArrayList<>(playerHand);
    }

    public int getBestHandValue(){ //getter for bestHand value using evaluateHands method
        return this.bestHandValue;
    }

    public int getRiskWorthiness() { // We override this value for specific hands such as Straight, FullHouse etc..
        return RiskWorthiness.DEFAULT_RISK.getRiskValue();
    }

    public int getBettingRound(){
        int betRound = 0;   //preflop
        if(communityCards.size() == 3){
            betRound = 1;   //flop
        }
        else if(communityCards.size() == 4){
            betRound = 2;   //turn
        }
        else if(communityCards.size() == 5){
            betRound = 3;   //river
        }
        return betRound;
    }

    //Hand classifiers
    public boolean isFourOfAKind(List<Card> hand) {
        return hand.get(0).getValue() == hand.get(3).getValue() 
                ||
                hand.get(1).getValue() == hand.get(4).getValue();
    }

    public boolean isFullHouse(List<Card> hand) { //player has a pair and three of a kind
        return (hand.get(0).getValue() == hand.get(1).getValue()
                && hand.get(2).getValue() == hand.get(4).getValue())
                || (hand.get(0).getValue() == hand.get(2).getValue()
                && hand.get(3).getValue() == hand.get(4).getValue());
    }

    public boolean isStraight(List<Card> hand) {
		return (hand.get(0).getValue() == (hand.get(1).getValue() + 1) % 13 && // ordered by rank
                hand.get(1).getValue() == hand.get(2).getValue() + 1 &&
                hand.get(2).getValue() == hand.get(3).getValue() + 1 &&	
                hand.get(3).getValue() == hand.get(4).getValue() + 1)
				||
                (hand.get(0).getValue() == (hand.get(1).getValue() - 1) % 13 &&  // ordered by game value
                hand.get(1).getValue()  == hand.get(2).getValue() - 1 &&
                hand.get(2).getValue()  == hand.get(3).getValue() - 1 &&	
                hand.get(3).getValue()  == hand.get(4).getValue() - 1);
    }

    public boolean isFlush(List<Card> hand) { // returns true if all cards in the hand are the same suit
        String suit = hand.get(0).getSuit();
        for (int i = 1; i <= 4; i++) {
            if (!hand.get(i).getSuit().equals(suit)){
                return false;
            }
        }
        return true;
    }

    public boolean isStraightFlush(List<Card> hand){ //returns true if hand is both a flush and a straight
        return isFlush(hand) && isStraight(hand);
    }

    public boolean isRoyalFlush(List<Card> hand) { // returns true if the hand is a straight flush from 'Ten -> Ace'
        return isFlush(hand) && hand.get(4).getValue() == 1
                && hand.get(0).getValue() == CardValue.KING.getCardValue()
                && hand.get(1).getValue() == CardValue.QUEEN.getCardValue()
                && hand.get(2).getValue() == CardValue.JACK.getCardValue()
                && hand.get(3).getValue() == CardValue.TEN.getCardValue();
    }
    
    public boolean isThreeOfAKind(List<Card> hand) { // returns true if the hand has three cards of the same value
        return hand.get(0).getValue() == hand.get(2).getValue()
                || hand.get(1).getValue() == hand.get(3).getValue()
                || hand.get(2).getValue() == hand.get(4).getValue();
    }
    
    public boolean isTwoPair(List<Card> hand) { // returns true if the hand has two pairs of cards with the same values
        return hand.get(0).getValue() == hand.get(1).getValue()
                && hand.get(2).getValue() == hand.get(3).getValue()
                || hand.get(0).getValue() == hand.get(1).getValue()
                && hand.get(3).getValue() == hand.get(4).getValue()
                || hand.get(1).getValue() == hand.get(2).getValue()
                && hand.get(3).getValue() == hand.get(4).getValue();
    }
    
    public boolean isPair(List<Card> hand) { // returns true if the hand has one pair of cards with the same value
        for (int i = 0; i < 5 - 1; i++) {
            if (hand.get(i).getValue() == hand.get(i + 1).getValue())
                return true;
        }
        return false;
    }
    
    public boolean isHigh(List<Card> hand) { // returns true if the hand has no classification, ace will default to high
        if (isFlush(hand) || isStraight(hand) || isThreeOfAKind(hand) || isTwoPair(hand) || isPair(hand) || isFullHouse(hand) || isFourOfAKind(hand)) {
            return false;
        }
        return true;
    }


}
