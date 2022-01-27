package banking;

public class Card {
    private final String BANK_ID_NUMBER;
    private final String ACCOUNT_NUMBER;
    private final String CHECKSUM;
    private String pinHash;
    private int balance = 0;

    public Card(String bankIdNumber,String accountNumber, String checksum, String pin) {
        BANK_ID_NUMBER = bankIdNumber;
        ACCOUNT_NUMBER = accountNumber;
        CHECKSUM = checksum;
        pinHash = Util.sha256Hash(pin);
    }

    public boolean isCorrectPin(String pin) {
        return pinHash.equals(Util.sha256Hash(pin));
    }

    public int getBalance() {
        return balance;
    }

    @Override
    public String toString() {
        return "" + BANK_ID_NUMBER + ACCOUNT_NUMBER + CHECKSUM;
    }
}
