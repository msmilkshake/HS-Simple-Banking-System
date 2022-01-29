package banking;

public class Bank {
    private final String BANK_ID;
    private String errorMessage;
    private State sessionState = State.LOGGED_OUT;
    private Card sessionCard = null;
    
    private enum State {
        LOGGED_IN,
        LOGGED_OUT
    }
    
    private enum Flag {
        WRONG_NUMBER,
        NOT_EXISTENT
    }
    
    public Bank(String bankId) {
        BANK_ID = bankId;
    }
    
    public boolean hasUserLoggedIn() {
        return sessionState == State.LOGGED_IN;
    }
    
    public void logout() {
        
    }

    public Card createAccount() {
        Card card = CardIssuer.issue(BANK_ID);
        DB.getInstance().insertCard(card);
        return card;
    }

    public void login(String cardNumber, String pin) {
        if (DB.getInstance().matchCardInfo(cardNumber, pin)) {
            sessionCard = new Card(cardNumber, pin);
            sessionState = State.LOGGED_IN;
        }
    }
    
    public boolean deposit(int money) {
        return DB.getInstance().addMoney(sessionCard, money) > 0;
    }

    public int getCardBalance() {
        return DB.getInstance().getBalance(sessionCard);
    }
}
