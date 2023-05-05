package CardSharps.TexasHoldem;

import CardSharps.Poker.*;


import java.util.ArrayList;

/*
TODO ensure hold cards are preserved in position 0 and 1 in HoldemHand class
 */

public class PostFlopHandAnalyser {
    private HoldemHand hand;
    private ArrayList<Card> holdCards = new ArrayList<Card>();              //misspelled hole cards???

    private ArrayList<Card> communityCards = new ArrayList<Card>();

    private ArrayList<Card> mergedHand = new ArrayList<Card>();

    public PostFlopHandAnalyser(HoldemHand hand) {
        this.hand = hand;

        this.holdCards.add(hand.getCard(0));
        this.holdCards.add(hand.getCard(1));

        this.communityCards = (ArrayList<Card>) hand.getCommunityCards();       //TODO ensure casting is read correctly

        this.mergedHand.addAll(holdCards);
        this.mergedHand.addAll(communityCards);
    }

    public void updateCardHand(HoldemHand hand) {
        this.hand = hand;

        this.holdCards.add(hand.getCard(0));
        this.holdCards.add(hand.getCard(1));

        this.communityCards = (ArrayList<Card>) hand.getCommunityCards();

        this.mergedHand.addAll(holdCards);
        this.mergedHand.addAll(communityCards);
    }

    public void sortMergedHandAscending() {
        Card temp;

        for(int i = 0; i < mergedHand.size() - 1; i++) {
            for(int j = 0; j < mergedHand.size() - i - 1; j++) {
                if(mergedHand.get(j).getValue() > mergedHand.get(j + 1).getValue()) {
                    temp = mergedHand.get(j);
                    mergedHand.set(j, mergedHand.get(j + 1));
                    mergedHand.set(j + 1, temp);
                }
            }
        }
    }

    public void sortCommunityCardsAscending() {
        Card temp;

        for(int i = 0; i < communityCards.size() - 1; i++) {
            for(int j = 0; j < communityCards.size() - i - 1; j++) {
                if(communityCards.get(j).getValue() > communityCards.get(j + 1).getValue()) {
                    temp = communityCards.get(j);
                    communityCards.set(j, communityCards.get(j + 1));
                    communityCards.set(j + 1, temp);
                }
            }
        }
    }

    public boolean isTopPair() {
        boolean cardIsEqual = false;

        for(int i = 0; i < holdCards.size(); i++) {
            for (int j = 0; j < communityCards.size(); j++) {
                if(holdCards.get(i).getValue() == communityCards.get(j).getValue()) {
                    cardIsEqual = true;
                }
            }
        }

        return cardIsEqual;
    }

    public boolean isOverPair() {
        boolean cardIsHighest = true;

        for(int i = 0; i < holdCards.size(); i++) {
            for(int j = 0; j < communityCards.size(); j++) {
                if(holdCards.get(i).getValue() <= communityCards.get(j).getValue()) {
                    cardIsHighest = false;
                }
            }
        }

        if(holdCards.get(0).getValue() != holdCards.get(1).getValue()) {        //TODO ensure .getValue() is correct
            cardIsHighest = false;
        }

        return cardIsHighest;
    }

    public boolean isWeakTwoPair() {
        boolean holdPairFound = false;
        boolean communityPairFound = false;

        for(int i = 0; i < holdCards.size(); i++) {
            for(int j = 0; j < communityCards.size(); j++) {
                if(holdCards.get(i).getValue() == communityCards.get(j).getValue()) {
                    holdPairFound = true;
                }
            }
        }

        for(int i = 0; i < communityCards.size(); i++) {
            for(int j = i; j < communityCards.size(); j++) {
                if(communityCards.get(i).getValue() == communityCards.get(j).getValue()) {
                    communityPairFound = true;
                }
            }
        }

        return holdPairFound && communityPairFound;
    }

    public boolean isStrongTwoPair() {
        boolean firstPairFound = false;
        boolean secondPairFound = false;

        for(int i = 0; i < holdCards.size(); i++) {
            for(int j = 0; j < communityCards.size(); j++) {
                if(i==0 && holdCards.get(i).getValue() == communityCards.get(j).getValue()) {
                    firstPairFound = true;
                }
                else if (i==1 && holdCards.get(i).getValue() == communityCards.get(j).getValue()) {
                    secondPairFound = true;
                }
            }
        }

        return firstPairFound && secondPairFound;
    }

    public boolean isMonsterHand() {
        return (isStrongTwoPair() || (hand.evaluateHand(mergedHand) >= 10000));//TODO hand.HandValue.THREES_VALUE.getHandValue()) );
    }

    public boolean isOESD() {
        int OESDCount = 4;
        int runningSequence = 0;
        int previousValue = -1;
        int currentValue;

        if((hand.evaluateHand(mergedHand) <= 10000 && hand.evaluateHand(mergedHand) >= 10100) || hand.evaluateHand(mergedHand) <= 30000000 && hand.evaluateHand(mergedHand) >= 30000150) { //TODO value of straight (double check values)
            sortMergedHandAscending();

            for(int i = 0; i < mergedHand.size(); i++){
                currentValue = mergedHand.get(i).getValue();

                if(currentValue == previousValue + 1){
                    runningSequence++;
                }
                else {
                    runningSequence = 0;
                }

                previousValue = currentValue;
            }
        }

        return runningSequence == OESDCount;
    }

    private boolean isOESD(int[] cardValues) {
        int OESDCount = 4;
        int runningSequence = 0;
        int previousValue = -1;
        int currentValue;

        if(hand.evaluateHand(mergedHand) != 100000000) { //TODO value of straight

            for(int i = 0; i < cardValues.length; i++){
                currentValue = cardValues[i];

                if(currentValue == previousValue + 1){
                    runningSequence++;
                }
                else {
                    runningSequence = 0;
                }

                previousValue = currentValue;
            }
        }

        return runningSequence == OESDCount;
    }

