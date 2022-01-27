package banking;

import java.util.Scanner;

public class UI {
    private static final Scanner SCN = new Scanner(System.in);

    private final Bank BANK = new Bank("400000");

    private String sessionCard = "";

    public void start() {
        mainMenu();
        System.out.println("Bye!");
    }

    private void mainMenu() {
        boolean run = true;
        while (run) {
            printMainMenu();
            int choice = readInt();
            switch (choice) {
                case 1:
                    System.out.println(BANK.createAccount());
                    break;
                case 2:
                    if (loginAttempt()) {
                        System.out.println("You have successfully logged in!");
                        run = sessionMenu();
                    } else {
                        System.out.println("Wrong card number or PIN!");
                    }
                    break;
                case 0:
                    run = false;
                    break;
                default:
                    System.out.println("Invalid command.");
            }
        }
    }

    private boolean loginAttempt() {
        System.out.println("Enter your card number:");
        String cardNumber = readLn();
        System.out.println("Enter your PIN:");
        String pin = readLn();
        if (BANK.login(cardNumber, pin)) {
            sessionCard = cardNumber;
            return true;
        }
        return false;
    }

    private boolean sessionMenu() {
        boolean run = true;
        while (run) {
            printSessionMenu();
            int choice = readInt();
            switch (choice) {
                case 1:
                    displayBalance();
                    break;
                case 2:
                    System.out.println("You have successfully logged out!");
                    run = false;
                    break;
                case 0:
                    return false;
                default:
                    System.out.println("Invalid command.");
            }
        }
        return true;
    }

    private void displayBalance() {
        int balance = BANK.getCardBalance(sessionCard);
        System.out.println("Balance: " + balance);
    }

    private void printMainMenu() {
        System.out.println("1. Create an account\n" +
                        "2. Log into account\n" +
                        "0. Exit");
    }

    private void printSessionMenu() {
        System.out.println("1. Balance\n" +
                "2. Log out\n" +
                "0. Exit");
    }

    private static int readInt() {
        return Integer.parseInt(SCN.nextLine());
    }

    private static String readLn() {
        return SCN.nextLine();
    }
}
