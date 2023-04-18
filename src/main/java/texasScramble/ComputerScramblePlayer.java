package texasScramble;

import java.util.Random;

public class ComputerScramblePlayer extends Player {

    public static final int VARIABILITY		= 50;
    private int riskTolerance				= 0;  // willingness of a player to take risks and bluff
    private Random dice						= new Random(System.currentTimeMillis());

    public ComputerScramblePlayer(String name, int money) {
        super(name, money);

        riskTolerance = Math.abs(dice.nextInt())%VARIABILITY
                - VARIABILITY/2;
    }

    public int getRiskTolerance() {
        return riskTolerance - getStake(); // tolerance drops as stake increases
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
            return Math.abs(dice.nextInt())%100 < getHand().getRiskWorthiness() +
                    getRiskTolerance();
    }

    @Override
    boolean shouldRaise(PotOfMoney pot) {
        return Math.abs(dice.nextInt()) % 80 < getHand().getRiskWorthiness() +
                getRiskTolerance();
    }

    @Override
    boolean shouldAllIn(PotOfMoney pot) {
        if(pot.getCurrentStake() < getStake() + getBank()){
            return false;
        } else {
            return Math.abs(dice.nextInt()) % 100 + getBank() < getHand().getRiskWorthiness() +
                    getRiskTolerance();
        }
    }
}
