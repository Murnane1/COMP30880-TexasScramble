package CardSharps.Blackjack;

import CardSharps.Poker.*;
import java.util.Scanner;

public class BlackjackHumanPlayer extends BlackjackPlayer { //error caused by abstract methods such as shouldSplit etc
    //constructor
    public BlackjackHumanPlayer(String name, int money){
        super(name, money);
    }

    public String makeChoice(BlackjackDeck deck, int handIndex, Card dealerCard) {
        while (true) {
            System.out.println("\nYour current hand statistics: \nHand Value: " + getHand(handIndex).getHandValue() + "\nDealer Hand Value: " + dealerCard.getValue() + "\nCurrent Stakes: " + getStake(handIndex) + "\nHand Index: " + handIndex);
            System.out.println("What would you like to do?");
            System.out.println("1. Hit");
            System.out.println("2. Stand");
            System.out.println("3. Split");
            System.out.println("4. Double Down");
            System.out.print("Enter your choice (1-4): ");
            Scanner scanner = new Scanner(System.in);
            int choice = scanner.nextInt();
            switch (choice) {
                case 1:
                    if (shouldHit(deck, handIndex, dealerCard)) {
                        hit(deck, handIndex);
                        return "HIT";
                    } else {
                        System.out.println("Invalid input, Please try again.");
                        break;
                    }
                case 2:
                    if (shouldStand(deck, handIndex, dealerCard)) {
                        return "STAND";
                    } else {
                        System.out.println("Invalid input, Please try again.");
                        break;
                    }
                case 3:
                    if (shouldSplit(deck, handIndex, dealerCard)) {
                        split(deck, handIndex);
                        return "SPLIT";
                    } else {
                        System.out.println("Invalid input, Please try again.");
                        break;
                    }
                case 4:
                    if (shouldDouble(deck, handIndex, dealerCard)) {
                        doubleDown(deck, handIndex);
                        return "DOUBLE";
                    } else {
                        System.out.println("Invalid input, Please try again.");
                        break;
                    }
                default:
                    System.out.println("Invalid input, Please try again.");
                    break;
            }
        }
    }
    public boolean shouldDouble(BlackjackDeck deck, int handIndex, Card dealerCard) {
        if(getBank() >= getStake(handIndex)*2){
            return true;
        }
        return false;
    }


    public boolean shouldSplit(BlackjackDeck deck, int handIndex, Card dealerCard){
        if(getBank() >= getStake(handIndex)*2) {
            return true;
        }
        return false;
    }

    public boolean shouldHit(BlackjackDeck deck, int handIndex, Card dealerCard) { // TODO---------------
            return true;
    }

    public boolean shouldStand(BlackjackDeck deck, int handIndex, Card dealerCard){
        return true;
    }
}
