package CardSharps.TexasHoldem;


import CardSharps.Poker.*;

import java.util.List;

public abstract class Player {

    private int bank       		= 0;		 // the total amount of money the player has left, not counting his/her

    private int stake      		= 0;		 // the amount of money the player has thrown into the current pot

    private String name    		= "Player";  // the unique identifying name given to the player

    private HoldemHand hand 		= null;      // the hand dealt to this player

    private boolean folded 		= false;     // set to true when the player folds (gives up)
    private boolean allIn       = false;

    protected Player(String name, int money) {
        this.name = name;
        bank = money;
    }

    public void reset() {
        folded = false;
        stake  = 0;
    }

    public String toString() {
        if (hasFolded())
            return "> " + getName() + " has folded, and has " + addCount(getBank(), "chip", "chips") + " in the bank.";
        else
            return "> " + getName() + " has  " + addCount(getBank(), "chip", "chips") + " in the bank";
    }

    public HoldemHand getHand(){return hand;}
    public int getBank(){return bank;};
    public int getStake() {return stake;};
    public String getName() { return name;};
    public boolean isBankrupt() { return bank == 0; };
    public boolean hasFolded() {return folded;};
    public boolean isAllIn() {
        return allIn;
    }

    public double getStakeToBankRatio(){
        return ( (double) getStake() / (getBank() + getStake()));
    }

    public void dealTo(DeckOfCards deck) {
        hand = deck.dealHoldemHand();
    }

    public void takePot(PotTexasHoldem pot) {
        System.out.println("\n> " + getName() + " says: I WIN " + addCount(pot.getTotal(), "chip", "chips") + "!\n");
        System.out.println(hand.toString());

        bank += pot.takePot();

        System.out.println(this);
    }

    public void sharePot(PotTexasHoldem pot, int numWinners){
        int share = pot.sharePot(numWinners);
        System.out.println(getName() + " takes their share of " + addCount(share, "chip", "chips") + " from the pot");
        System.out.println(hand.toString());

        bank += share;
    }

    public void fold(PotTexasHoldem pot) {
        if (!folded)
            System.out.println("\n> " + getName() + " says: I fold!\n");

        folded = true;
    }

    public void seeBet(PotOfMoney pot) {
        int needed  = pot.getCurrentStake() - getStake();
        if (needed == 0 || needed > getBank()) {
            System.out.println(getName() + " cannot cover bet");
            return;
        }

        stake += needed;
        bank  -= needed;

        pot.addToPot(needed);

        System.out.println("\n> " + getName() + " says: I see that " + addCount(needed, "chip", "chips") + "!\n");
    }

    public void raiseBet(PotOfMoney pot, int amount) {
        if (getBank() == 0) return;

        stake += amount;
        bank  -= amount;

        pot.raiseStake(amount);

        System.out.println("\n> " + getName() + " says: and I raise you "+ addCount(amount, "chip", "chips") +"\n");
    }

    public void allIn(PotTexasHoldem pot) {
        pot.addToPot(bank);     //add remaining of bank to pot
        stake += bank;
        bank = 0;
        allIn = true;
        System.out.println("\n> " + getName() + " says: ALL IN with " + addCount(getStake(),"chip","chips") + "!\n");
    }

    abstract boolean shouldSee(PotTexasHoldem pot);
    abstract boolean shouldRaise(PotTexasHoldem pot);

    abstract int raiseAmount(PotTexasHoldem pot);

    abstract boolean shouldAllIn(PotTexasHoldem pot);

    public void nextAction(PotTexasHoldem pot) {
        boolean canCheck = true;

        if (hasFolded()) return;  // no longer in the game

        if(shouldAllIn(pot) && pot.getCurrentStake() >= getStake() + getBank()) {
            allIn(pot);
        } else if (pot.getCurrentStake() >= getStake() + getBank()) {
            fold(pot);
        } else if(!isAllIn()){
            if (pot.getCurrentStake() > getStake()) {                   // existing bet must be covered
                if (shouldSee(pot)) {
                    seeBet(pot);
                    canCheck = false;
                }
                else{
                    fold(pot);
                    return;
                }
            }
            if (shouldRaise(pot)){
                int amount = raiseAmount(pot);
                raiseBet(pot, amount);
            } else {
                if(canCheck)
                    System.out.println("\n> " + getName() + " says: I check!\n");
            }
        }
    }

    //Different method to open bet
    public void postBlind(PotTexasHoldem pot, int blindAmt, String type) {
        if (bank == 0) return;

        stake = stake + blindAmt;
        pot.addStake(blindAmt);
        bank = bank-blindAmt;

        System.out.println("\n> " + getName() + " says: I post " + type + " with "+ blindAmt +" chip!\n");
    }

    public void reduceStake(int reduction){
        stake -= reduction;
    }


    public String addCount(int count, String singular, String plural) {
        if (count == 1 || count == -1)
            return count + " " + singular;
        else
            return count + " " + plural;
    }
}
