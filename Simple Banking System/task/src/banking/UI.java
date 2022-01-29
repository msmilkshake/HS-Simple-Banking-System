package banking;

import java.util.Scanner;

public class UI {
    private static final Scanner SCN = new Scanner(System.in);
    private static final int CARD_DIGIT_LENGTH = 16;
    
    private final Bank BANK = new Bank("400000");
    
    public UI(String[] args) {
        DB.init(Util.getArgsMap(args).get("fileName"));
    }
    
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
                    createAccount();
                    break;
                case 2:
                    login();
                    break;
                case 0:
                    run = false;
                    break;
                default:
                    System.out.println("Invalid command.");
            }
            if (BANK.hasUserLoggedIn()) {
                run = sessionMenu();
            }
        }
    }
    
    private void createAccount() {
        Card card = BANK.createAccount();
        System.out.println("Your card number:\n" + card +
                "\nYour card PIN:\n" + card.getPin());
    }
    
    private void login() {
        System.out.println("Enter your card number:");
        String number = readLn();
        System.out.println("Enter your PIN:");
        String pin = readLn();
        BANK.login(number, pin);
        System.out.println(BANK.hasUserLoggedIn() ?
                "You have successfully logged in!" :
                "Wrong card number or PIN!");
    }
    
    private boolean sessionMenu() {
        boolean run = true;
        while (run) {
            printSessionMenu();
            int choice = readInt();
            switch (choice) {
                case 1:
                    checkBalance();
                    break;
                case 2:
                    depositMoney();
                    break;
                case 3:
                    transferMoney();
                    break;
                case 4:
                    closeAccount();
                    run = false;
                    break;
                case 5:
                    BANK.logout();
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
    
    private void closeAccount() {
        BANK.closeAccount();
    }
    
    private void transferMoney() {
        System.out.println("Transfer\n" +
                "Enter card number:");
        String number = readLn();
        if (number.length() != CARD_DIGIT_LENGTH ||
                !Util.getLuhnDigit(number.substring(0, number.length() - 1))
                        .equals(number.substring(number.length() - 1))) {
            System.out.println("Probably you made a mistake in the card number." +
                    " Please try again!");
            return;
        }
        if (number.equals(BANK.getSessionCard().toString())) {
            System.out.println("You can't transfer money to the same account!");
            return;
        }
        if (!BANK.isIssuedCard(number)) {
            System.out.println("Such a card does not exist.");
            return;
        }
        System.out.println("Enter how much money you want to transfer:");
        int amount = readInt();
        if (!BANK.transfer(number, amount)) {
            System.out.println("Not enough money!");
            return;
        }
        System.out.println("Success!");
    }
    
    private void depositMoney() {
        System.out.println("Enter income:");
        int money = readInt();
        if (BANK.deposit(money)) {
            System.out.println("Income was added!");
        }
    }
    
    private void checkBalance() {
        int balance = BANK.getCardBalance();
        System.out.println("Balance: " + balance);
    }
    
    private void printMainMenu() {
        System.out.println("" +
                "1. Create an account\n" +
                "2. Log into account\n" +
                "0. Exit");
    }
    
    private void printSessionMenu() {
        System.out.println("" +
                "1. Balance\n" +
                "2. Add income\n" +
                "3. Do transfer\n" +
                "4. Close account\n" +
                "5. Log out\n" +
                "0. Exit");
    }
    
    private static int readInt() {
        return Integer.parseInt(SCN.nextLine());
    }
    
    private static String readLn() {
        return SCN.nextLine();
    }
}
