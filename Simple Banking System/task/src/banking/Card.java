package banking;

public class Card {
    private final String BANK_ID_NUMBER;
    private final String ACCOUNT_NUMBER;
    private final String CHECKSUM;
    private String pin;
    private int balance = 0;

    public Card(String bankIdNumber,String accountNumber, String checksum, String pin) {
        BANK_ID_NUMBER = bankIdNumber;
        ACCOUNT_NUMBER = accountNumber;
        CHECKSUM = checksum;
        this.pin = pin;
    }
    
    public Card(String number, String pin) {
        BANK_ID_NUMBER = number.substring(0, 6);
        ACCOUNT_NUMBER = number.substring(6, number.length() - 1);
        CHECKSUM = number.substring(number.length() - 1);
        this.pin = pin;
    }

    public String getPin() {
        return pin;
    }

    public int getBalance() {
        return balance;
    }

    public void setBalance(int balance) {
        this.balance = balance;
    }

    @Override
    public String toString() {
        return "" + BANK_ID_NUMBER + ACCOUNT_NUMBER + CHECKSUM;
    }
}
