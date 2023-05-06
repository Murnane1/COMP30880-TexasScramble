package CardSharps.TexasScramble;

public abstract class Player {
    private int bank = 0;         // the total amount of money the player has left, not counting his/her
    private int stake = 0;         // the amount of money the player has thrown into the current pot
    private String name = "Player";  // the unique identifying name given to the player
    private ScrambleHand hand = null;      // the hand dealt to this player
    private boolean folded = false;     // set to true when the player folds (gives up)
    private boolean allIn = false;
    private String word = null;
    private int wordScore = 0;

    //--------------------------------------------------------------------//
    //--------------------------------------------------------------------//
    // Constructor
    //--------------------------------------------------------------------//
    //--------------------------------------------------------------------//

    public Player(String name, int money) {
        this.name = name;
        bank = money;
        reset();
    }

    //--------------------------------------------------------------------//
    //--------------------------------------------------------------------//
    // Reset internal state for start of new hand of poker
    //--------------------------------------------------------------------//
    //--------------------------------------------------------------------//

    public void reset() {
        folded = false;
        allIn = false;
        stake = 0;
        hand = null;
        word = null;
        wordScore = 0;
    }


    //--------------------------------------------------------------------//
    //--------------------------------------------------------------------//
    // Display Behaviour
    //--------------------------------------------------------------------//
    //--------------------------------------------------------------------//

    public String toString() {
        if (hasFolded())
            return "> " + getName() + " has folded, and has " + addCount(getBank(), "chip", "chips") + " in the bank.";
        else if (isAllIn()) {
            return "> " + getName() + " is all in, and has " + addCount(getBank(), "chip", "chips") + " in the bank.";
        } else
            return "> " + getName() + " has " + addCount(getBank(), "chip", "chips") + " in the bank";
    }


    //--------------------------------------------------------------------//
    //--------------------------------------------------------------------//
    // Accessors
    //--------------------------------------------------------------------//
    //--------------------------------------------------------------------//

    public ScrambleHand getHand() {
        return hand;
    }

    public int getBank() {
        return bank;
    }


    public int getStake() {
        return stake;
    }


    public String getName() {
        return name;
    }

    public String getWord() {
        return word;
    }

    public int getWordScore() {
        return wordScore;
    }

    public boolean isBankrupt() {
        return bank == 0;
    }

    public boolean hasFolded() {
        return folded;
    }

    public boolean isAllIn() {
        return allIn;
    }

    public double getStakeToBankRatio(){
        return getStake() / (getBank() + getStake());
    }

    //--------------------------------------------------------------------//
    //--------------------------------------------------------------------//
    // Modifiers
    //--------------------------------------------------------------------//
    //--------------------------------------------------------------------//

    public void dealTo(BagOfTiles bag, ScrabbleDictionary dictionary) {
        hand = bag.dealHand(dictionary);
    }

    public void setWord(String word){
        this.word = word;
    }

    public void setWordScore(int wordScore) {
        this.wordScore = wordScore;
    }

    public void setHand(ScrambleHand hand){
        this.hand = hand;
    }

    public void reduceStake(int reduction){
        stake -= reduction;
    }

    public void takePot(PotOfMoney pot) {
        System.out.println(getName() + " takes pot of " + addCount(pot.getTotal(), "chip", "chips"));
        if(getWord() != null) {
            System.out.println("Their word was \"" + getWord() + "\" with a score of " + getWordScore());
        }
        bank += pot.takePot();
    }

    public void sharePot(PotOfMoney pot, int numWinners){
        int share = pot.sharePot(numWinners);
        System.out.println(getName() + " takes their share of " + addCount(share, "chip", "chips") +
                " from the pot\nTheir word was \"" + getWord() + "\" with a score of " + getWordScore());
        bank += share;
    }

    public void penalty(int penaltyValue, PotOfMoney pot) {
        bank -= penaltyValue;
        pot.addToPot(penaltyValue);
    }

    //--------------------------------------------------------------------//
    //--------------------------------------------------------------------//
    // Allowable player actions in Poker
    //--------------------------------------------------------------------//
    //--------------------------------------------------------------------//

    public void fold(PotOfMoney pot) {
        if (!folded)
            System.out.println("\n> " + getName() + " says: I fold!\n");

        folded = true;
    }

    public boolean postBlind(PotOfMoney pot, int blindAmt, String type) {
        if (bank < blindAmt) return false;

        stake += blindAmt;
        bank -= blindAmt;

        pot.addBlind(blindAmt);

        System.out.println("\n> " + getName() + " posts the " + type + " with "+ addCount(blindAmt, "chip", "chips") + "\n");
        return true;
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

    public void allIn(PotOfMoney pot) {
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

    abstract boolean shouldOpen(PotOfMoney pot);

    abstract boolean shouldSee(PotOfMoney pot);

    abstract boolean shouldRaise(PotOfMoney pot);
    abstract int raiseAmount(PotOfMoney pot);

    abstract boolean shouldAllIn(PotOfMoney pot);

    abstract boolean shouldChallenge(PotOfMoney pot, String word);

    abstract void chooseWord();

    //--------------------------------------------------------------------//
    //--------------------------------------------------------------------//
    // Game actions are scheduled here
    //--------------------------------------------------------------------//
    //--------------------------------------------------------------------//

    public void nextAction(PotOfMoney pot) {
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

    //--------------------------------------------------------------------//
    //--------------------------------------------------------------------//
    // Some small but useful helper routines
    //--------------------------------------------------------------------//
    //--------------------------------------------------------------------//

    public String addCount(int count, String singular, String plural) {
        if (count == 1 || count == -1)
            return count + " " + singular;
        else
            return count + " " + plural;
    }
}