package CardSharps.TexasHoldem;


import CardSharps.Poker.*;

public abstract class HoldemPlayer extends Player {

    HoldemHand hand = null;

    protected HoldemPlayer(String name, int money) {
        super(name, money);
    }

    public HoldemHand getHand(){return hand;}

    public void dealTo(DeckOfCards deck) {
        hand = deck.dealHoldemHand();
    }
}
