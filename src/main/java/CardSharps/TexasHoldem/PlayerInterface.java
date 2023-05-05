package CardSharps.TexasHoldem;


import CardSharps.Poker.*;
import java.util.ArrayList;
import java.util.List;

public interface PlayerInterface {
    public void reset();
    public String toString();
    public HoldemHand getHand();
    public int getBank();
    public int getStake();
    public String getName();
    public boolean isBankrupt();
    public boolean hasFolded();
    public boolean isAllIn();
    public int getAllInAddition();
    //public void reorganizeHand();
    public void dealTo(DeckOfCards deck);
    public void addCommunityCards(List<Card> card);
    public void takePot(PotOfMoney pot);
    public void fold();
    public void openBetting(PotOfMoney pot);
    public void seeBet(PotTexasHoldem pot);
        //public void raiseBet(PotOfMoney pot);
    public void raiseBet(PotTexasHoldem pot);
    public void allIn(PotOfMoney pot);
    public boolean shouldOpen(PotOfMoney pot);
    public boolean shouldSee(PotOfMoney pot);
    public boolean shouldRaise(PotOfMoney pot);
    public boolean shouldAllIn(PotOfMoney pot);
    public void nextAction(PotTexasHoldem pot);

    //Different method to open bet
    public boolean postBlind(PotOfMoney pot, int blindAmt, String type);
    public void reduceStake(int reduction);

}