    public boolean isGutshot() {
        boolean gutshot = false;
        int[] mergedHandValues = new int[mergedHand.size()];
        int[] temp;

        sortMergedHandAscending();

        for (int i = 0; i < mergedHand.size(); i++) {
            mergedHandValues[i] = mergedHand.get(i).getValue();
        }
        temp = mergedHandValues;

        if (hand.evaluateHand(mergedHand) != 100000000 && !(isGutshot())) { //TODO value of straight)
            for (int i = 0; i < mergedHand.size(); i++) {
                for (int j = 2; j <= 14; j++) {
                    mergedHandValues[i] = j;

                    if (isOESD(mergedHandValues)) {
                        gutshot = true;
                    }
                }
                mergedHandValues = temp;
            }
        }

        return gutshot;
    }

    public boolean isDBGutshot() {
        boolean gutshot = false;
        boolean dbGutshot = false;
        int[] mergedHandValues = new int[mergedHand.size()];
        int[] temp;
        int[] reducedHand;
        int gutshotIndex = 0;

        sortMergedHandAscending();

        for (int i = 0; i < mergedHand.size(); i++) {
            mergedHandValues[i] = mergedHand.get(i).getValue();
        }
        temp = mergedHandValues;

        if (hand.evaluateHand(mergedHand) != 100000000 && !(isGutshot())) { //TODO value of straight)
            for (int i = 0; i < mergedHand.size(); i++) {
                int tempGutshotIndex = i;
                for (int j = 2; j <= 14; j++) {
                    mergedHandValues[i] = j;

                    if (isOESD(mergedHandValues)) {
                        gutshot = true;
                        gutshotIndex = tempGutshotIndex;
                    }
                }
                mergedHandValues = temp;
            }

            reducedHand = new int[mergedHand.size() - gutshotIndex + 1];

            int tempIndex = 0;
            for(int i = gutshotIndex; i < mergedHand.size(); i ++) {
                    reducedHand[tempIndex++] = mergedHand.get(i).getValue();
            }

            temp = reducedHand;

            if (gutshot) {
                for (int i = gutshotIndex; i < reducedHand.length; i++) {
                    for (int j = 2; j <= 14; j++) {
                        reducedHand[i] = j;

                        if (isOESD(reducedHand)) {
                            dbGutshot = true;
                        }
                    }
                    reducedHand = temp;
                }
            }
        }

        return dbGutshot;
    }

    public boolean isFlushDraw() { //TODO add case for monochrome community cards
        int heartCount = 0;
        int spadeCount = 0;
        int clubCount = 0;
        int diamondCount = 0;

        boolean flushDraw = false;

        for(int i = 0; i < mergedHand.size(); i ++){
            switch (mergedHand.get(i).getSuit()) {
                case "hearts":
                    heartCount++;
                case "spades":
                    spadeCount++;
                case "clubs":
                    clubCount++;
                case "diamonds":
                    diamondCount++;
            }
        }

        if(heartCount == 4 || spadeCount == 4 || clubCount == 4 || diamondCount == 4 ) {
            flushDraw = true;
        }

        return flushDraw;
    }

    public boolean isMonsterDraw() {
        if(isFlushDraw() && (isOESD() || isGutshot() || isTopPair())) {
            return true;
        }
        else {
            return false;
        }
    }

    public boolean isOverCard() {
        boolean cardIsHighest = true;

        for(int i = 0; i < holdCards.size(); i++) {
            for(int j = 0; j < communityCards.size(); j++) {
                if(holdCards.get(i).getValue() <= communityCards.get(j).getValue()) {
                    cardIsHighest = false;
                }
            }
        }

        return cardIsHighest;
    }

    public boolean isTrash() {
        boolean trash = true;

        if(isOESD() || isGutshot() || isDBGutshot() || isFlushDraw() || isMonsterDraw()){
            trash = false;
        }

        if(isTopPair() || isOverPair() || isWeakTwoPair() || isStrongTwoPair() || isMonsterHand()){
            trash = false;
        }

        if(isOverCard()){
            trash = false;
        }

        return trash;
    }

    public boolean isDrawy() {
        int heartCount = 0;
        int spadeCount = 0;
        int clubCount = 0;
        int diamondCount = 0;

        int longestRun= 0;

        boolean drawy = false;

        for(int i = 0; i < communityCards.size(); i ++){
            switch (communityCards.get(i).getSuit()) {
                case "hearts":
                    heartCount++;
                case "spades":
                    spadeCount++;
                case "clubs":
                    clubCount++;
                case "diamonds":
                    diamondCount++;
            }
        }

        int previousValue = -1;
        int currentValue;

        sortCommunityCardsAscending();

        for(int i = 0; i < communityCards.size(); i++){
            currentValue = communityCards.get(i).getValue();

            if(currentValue == previousValue + 1){
                longestRun++;
            }
            else {
                longestRun = 0;
            }

            previousValue = currentValue;
        }

        if((heartCount >= 3 || spadeCount >= 3 || clubCount >= 3 || diamondCount >= 3) || (longestRun >=3)){
            drawy = true;
        }
        else if ((heartCount == 2 || spadeCount == 2 || clubCount == 2 || diamondCount == 2) && (longestRun ==2)) {
            drawy = true;
        }
        else {
            drawy = false;
        }

        return drawy;
    }

    public boolean isDry() {
        return !(isDrawy());
    }
}
