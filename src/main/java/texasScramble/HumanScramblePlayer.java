package texasScramble;

import java.util.*;
import java.util.stream.Collectors;

public class HumanScramblePlayer extends Player {
    public HumanScramblePlayer(String name, int money) {
        super(name, money);
    }

    @Override
    boolean shouldOpen(PotOfMoney pot) {
        return true;
    }

    @Override
    public boolean shouldSee(PotOfMoney pot) {
        return askQuestion("Do you want to see the bet of " +
                addCount(pot.getCurrentStake() - getStake(), "chip", "chips"));
    }

    @Override
    public boolean shouldRaise(PotOfMoney pot) {
        return askQuestion("Do you want to raise the bet by 1 chip");
    }


    @Override
    public boolean shouldAllIn(PotOfMoney pot) {
        //TODO is there a way to let player know they won't have blind for next round
        if(pot.getCurrentStake() < getStake() + getBank()){
            return false;
        } else {
            return askQuestion("Do you want to go all-in");
        }
    }

    @Override
    public boolean shouldChallenge(PotOfMoney pot, String word) {
        return askQuestion("Do you want to challenge the word \"" + word + "\"");
    }

    @Override
    public void chooseWord() {
        boolean hasWord = false;
        String enteredWord = null;

        while (hasWord == false) {
            System.out.print("\n>> " + getName() + ", please enter your word: ");
            Scanner sc = new Scanner(System.in);
            enteredWord = sc.nextLine().toUpperCase();
            enteredWord.replace(" ","");
            hasWord = checkWord(enteredWord);
            //sc.close();
        }

        setWord(enteredWord);
        setWordScore(getHand().calculateWordValue(enteredWord));
    }

    public boolean checkWord(String testWord){
        testWord = testWord.toUpperCase();
        testWord = testWord.replace(" ","");

        //entered word to set of chars
        List<Character> enteredChars = testWord.chars()
                .mapToObj(e->(char)e).collect(Collectors.toList());

        List<Character> playerTiles = new ArrayList<>();
        for (Tile tile: getHand().getHand()) {
            playerTiles.add(tile.getLetter());
        }
        for (Tile tile: getHand().getCommunityTiles()) {
            playerTiles.add(tile.getLetter());
        }

        //does player have entered letters
        if(!playerTiles.containsAll(enteredChars)){
            System.out.println("You do not have the tiles to create this word. You can try again");
            return false;
        }

        //check each tile used max once
        for(Character letter : enteredChars){
            if (Collections.frequency(enteredChars, letter) > Collections.frequency(playerTiles, letter)){
                System.out.println("You can only use each tile once. Try another word");
                return false;
            }
        }

        return true;
    }

    public boolean askQuestion(String question) 	{
        System.out.print("\n>> " + question + " (y/n)?  ");

        boolean haveAnswer = false;
        while (!haveAnswer){
            byte[] input = new byte[100];
            try {
                System.in.read(input);

                for (int i = 0; i < input.length; i++)
                    if ((char)input[i] == 'y' || (char)input[i] == 'Y')
                        return true;
                    else if ((char)input[i] == 'n' || (char)input[i] == 'N')
                        return false;
            }
            catch (Exception e){};
            System.out.println("Please enter 'y' or 'n' to answer");
        }
        return false;
    }
}
