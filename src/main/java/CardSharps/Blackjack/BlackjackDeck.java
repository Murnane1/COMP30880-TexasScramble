package CardSharps.Blackjack;

import CardSharps.Poker.*;
import java.util.Random;

public class BlackjackDeck {
    public static final String[] suits = { "hearts", "diamonds", "clubs", "spades" };
    public static final int NUMCARDS = 52; //cards in deck
    private Card[] deck = new Card[NUMCARDS]; //total sequence of playing cards
    private int next = 0; //next card dealt
    private Random dice = new Random(System.currentTimeMillis());

    public BlackjackDeck(){
        for (int i = 0; i < suits.length; i++) {
            deck[next++] = new NumberCard("Ace", suits[i], 1, 11); //ace is 1 or 11.
            deck[next++] = new NumberCard("Deuce", suits[i], 2);
            deck[next++] = new NumberCard("Three", suits[i], 3);
            deck[next++] = new NumberCard("Four", suits[i], 4);
            deck[next++] = new NumberCard("Five", suits[i], 5);
            deck[next++] = new NumberCard("Six", suits[i], 6);
            deck[next++] = new NumberCard("Seven", suits[i], 7);
            deck[next++] = new NumberCard("Eight", suits[i], 8);
            deck[next++] = new NumberCard("Nine", suits[i], 9);
            deck[next++] = new NumberCard("Ten", suits[i], 10);
            deck[next++] = new FaceCard("Jack", suits[i], 10);
            deck[next++] = new FaceCard("Queen", suits[i], 10);
            deck[next++] = new FaceCard("King", suits[i], 10);
        }
        reset();
    }

    public void reset(){ //reset state for new hand
        next = 0;
        shuffle();
    }

    public void shuffle(){ //shuffles cards in deck
        Card palm = null;

        int alpha = 0, beta = 0;

        for (int i = 0; i < NUMCARDS * NUMCARDS; i++) {
            alpha = Math.abs(dice.nextInt()) % NUMCARDS;
            beta = Math.abs(dice.nextInt()) % NUMCARDS;

            palm = deck[alpha];
            deck[alpha] = deck[beta];
            deck[beta] = palm;
        }
    }

    //getters
    public Card dealNext(){ //deal next card
        if (next >= NUMCARDS)
            return new FaceCard("Joker", "no suit", 0); // deck is empty
        else
            return deck[next++];
    }

    public BlackjackHand dealHand(){ //deals hand
        BlackjackHand hand = new BlackjackHand(this);
        return hand;
    }
}