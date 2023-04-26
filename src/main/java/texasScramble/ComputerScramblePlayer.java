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
            return Math.abs(dice.nextInt()) % 100 + getBank() < getHand().getRiskWorthiness() +
                    getRiskTolerance();
        }
    }

    @Override
    boolean shouldChallenge(PotOfMoney pot, String word) {
        //TODO when does computer challenge word
        //increasing chance of challenging as word is further down sorted dictionary
        //challenge 85% of invalid words?
        return true;
    }

    @Override
    String chooseWord() {
        return "computerWord";
        //first contructable word from sorted wordlist
    }
}
