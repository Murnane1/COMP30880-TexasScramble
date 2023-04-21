package texasScramble;

public class HumanScramblePlayer extends Player {
    public HumanScramblePlayer(String name, int money) {
        super(name, money);
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
            return askQuestion("Do you want to see the bet of " +
                    addCount(pot.getCurrentStake() - getStake(), "chip", "chips"));
    }

    @Override
    public boolean shouldRaise(PotOfMoney pot) {
        return askQuestion("Do you want to raise the bet by 1 chip");
    }


    @Override
    public boolean shouldAllIn(PotOfMoney pot) {
        if(pot.getCurrentStake() < getStake() + getBank()){
            return false;
        } else {
            return askQuestion("Do you want to go all-in");
        }
    }

    public boolean askQuestion(String question) 	{
        System.out.print("\n>> " + question + " (y/n)?  ");

        byte[] input = new byte[100];

        try {
            System.in.read(input);

            for (int i = 0; i < input.length; i++)
                if ((char)input[i] == 'y' || (char)input[i] == 'Y')
                    return true;
        }
        catch (Exception e){};

        return false;
    }
}
