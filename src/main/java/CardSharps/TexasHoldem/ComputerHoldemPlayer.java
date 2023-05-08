package CardSharps.TexasHoldem;


import java.util.Random;

public class ComputerHoldemPlayer extends HoldemPlayer {
    private int riskTolerance				= 0;
    private final Random dice						= new Random(System.currentTimeMillis());



    public ComputerHoldemPlayer(String name, int money, int riskTolerance) {
        super(name, money);
        this.riskTolerance = riskTolerance;
    }

    public int getRiskTolerance() {
        return riskTolerance - getStake(); // tolerance drops as stake increases
    }

    @Override
    public boolean shouldSee(PotTexasHoldem pot) {
        if (getStake() == 0)
            return true;
        else
            return getBetWortiness(pot, 35) > 0;
    }

    @Override
    public boolean shouldRaise(PotTexasHoldem pot) {
        return getBetWortiness(pot, 65) > 0;
    }

    @Override
    public int raiseAmount(PotTexasHoldem pot) {
        if(getStakeToBankRatio() == 0){
            return getBetWortiness(pot, 0) / 25;           //decreasing the value the more of the bank invested
        } else if (getStakeToBankRatio() <= 0.25){              //prevent large investment at low stakes
            return (int) (getBetWortiness(pot, 0) / (70*getStakeToBankRatio()));
        }
        else {
            return (int) (getBetWortiness(pot, 0) / (50*getStakeToBankRatio()));
        }
    }


    @Override
    public boolean shouldAllIn(PotTexasHoldem pot) {
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
    public int getBetWortiness(PotTexasHoldem pot, int risk) {
        if(risk == 0){
            risk = 1;       //Prevent breaking game with risk of 0. Can't get the modulus of 0
        }

        return (int) (getRiskTolerance() + 100 * getStakeToBankRatio() + getHand().getBestHandValue() -
                (100 * pot.getAverageStakeToBankRatio() + 3 * pot.getActivePlayers().size() + 2 * (pot.getCurrentStake() - getStake()) + 30-10*(getHand().getBettingRound()) + Math.abs(dice.nextInt() % risk)));
    }
}
