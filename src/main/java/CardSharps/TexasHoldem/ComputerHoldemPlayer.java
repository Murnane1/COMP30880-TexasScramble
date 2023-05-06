package CardSharps.TexasHoldem;


import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import CardSharps.Poker.*;

public class ComputerHoldemPlayer extends Player {
    public static final int VARIABILITY		= 50;
    private int riskTolerance				= 0;
    private Random dice						= new Random(System.currentTimeMillis());



    public ComputerHoldemPlayer(String name, int money) {
        super(name, money);

        riskTolerance = Math.abs(dice.nextInt())%VARIABILITY
                - VARIABILITY/2;
    }

    public int getRiskTolerance() {
        return riskTolerance - getStake(); // tolerance drops as stake increases
    }

    @Override
    public boolean shouldOpen(PotOfMoney pot) {
        return true;
    }

    @Override
    public boolean shouldSee(PotOfMoney pot) {
        if (getStake() == 0)
            return true;
        else
            return Math.abs(dice.nextInt())%100 < getHand().getRiskWorthiness() +
                    getRiskTolerance();
    }

    @Override
    public boolean shouldRaise(PotOfMoney pot) {
        return Math.abs(dice.nextInt()) % 80 < getHand().getRiskWorthiness() +
                getRiskTolerance();
    }

    @Override
    public boolean shouldAllIn(PotOfMoney pot) {
        if(pot.getCurrentStake() < getStake() + getBank()){
            return false;
        } else {
            /*return Math.abs(dice.nextInt()) % 100 + getBank() < getHand().getRiskWorthiness() +
                    getRiskTolerance();*/
            return true;
        }
    }
}
