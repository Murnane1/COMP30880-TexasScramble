package CardSharps.TexasHoldem;

import CardSharps.Poker.*;
import java.util.ArrayList;
import java.util.List;

public class HumanHoldemPlayer implements PlayerInterface {

    private int bank       		= 0;		 // the total amount of money the player has left, not counting his/her
    // stake in the pot

    private int stake      		= 0;		 // the amount of money the player has thrown into the current pot

    private String name    		= "Player";  // the unique identifying name given to the player

    private HoldemHand hand 		= null;      // the hand dealt to this player

    private boolean folded 		= false;     // set to true when the player folds (gives up)
    private boolean allIn       = false;
    private int allInAddition   = 0;



    public HumanHoldemPlayer(String name, int money) {
        this.name = name;
        this.bank = money;
    }

    public boolean askQuestion(String question)     {
        System.out.print("\n>> " + question + " (y/n)?  ");

        byte[] input = new byte[100];

        try {
            System.in.read(input);

            for (int i = 0; i < input.length; i++)
                if ((char)input[i] == 'y' || (char)input[i] == 'Y')
                    return true;
        }
        catch (Exception e){};

        return false;
    }

    @Override
    public void reset() {
        folded = false;
        stake  = 0;
    }

    @Override
    public String toString() {
        if (hasFolded())
            return "> " + getName() + " has folded, and has " + addCount(getBank(), "chip", "chips") + " in the bank.";
        else
            return "> " + getName() + " has  " + addCount(getBank(), "chip", "chips") + " in the bank";
    }

    @Override
    public HoldemHand getHand() {
        return hand;
    }

    @Override
    public int getBank() {
        return bank;
    }

    @Override
    public int getStake() {
        return stake;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public boolean isBankrupt() {
        return bank == 0;
    }

    @Override
    public boolean hasFolded() {
        return folded;
    }

    @Override
    public boolean isAllIn() {
        return allIn;
    }

    public int getAllInAddition() {
        return allInAddition;
    }

    /* @Override
    public void reorganizeHand() {
        hand = hand.categorize();
    } */

    @Override
    public void dealTo(DeckOfCards deck) {
        hand = deck.dealHoldemHand();
    }

    @Override
    public void addCommunityCards(List<Card> cards){
        this.hand.addCommunityCards(cards);
    }


    @Override
    public void takePot(PotOfMoney pot) {
        System.out.println("\n> " + getName() + " says: I WIN " + addCount(pot.getTotal(), "chip", "chips") + "!\n");
        System.out.println(hand.toString());

        bank += pot.takePot();

        System.out.println(this);
    }

    @Override
    public void fold() {
        if (!folded)
            System.out.println("\n> " + getName() + " says: I fold!\n");

        folded = true;
    }

    @Override
    public void openBetting(PotOfMoney pot) {

        if (bank == 0) return;

        stake++;
        bank--;

        pot.raiseStake(1);

        System.out.println("\n> " + getName() + " says: I open with one chip!\n");

    }
    @Override
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

    @Override
    public void seeBet(PotTexasHoldem pot) {
        int needed  =pot.getCurrentStake() - getStake();   //stake last pot

        if (needed == 0 || needed > getBank())
            return;

        stake += needed;
        bank  -= needed;

        pot.addToPot(needed);


        System.out.println("\n> " + getName() + " says: I see that " + addCount(needed, "chip", "chips") + "!\n");

    }

    @Override
    public void raiseBet(PotTexasHoldem pot) {
        if (getBank() == 0) return;

        stake++;
        bank--;


        pot.raiseStake(1);
        System.out.println("\n> " + getName() + " says: and I raise you 1 chip!\n");

    }
    @Override
    public void reduceStake(int reduction){
        stake -= reduction;
    }
    @Override
    public void allIn(PotOfMoney pot) {
        int previousStake = stake;
        stake += bank;
        bank = 0;
        allIn = true;
        allInAddition = stake - previousStake;
    }

    @Override
    public boolean shouldOpen(PotOfMoney pot) {
        return true;
    }

    @Override
    public boolean shouldSee(PotOfMoney pot) {
        if (getStake() == 0)
            return true;
        else
            return askQuestion("Do you want to see the bet of " +
                    addCount(pot.getCurrentStake() - getStake(), "chip", "chips"));
    }

    @Override
    public boolean shouldRaise(PotOfMoney pot) {
        return askQuestion("Do you want to raise the bet by 1 chip");
    }

    @Override
    public boolean shouldAllIn(PotOfMoney pot) {
        if(pot.getCurrentStake() < getStake() + getBank()){
            return false;
        } else {
            return askQuestion("Do you want to go all-in");
        }
    }

    @Override
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

    private String addCount(int count, String singular, String plural) {
        if (count == 1 || count == -1)
            return count + " " + singular;
        else
            return count + " " + plural;
    }
}
