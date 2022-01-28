package banking;

public class Bank {
    private final String BANK_ID;

    public Bank(String bankId) {
        BANK_ID = bankId;
    }

    public String createAccount() {
        Card card = CardIssuer.issue(BANK_ID);
        DB.getInstance().insertCard(card);
        return "Your card number:\n" + card + "\n" +
                "Your card PIN:\n" + card.getPin();
    }

    public boolean login(String cardNumber, String pin) {
        return DB.getInstance().queryCard(cardNumber, pin);
    }

    public int getCardBalance(String cardNumber) {
        return DB.getInstance().getBalance(cardNumber);
    }
}
