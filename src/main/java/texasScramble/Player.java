package texasScramble;

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
            return "> " + getName() + " has  " + addCount(getBank(), "chip", "chips") + " in the bank";
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
        /*if (isAllIn()) {
            return false;
        } else {*/
            return bank == 0;
        //}
    }


    public boolean hasFolded() {
        // has given up on the current hand

        return folded;
    }

    public boolean isAllIn() {
        return allIn;
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
        // when the winner of a hand takes the pot as his/her winnings

        System.out.println("\n> " + getName() + " says: I WIN " + addCount(pot.getTotal(), "chip", "chips") + "!\n");
        System.out.println("Winning hand: \n" + hand.toString());

        bank += pot.takePot();

        System.out.println(this);
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

    public void seeBet(PotOfMoney pot) {
        int needed  = pot.getCurrentStake() - getStake();

        if (needed == 0 || needed > getBank())
            return;

        stake += needed;
        bank  -= needed;

        pot.addToPot(needed);

        System.out.println("\n> " + getName() + " says: I see that " + addCount(needed, "chip", "chips") + "!\n");
    }


    public void raiseBet(PotOfMoney pot) {
        if (getBank() == 0) return;

        stake++;
        bank--;

        pot.raiseStake(1);

        System.out.println("\n> " + getName() + " says: and I raise you 1 chip!\n");
    }

    public void allIn(PotOfMoney pot) {
        stake += bank;
        bank = 0;
        allIn = true;
    }

    //--------------------------------------------------------------------//
    //--------------------------------------------------------------------//
    // Key decisions a player must make
    //--------------------------------------------------------------------//
    //--------------------------------------------------------------------//

    abstract boolean shouldOpen(PotOfMoney pot);

    abstract boolean shouldSee(PotOfMoney pot);

    abstract boolean shouldRaise(PotOfMoney pot);

    abstract boolean shouldAllIn(PotOfMoney pot);

    abstract boolean shouldChallenge(PotOfMoney pot, String word);

    abstract void chooseWord();

    //--------------------------------------------------------------------//
    //--------------------------------------------------------------------//
    // Game actions are scheduled here
    //--------------------------------------------------------------------//
    //--------------------------------------------------------------------//

    public void nextAction(PotOfMoney pot) {
        if (hasFolded()) return;  // no longer in the game

        if (isBankrupt() ) {
            // not enough money to cover the bet

            System.out.println("\n> " + getName() + " says: I'm out!\n");

            fold();

            return;
        }
        if(shouldAllIn(pot)) {
            allIn(pot);
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
            }

            else {
                System.out.println("\n> The pot total is " + pot.getTotal() + ". " + getName() + "'s stake is " + getStake());
                System.out.println("\n> " + getName() + " says: I check!\n");
            }
        }
    }

    //TODO method to deal community tiles (for Round)


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
