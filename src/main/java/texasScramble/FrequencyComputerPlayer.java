package texasScramble;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class FrequencyComputerPlayer extends Player{

    private long wordKnowledge;     //can only use words with a frequency above to this value (0 for perfect knowledge)
    private WordFrequencyDictionary wordFrequencyDictionary;
    private Random dice			= new Random(System.currentTimeMillis());
    private int riskTolerance;  // willingness of a player to take risks and bluff


    public FrequencyComputerPlayer(String name, int money, long wordKnowledge, int riskTolerance , WordFrequencyDictionary wordFrequencyDictionary) {
        super(name, money);
        this.wordKnowledge = wordKnowledge;
        this.wordFrequencyDictionary = wordFrequencyDictionary;
        this.riskTolerance = riskTolerance;
    }

    @Override
    boolean shouldOpen(PotOfMoney pot) {
        return true;
    }

    @Override
    boolean shouldSee(PotOfMoney pot) {
        //TODO should be affected by stake:bank ratio
        if (getStake() == 0)
            return true;
        else
            return true;
    }

    @Override
    boolean shouldRaise(PotOfMoney pot) {
        //TODO should be affected by stake:bank ratio
        return false;
    }

    @Override
    boolean shouldAllIn(PotOfMoney pot) {
        if(pot.getCurrentStake() < getStake() + getBank()){
            return false;
        } else {
            return Math.abs(dice.nextInt()) % 100 + getBank() < getHand().getHandQuality(getWord()) +
                    getRiskTolerance();
        }
    }

    @Override
    boolean shouldChallenge(PotOfMoney pot, String word) {
        int wordValue = getHand().calculateWordValue(word);
        if(wordValue > 20){
            if(word.length() < 5){
                return Math.abs(dice.nextInt()) % 1000 > getRiskTolerance();
            } else if (word.length() == 5 && !word.equals("QUACK") && !word.equals("QUICK")) {
                return Math.abs(dice.nextInt()) % 100 > getRiskTolerance();
            }
        }
        return false;
    }

    @Override
    void chooseWord() {               //TODO test
        ScrambleHand hand = getHand();
        List<String> possibleWords = hand.getPossibleWords();
        List<String> knownWords = new ArrayList<>();
        int currBestWordValue = 0;
        String currBestWord = null;

        for (String word: possibleWords) {
            if(wordFrequencyDictionary.getWordFrequency(word) >= wordKnowledge){
                knownWords.add(word);
                if(hand.calculateWordValue(word) > currBestWordValue){
                     currBestWordValue = hand.calculateWordValue(word);
                     currBestWord = word;
                }
            }
        }
        setWordScore(currBestWordValue);
        setWord(currBestWord);
        //return currBestWord;
    }

    private int getRiskTolerance() {
        return riskTolerance;
    }
}
