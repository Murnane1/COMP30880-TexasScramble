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
        if (getStake() == 0)
            return true;
        else
            return getBetWortiness(pot, 35) > 0;
    }

    @Override
    boolean shouldRaise(PotOfMoney pot) {
        return getBetWortiness(pot, 65) > 0;
    }

    @Override
    int raiseAmount(PotOfMoney pot) {
        int amount = getBetWortiness(pot, 0)/200 * getBank();
        if(amount < 1){
            return 1;
        } else {
            return amount;
        }
    }


    @Override
    boolean shouldAllIn(PotOfMoney pot) {
        if(pot.getCurrentStake() < getStake() + getBank()){
            return false;
        } else {
            return getBetWortiness(pot, 25) > pot.getCurrentStake() - getStake();       // all in factors the amount the player has to raise by to go all in more than other actions
        }
    }

    /*
    *   Higher the value the better the bet
    *   All factors that encourage a larger bet minus the factors that make a bet more risky
    *   The risk integer is riskiness of given move. I.E raising is taking on a bigger risk than seeing a bet
    *   The highest value it can return is 300
    *   The lowest value it can return is -127 + provided risk integer
     */
    public int getBetWortiness(PotOfMoney pot, int risk) {
        if(risk == 0){
            risk = 1;       //Prevent breaking game with risk of 0. Can't get the modulus of 0
        }

        chooseWord();
        if (getWord() == null) {
            return (int) (getRiskTolerance() + 100 * getStakeToBankRatio() + getHand().getHandQuality("") -
                    (100 * pot.getAverageStakeToBankRatio() + 3 * pot.getActivePlayers().size() + 2 * (pot.getCurrentStake() - getStake()) + 30-10*(getHand().getBettingRound()) + Math.abs(dice.nextInt() % risk)));
        } else
            return (int) (getRiskTolerance() + 100 * getStakeToBankRatio() + getHand().getHandQuality(getWord()) -
                    (100 * pot.getAverageStakeToBankRatio() + 3 * pot.getActivePlayers().size() + 2 * (pot.getCurrentStake() - getStake()) + Math.abs(dice.nextInt() % risk)));
    }


    /*
     *       Each word length split into 3 categories
     *       1) Words that should almost always be challenged
     *       2) Slightly suspicious words
     *       3) Very normal words
     *
     *       The likelihood of being challenged is proportional to this
     *
     */
    @Override
    boolean shouldChallenge(PotOfMoney pot, String word) {
        int wordValue = getHand().calculateWordValue(word);
        if(wordFrequencyDictionary.getWordFrequency(word) > wordKnowledge || wordValue < getWordScore() || getWord().equals(word)) {
            return false;
        }

        int doubt = 0;
        if(word.length() == TOTAL_TILES){           //remove any all tiles bonus
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