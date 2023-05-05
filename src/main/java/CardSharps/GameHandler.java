package CardSharps;

import java.sql.SQLOutput;
import java.util.Scanner;
import java.util.InputMismatchException;

import CardSharps.TexasHoldem.GameOfTexasHoldem;
import CardSharps.TexasScramble.GameOfTexasScramble;
import CardSharps.Blackjack.GameOfBlackjack;
import CardSharps.Poker.GameOfPoker;

public class GameHandler {
    public static void main(String[] args) {
        System.out.printf("Welcome to CardSharps!");
        Scanner scanner = new Scanner(System.in);
        System.out.println("Please select which game you want to play: ");
        System.out.println("1. Texas Holdem");
        System.out.println("2. BlackJack");
        System.out.println("3. Texas Scramble");
        System.out.println("4. Poker");

        int choice = 0;
        boolean validInput = false;
        while (!validInput){
            try {
                choice = scanner.nextInt();
                validInput = true;
            } catch (InputMismatchException e){
                scanner.nextLine();
            }

            if (choice < 1 || choice > 4){
                System.out.println("Invalid choice, please try again.");
                validInput = false;
            }
        }

        switch(choice){
            case 1:
                GameOfTexasHoldem.main(args);
                break;
            case 2:
                GameOfBlackjack.main(args);
                break;
            case 3:
                GameOfTexasScramble.main(args);
                break;
            case 4:
                GameOfPoker.main(args);
            default:
                System.out.println("Invalid choice. Please try again.");
                break;
        }
    }
}