package texasScramble;

public class HumanScramblePlayer extends Player {
    public HumanScramblePlayer(String name, int money) {
        super(name, money);
    }

    @Override
    boolean shouldOpen(PotOfMoney pot) {
        return false;
    }

    @Override
    boolean shouldSee(PotOfMoney pot) {
        return false;
    }

    @Override
    boolean shouldRaise(PotOfMoney pot) {
        return false;
    }

    @Override
    boolean shouldAllIn(PotOfMoney pot) {
        return false;
    }
}
