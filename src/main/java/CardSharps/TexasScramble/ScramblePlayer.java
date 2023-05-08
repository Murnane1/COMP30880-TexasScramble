package CardSharps.TexasScramble;

import CardSharps.TexasHoldem.*;

public abstract class ScramblePlayer extends Player {
    ScrambleHand hand = null;
    private String word = null;
    private int wordScore = 0;

    //--------------------------------------------------------------------//
    //--------------------------------------------------------------------//
    // Constructor
    //--------------------------------------------------------------------//
    //--------------------------------------------------------------------//

    public ScramblePlayer(String name, int money) {
        super(name, money);
        reset();
    }

    //--------------------------------------------------------------------//
    //--------------------------------------------------------------------//
    // Reset internal state for start of new hand of poker
    //--------------------------------------------------------------------//
    //--------------------------------------------------------------------//

    @Override
    public void reset() {
        setFolded(false);
        setStake(0);
        word = null;
        wordScore = 0;
    }

    public String getWord() {
        return word;
    }

    public int getWordScore() {
        return wordScore;
    }

    public ScrambleHand getHand() {
        return hand;
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

    public void setHand(ScrambleHand hand) {
        this.hand = hand;
    }

    public void setWordScore(int wordScore) {
        this.wordScore = wordScore;
    }

    public void penalty(int penaltyValue, PotTexasHoldem pot) {
        reduceBank(penaltyValue);
        pot.addToPot(penaltyValue);
    }

    abstract boolean shouldChallenge(PotTexasHoldem pot, String word);

    abstract void chooseWord();

    @Override
    public void takePot(PotTexasHoldem pot) {
        System.out.println(getName() + " takes pot of " + addCount(pot.getTotal(), "chip", "chips"));
        if(getWord() != null) {
            System.out.println("Their word was \"" + getWord() + "\" with a score of " + getWordScore());
        }
        increaseBank(pot.takePot());
    }

    @Override
    public void sharePot(PotTexasHoldem pot, int numWinners){
        int share = pot.sharePot(numWinners);
        System.out.println(getName() + " takes their share of " + addCount(share, "chip", "chips") +
                " from the pot\nTheir word was \"" + getWord() + "\" with a score of " + getWordScore());
        increaseBank(share);
    }
}