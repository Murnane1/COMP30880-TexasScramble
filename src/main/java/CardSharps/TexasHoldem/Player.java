package CardSharps.TexasHoldem;

public abstract class Player {
    private int bank       		= 0;		 // the total amount of money the player has left, not counting his/her

    private int stake      		= 0;		 // the amount of money the player has thrown into the current pot

    private String name    		= "Player";  // the unique identifying name given to the player

    private boolean folded 		= false;     // set to true when the player folds (gives up)
    private boolean allIn       = false;


    public Player(String name, int money){
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
        else if (isAllIn()) {
            return "> " + getName() + " is all in, and has " + addCount(getBank(), "chip", "chips") + " in the bank.";
        } else
            return "> " + getName() + " has " + addCount(getBank(), "chip", "chips") + " in the bank";
    }


    //Accessors
    public int getBank(){return bank;};
    public int getStake() {return stake;};
    public String getName() { return name;};
    public boolean isBankrupt() { return bank == 0; };
    public boolean hasFolded() {return folded;};
    public boolean isAllIn() {
        return allIn;
    }

    //Modifiers
    public void setBank(int bank) { this.bank = bank; }
    public void reduceBank(int amount) {
        bank -= amount;
    }
    public void increaseBank(int amount) {
        bank += amount;
    }
    public void setStake(int stake) { this.stake = stake; }

    public void setFolded(boolean folded) { this.folded = folded; }

    public double getStakeToBankRatio(){
        return ( (double) getStake() / (getBank() + getStake()));
    }

    public void takePot(PotTexasHoldem pot) {
        System.out.println(getName() + " takes pot of " + addCount(pot.getTotal(), "chip", "chips"));
        bank += pot.takePot();
    }

    public void sharePot(PotTexasHoldem pot, int numWinners){
        int share = pot.sharePot(numWinners);
        System.out.println(getName() + " takes their share of " + addCount(share, "chip", "chips") +
                " from the pot");
        bank += share;
    }

    public void fold(PotTexasHoldem pot) {
        if (!folded)
            System.out.println("\n> " + getName() + " says: I fold!\n");

        folded = true;
    }

    public void seeBet(PotTexasHoldem pot) {
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

    public void raiseBet(PotTexasHoldem pot, int amount) {
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

    //--------------------------------------------------------------------//
    //--------------------------------------------------------------------//
    // Key decisions a player must make
    //--------------------------------------------------------------------//
    //--------------------------------------------------------------------//

    protected abstract boolean shouldSee(PotTexasHoldem pot);
    protected abstract boolean shouldRaise(PotTexasHoldem pot);

    protected abstract int raiseAmount(PotTexasHoldem pot);

    protected abstract boolean shouldAllIn(PotTexasHoldem pot);

    public void nextAction(PotTexasHoldem pot) {
        boolean canCheck = true;

        if (hasFolded()) return;  // no longer in the game

        if(pot.getCurrentStake() >= getStake() + getBank()) {
            if(shouldAllIn(pot)) {
                allIn(pot);
            } else{
                fold(pot);
            }
        }
        else if(!isAllIn()){
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


    //--------------------------------------------------------------------//
    //--------------------------------------------------------------------//
    // Allowable player actions in Poker
    //--------------------------------------------------------------------//
    //--------------------------------------------------------------------//

    //Different method to open bet
    public boolean postBlind(PotTexasHoldem pot, int blindAmt, String type) {
        if (bank < blindAmt) return false;

        stake += blindAmt;
        bank -= blindAmt;

        pot.addBlind(blindAmt);

        System.out.println("\n> " + getName() + " posts the " + type + " with "+ addCount(blindAmt, "chip", "chips") + "\n");
        return true;
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
