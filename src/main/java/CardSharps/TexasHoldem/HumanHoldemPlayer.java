package CardSharps.TexasHoldem;

import java.util.Scanner;

public class HumanHoldemPlayer extends HoldemPlayer {

    public HumanHoldemPlayer(String name, int money) {
        super(name, money);
    }

    public boolean askQuestion(String question) 	{
        System.out.print("\n>> " + question + " (y/n)?  ");

        boolean haveAnswer = false;
        while (!haveAnswer){
            byte[] input = new byte[100];
            try {
                System.in.read(input);

                for (int i = 0; i < input.length; i++) {
                    if ((char) input[i] == 'y' || (char) input[i] == 'Y')
                        return true;
                    else if ((char) input[i] == 'n' || (char) input[i] == 'N')
                        return false;
                }
                System.out.println("\n");
            }
            catch (Exception e){e.printStackTrace();}
            System.out.println("Please enter 'y' or 'n' to answer");
        }
        return false;
    }

    @Override
    public boolean shouldSee(PotTexasHoldem pot) {
        return askQuestion("Do you want to see the bet of " +
                addCount(pot.getCurrentStake() - getStake(), "chip", "chips"));
    }

    @Override
    public boolean shouldRaise(PotTexasHoldem pot) {
        if(getBank() == 1){
            System.out.println("Note: Raising will make you All In");
        }
        return askQuestion("Do you want to raise the bet ");
    }

    @Override
    public int raiseAmount(PotTexasHoldem pot) {
        int enteredValue = -1;
        while (enteredValue < 0 || enteredValue > getBank()) {
            System.out.print("\n>> " + getName() + ", please enter how much you want to raise by: ");
            enteredValue = getInputValue();
            if(enteredValue > getBank()){
                System.out.println("You cannot to afford to raise by this much. You have " + addCount(getBank(), "chip", "chips") + " remaining");
            }
            if(enteredValue == getBank()){
                if(!askQuestion("Are you sure you want to go all in ")){
                    enteredValue = -1;
                }
            }
        }
        return enteredValue;
    }

    public int getInputValue(){
        Scanner sc = new Scanner(System.in);
        try {
            return sc.nextInt();
        } catch (Exception e){
            System.out.println("Invalid input. Please try again");
        }
        return -1;
    }

    @Override
    public boolean shouldAllIn(PotTexasHoldem pot) {
        if(pot.getCurrentStake() < getStake() + getBank()){
            return false;
        } else {
            return askQuestion("Do you want to go all-in ");
        }
    }
}
