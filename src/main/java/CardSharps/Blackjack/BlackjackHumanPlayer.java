package CardSharps.Blackjack;

import CardSharps.Poker.*;

public class BlackjackHumanPlayer extends BlackjackPlayer { //error caused by abstract methods such as shouldSplit etc
    //constructor
    public BlackjackHumanPlayer(String name, int money){
        super(name, money);
    }

    // todo (verify) DECISIONS
    public boolean askQuestion(String question) {
        System.out.print("\n>> " + question + "(y/n)? : ");
        byte[] input = new byte[100];
        try {
            System.in.read(input);

            for (int i = 0; i < input.length; i++)
                if ((char) input[i] == 'y' || (char) input[i] == 'Y')
                    return true;
        } catch (Exception e) {
        };
        return false;
    }

    public boolean shouldDouble(BlackjackDeck deck, int handIndex, Card dealerCard) {
        if(getBank() >= getStake(handIndex)*2){
            if(askQuestion("Would you like to DOUBLE DOWN?\n" + "Current hand value: " + getHand(handIndex).getHandValue() + "\n" + "Current Stakes: " + getStake(handIndex) + "\n")){
                return true;
            }
        }
        return false;
    }


    public boolean shouldSplit(BlackjackDeck deck, int handIndex, Card dealerCard){
        if(getBank() >= getStake(handIndex)*2)
            if (askQuestion("Would you like to SPLIT?\n" + "Current hand value: " + getHand(handIndex).getHandValue() + "\n" + "Current Stakes: " + getStake(handIndex) + "\n")){
                return true;
            } else {
                return false;
            }
        else
            return false;
    }

    public boolean shouldHit(BlackjackDeck deck, int handIndex, Card dealerCard) { // TODO---------------
         if(askQuestion("Would you like to HIT?\n" + "Current hand value: " + getHand(handIndex).getHandValue() + "\n")){
            return true;
         }
         else{
            return false;
         }
    }

    public boolean shouldStand(BlackjackDeck deck, int handIndex, Card dealerCard){
        if(askQuestion("Would you like to STAND?\n" + "Current hand value: " + getHand(handIndex).getHandValue() + "\n")){
            return true;
        }
        else{
            return false;
        }
    }


}
