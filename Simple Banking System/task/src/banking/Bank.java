package banking;

import java.util.HashMap;
import java.util.Map;

public class Bank {
    private final String BANK_ID;

    private Map<String, Card> issuedCards = new HashMap<>();

    public Bank(String bankId) {
        BANK_ID = bankId;
    }

    public String createAccount() {
        Card card = CardIssuer.issue(BANK_ID);
        String pin = CardIssuer.getLastGeneratedPin();
        issuedCards.put(card.toString(), card);
        return "Your card number:\n" + card + "\n" +
                "Your card PIN:\n" + pin;
    }

    public boolean login(String cardNumber, String pin) {
        if (issuedCards.containsKey(cardNumber)) {
            return issuedCards.get(cardNumber).isCorrectPin(pin);
        }
        return false;
    }

    public int getCardBalance(String cardNumber) {
        return issuedCards.get(cardNumber).getBalance();
    }
}
