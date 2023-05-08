package CardSharps.TexasScramble;

import CardSharps.TexasHoldem.*;

public class FrequencyComputerPlayer extends ComputerScramblePlayer{


    public FrequencyComputerPlayer(String name, int money, long wordKnowledge, int riskTolerance , WordFrequencyDictionary wordFrequencyDictionary) {
        super(name, money, wordKnowledge, riskTolerance, wordFrequencyDictionary);
    }

    @Override
    public boolean shouldSee(PotTexasHoldem pot) {
        if (getStake() == 0)
            return true;
        else
            return getBetWorthiness(pot, 35) > 0;
    }

    @Override
    public boolean shouldRaise(PotTexasHoldem pot) {
        return getBetWorthiness(pot, 65) > 0;
    }

    @Override
    public int raiseAmount(PotTexasHoldem pot) {
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
    public boolean shouldAllIn(PotTexasHoldem pot) {
        if(pot.getCurrentStake() < getStake() + getBank()){
            return false;
        } else {
            return getBetWorthiness(pot, 25) > pot.getCurrentStake() - getStake();       // all in factors the amount the player has to raise by to go all in more than other actions
        }
    }
}