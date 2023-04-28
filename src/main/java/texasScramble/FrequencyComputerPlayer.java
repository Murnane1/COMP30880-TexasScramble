package texasScramble;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class FrequencyComputerPlayer extends Player{

    private long wordKnowledge;     //can only use words with a frequency up to this value
    private WordFrequencyDictionary wordFrequencyDictionary;
    private ScrabbleDictionary dictionary;
    private Random dice			= new Random(System.currentTimeMillis());
    private int riskTolerance;  // willingness of a player to take risks and bluff
    private String word;



    public FrequencyComputerPlayer(String name, int money, long wordKnowledge, int riskTolerance ,WordFrequencyDictionary wordFrequencyDictionary, ScrabbleDictionary dictionary) {
        super(name, money);
        this.wordKnowledge = wordKnowledge;
        this.wordFrequencyDictionary = wordFrequencyDictionary;
        this.dictionary = dictionary;
        this.riskTolerance = riskTolerance;
    }

    @Override
    boolean shouldOpen(PotOfMoney pot) {
        return true;
    }

    @Override
    boolean shouldSee(PotOfMoney pot) {
        //TODO should be affected by stake:bank ratio
        word = chooseWord();
        if (getStake() == 0)
            return true;
        else
            return true;
    }

    @Override
    boolean shouldRaise(PotOfMoney pot) {
        //TODO should be affected by stake:bank ratio
        word = chooseWord();
        return false;
    }

    @Override
    boolean shouldAllIn(PotOfMoney pot) {
        word = chooseWord();
        if(pot.getCurrentStake() < getStake() + getBank()){
            return false;
        } else {
            return Math.abs(dice.nextInt()) % 100 + getBank() < getHand().getRiskWorthiness(word) +
                    getRiskTolerance();
        }
    }

    @Override
    boolean shouldChallenge(PotOfMoney pot, String word) {
        return false;
    }

    @Override
    String chooseWord() {               //TODO test
        ScrambleHand hand = getHand();
        List<String> possibleWords = hand.getPossibleWords(dictionary);
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
        return currBestWord;
    }

    private int getRiskTolerance() {
        return riskTolerance;
    }
}
