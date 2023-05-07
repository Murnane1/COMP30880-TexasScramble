package CardSharps.TexasScramble;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static java.lang.Math.pow;
import static CardSharps.TexasScramble.ScrambleHand.ALL_LETTER_BONUS;
import static CardSharps.TexasScramble.ScrambleHand.TOTAL_TILES;

public class FrequencyComputerPlayer extends ComputerScramblePlayer{



    public FrequencyComputerPlayer(String name, int money, long wordKnowledge, int riskTolerance , WordFrequencyDictionary wordFrequencyDictionary) {
        super(name, money, wordKnowledge, riskTolerance, wordFrequencyDictionary);
    }

    @Override
    boolean shouldSee(PotOfMoney pot) {
        if (getStake() == 0)
            return true;
        else
            return getBetWorthiness(pot, 35) > 0;
    }

    @Override
    boolean shouldRaise(PotOfMoney pot) {
        return getBetWorthiness(pot, 65) > 0;
    }

    @Override
    int raiseAmount(PotOfMoney pot) {
        if(getStakeToBankRatio() == 0){
            return getBetWorthiness(pot, 0) / 25;           //decreasing the value the more of the bank invested
        } else if (getStakeToBankRatio() <= 0.25){              //prevent large investment at low stakes
            return (int) (getBetWorthiness(pot, 0) / (70*getStakeToBankRatio()));
        }
        else {
            return (int) (getBetWorthiness(pot, 0) / (50*getStakeToBankRatio()));
        }
    }


    @Override
    boolean shouldAllIn(PotOfMoney pot) {
        if(pot.getCurrentStake() < getStake() + getBank()){
            return false;
        } else {
            return getBetWorthiness(pot, 25) > pot.getCurrentStake() - getStake();       // all in factors the amount the player has to raise by to go all in more than other actions
        }
    }
}