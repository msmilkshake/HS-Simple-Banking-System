package banking;

public class Bank {
    private final String BANK_ID;
    private State sessionState = State.LOGGED_OUT;
    private Card sessionCard = null;
    
    private enum State {
        LOGGED_IN,
        LOGGED_OUT
    }
    
    public Bank(String bankId) {
        BANK_ID = bankId;
    }
    
    public boolean hasUserLoggedIn() {
        return sessionState == State.LOGGED_IN;
    }
    
    public void logout() {
        sessionCard = null;
        sessionState = State.LOGGED_OUT;
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
    
    public boolean transfer(String cardNumber, int amount) {
        return DB.getInstance().transfer(sessionCard, cardNumber, amount);
    }
    
    public boolean isIssuedCard(String cardNumber) {
        return DB.getInstance().cardExists(cardNumber);
    }
    
    public boolean deposit(int money) {
        return DB.getInstance().addMoney(sessionCard, money) > 0;
    }
    
    public void closeAccount() {
        if (sessionCard == null) {
            return;
        }
        Card card = sessionCard;
        logout();
        DB.getInstance().deleteCard(card);
    }
    
    public int getCardBalance() {
        return DB.getInstance().getBalance(sessionCard);
    }
    
    public Card getSessionCard() {
        return sessionCard;
    }
}
