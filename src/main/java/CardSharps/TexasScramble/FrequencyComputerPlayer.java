package CardSharps.TexasScramble;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static java.lang.Math.pow;
import static CardSharps.TexasScramble.ScrambleHand.ALL_LETTER_BONUS;
import static CardSharps.TexasScramble.ScrambleHand.TOTAL_TILES;

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
        chooseWord();
        //TODO should be affected by stake:bank ratio
        if (getStake() == 0)
            return true;
        else
            return Math.abs(dice.nextInt())%80 < /*getHand().getHandQuality(getWord())*/ +
                    getRiskTolerance();
    }

    @Override
    boolean shouldRaise(PotOfMoney pot) {
        chooseWord();
        //TODO should be affected by stake:bank ratio
        return Math.abs(dice.nextInt())%60 < /*getHand().getHandQuality(getWord())*/ +
                getRiskTolerance();
    }

    @Override
    int raiseAmount() {
        return 2;
    }

    @Override
    boolean shouldAllIn(PotOfMoney pot) {
        chooseWord();
        if(pot.getCurrentStake() < getStake() + getBank()){
            return false;
        } else {
            return Math.abs(dice.nextInt()) % 50 + getBank() < /*getHand().getHandQuality(getWord())*/ +
                    getRiskTolerance();
        }
    }


    /*
     *       Each word length split into 3 categories
     *       1) Words that should almost always be challenged
     *       2) Slightly suspicious words
     *       3) Very normal words
     *
     *       The likelihood of being challenged is proporional to this
     *
     */
    @Override
    boolean shouldChallenge(PotOfMoney pot, String word) {
        int wordValue = getHand().calculateWordValue(word);
        if(wordFrequencyDictionary.getWordFrequency(word) > wordKnowledge || wordValue < getWordScore() || getWord().equals(word)) {
            return false;
        }

        int doubt = 0;
        if(word.length() == TOTAL_TILES){
            wordValue -= ALL_LETTER_BONUS;
        }

        if(word.length() >= 6){         //6 & 7 letter words relatively similar scores
            if(wordValue >= 28) {
                doubt = (int) (Math.abs(dice.nextInt()) % pow(wordValue, 4));
            } else if (wordValue >= 20) {
                doubt = (int) (Math.abs(dice.nextInt()) % pow(wordValue, 3));
            } else {
                doubt = (int) (Math.abs(dice.nextInt()) % pow(wordValue, 2));
            }
        }
        else if(word.length() == 5) {
            if(wordValue >= 21) {
                doubt = (int) (Math.abs(dice.nextInt()) % pow(wordValue, 4));
            } else if (wordValue >= 17) {
                doubt = (int) (Math.abs(dice.nextInt()) % pow(wordValue, 3));
            }else {
                doubt = (int) (Math.abs(dice.nextInt()) % pow(wordValue, 2));
            }
        }
        else if(word.length() == 4) {
            if(wordValue >= 20) {
                doubt = (int) (Math.abs(dice.nextInt()) % pow(wordValue, 4));
            } else if (wordValue >= 14) {
                doubt = (int) (Math.abs(dice.nextInt()) % pow(wordValue, 3));
            }else {
                doubt = (int) (Math.abs(dice.nextInt()) % pow(wordValue, 2));
            }
        }
        else if(word.length() == 3) {
            if(wordValue >= 19) {
                doubt = (int) (Math.abs(dice.nextInt()) % pow(wordValue, 4));
            } else if (wordValue >= 10) {
                doubt = (int) (Math.abs(dice.nextInt()) % pow(wordValue, 3));
            } else {
                doubt = (int) (Math.abs(dice.nextInt()) % pow(wordValue, 2));
            }
        } else {
            doubt =  Math.abs(dice.nextInt()) % 100;
        }

        if (getBank() < getRiskTolerance()){
            doubt -= 250;
        }
        return doubt > getRiskTolerance()*150;
    }

    @Override
    void chooseWord() {                 //sets the players word as the highest scoring one from their dictionary
        ScrambleHand hand = getHand();
        List<String> possibleWords = hand.getPossibleWords();
        List<String> knownWords = new ArrayList<>();
        int currBestWordValue = 0;
        String currBestWord = null;

        for (String word: possibleWords) {
            if(wordFrequencyDictionary.getWordFrequency(word) >= wordKnowledge){            //TODO add random chance of knowing word outside knowledge
                knownWords.add(word);
                if(hand.calculateWordValue(word) > currBestWordValue){
                    currBestWordValue = hand.calculateWordValue(word);
                    currBestWord = word;
                }
            }
        }
        if(currBestWord == null){
            //TODO try make up a word
            setWordScore(0);
            setWord("I CAN'T MAKE A WORD");
        }
        setWordScore(currBestWordValue);
        setWord(currBestWord);
    }

    private int getRiskTolerance() {
        return riskTolerance;
    }
}