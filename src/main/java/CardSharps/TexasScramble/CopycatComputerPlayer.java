package CardSharps.TexasScramble;

public class CopycatComputerPlayer extends ComputerScramblePlayer{

    private double previousPotStake = 0;
    private double previousNumActive;

    public CopycatComputerPlayer(String name, int money, long wordKnowledge, int riskTolerance, WordFrequencyDictionary wordFrequencyDictionary) {
        super(name, money, wordKnowledge, riskTolerance, wordFrequencyDictionary);
    }

    public double getPreviousPotStake() {
        return previousPotStake;
    }

    public double getPreviousNumActive() {
        return previousNumActive;
    }

    public double remainingPercentage(PotOfMoney pot) {
        return pot.getActivePlayers().size() / getPreviousNumActive();
    }

    public void updateTrackers(PotOfMoney pot){
        previousPotStake = pot.getCurrentStake();
        previousNumActive = pot.getActivePlayers().size();
    }

    @Override
    boolean shouldSee(PotOfMoney pot) {
        if(remainingPercentage(pot)*100 > getRiskTolerance()){
            return true;    //if the percentage of players remaining is greater than their risk tolerance see bet
        } else {
            return false;
        }
    }

    @Override
    boolean shouldRaise(PotOfMoney pot) {
        double increaseStake = pot.getCurrentStake() / getPreviousPotStake();
        if(increaseStake / pot.getActivePlayers().size() <= 2 || getPreviousPotStake() == 0){
            updateTrackers(pot);
            return false;        //if the average increase is over 2 for each player raise bet
        } else {
            return true;
        }
    }

    @Override
    int raiseAmount(PotOfMoney pot) {
        double averageIncrease =  (pot.getCurrentStake() / getPreviousPotStake())/pot.getActivePlayers().size();
        updateTrackers(pot);
        return (int) averageIncrease - 1;
    }

    @Override
    boolean shouldAllIn(PotOfMoney pot) {
        if(shouldSee(pot)){
            return true;
        } else {
            updateTrackers(pot);
            return false;
        }
    }
}
