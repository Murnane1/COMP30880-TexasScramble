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
    private int allInAddition   = 0;

    public Player(String name, int money) {
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
    public int getAllInAddition() {
        return allInAddition;
    }
    public void dealTo(DeckOfCards deck) {
        hand = deck.dealHoldemHand();
    }
    public void addCommunityCards(List<Card> cards){
        this.hand.addCommunityCards(cards);
    }
    public void takePot(PotOfMoney pot) {
        System.out.println("\n> " + getName() + " says: I WIN " + addCount(pot.getTotal(), "chip", "chips") + "!\n");
        System.out.println(hand.toString());

        bank += pot.takePot();

        System.out.println(this);
    }

    public void fold() {
        if (!folded)
            System.out.println("\n> " + getName() + " says: I fold!\n");

        folded = true;
    }

    public void openBetting(PotOfMoney pot) {

        if (bank == 0) return;

        stake++;
        bank--;

        pot.raiseStake(1);

        System.out.println("\n> " + getName() + " says: I open with one chip!\n");

    }

    public void seeBet(PotTexasHoldem pot) {
        int needed  =pot.getCurrentStake() - getStake();   //stake last pot

        if (needed == 0 || needed > getBank())
            return;

        stake += needed;
        bank  -= needed;

        pot.addToPot(needed);


        System.out.println("\n> " + getName() + " says: I see that " + addCount(needed, "chip", "chips") + "!\n");

    }

    public void raiseBet(PotTexasHoldem pot) {
        if (getBank() == 0) return;

        stake++;
        bank--;


        pot.raiseStake(1);
        System.out.println("\n> " + getName() + " says: and I raise you 1 chip!\n");
    }

    public void allIn(PotOfMoney pot) {
        int previousStake = stake;
        stake += bank;
        bank = 0;
        allIn = true;
        allInAddition = stake - previousStake;
    }

    abstract boolean shouldOpen(PotOfMoney pot);
    abstract boolean shouldSee(PotOfMoney pot);
    abstract boolean shouldRaise(PotOfMoney pot);
    abstract boolean shouldAllIn(PotOfMoney pot);

    public void nextAction(PotTexasHoldem pot) {

        if (hasFolded()) return;  // no longer in the game

        if (isBankrupt() ) {
            // not enough money to cover the bet

            System.out.println("\n> " + getName() + " says: I'm out!\n");

            fold();

            return;
        }
        if(shouldAllIn(pot)) {
            allIn(pot);
            return;
        }

        else if(!isAllIn()){
            if (pot.getCurrentStake() > getStake()) {
                // existing bet must be covered

                if (shouldSee(pot)) {
                    seeBet(pot);
                }
                else{
                    fold();
                    return;
                }
            }
            if (shouldRaise(pot)){
                raiseBet(pot);
                return;
            }

            else {
                System.out.println("\n> " + getName() + " says: I check!\n");
            }
        }

    }

    //Different method to open bet
    public boolean postBlind(PotOfMoney pot, int blindAmt, String type) {
        if (bank == 0) return false;

        //FLAG to check if player had enough for blind
        boolean enough = true;

        stake = stake + blindAmt;
        pot.addStake(blindAmt);
        bank = bank-blindAmt;

        System.out.println("\n> " + getName() + " says: I post " + type + " with "+ blindAmt +" chip!\n");
        return enough;
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
