package banking;

import java.util.Random;

public class CardIssuer {
    private static final int ACC_NR_LENGTH = 9;
    private static final int PIN_LENGTH = 4;
    private static final Random RANDOM = new Random();
    
    public static Card issue(String bankId) {
        String accountNumber;
        String checksum;
        String pin;
        Card card;
        do {
            accountNumber = generateUniqueAccountNumber();
            checksum = getChecksum(bankId, accountNumber);
            pin = generatePin();
            card = new Card(bankId, accountNumber, checksum, pin);
        } while (DB.getInstance().cardExists(card.toString()));
        return card;
    }
    
    private static String generateUniqueAccountNumber() {
        return generateDigits(ACC_NR_LENGTH);
    }
    
    private static String generatePin() {
        return generateDigits(PIN_LENGTH);
    }
    
    private static String generateDigits(int n) {
        StringBuilder digits = new StringBuilder(n);
        for (int i = 0; i < n; ++i) {
            digits.append((char) (RANDOM.nextInt(10) + '0'));
        }
        return digits.toString();
    }
    
    private static String getChecksum(String bankId, String accountNumber) {
        return Util.getLuhnDigit(bankId + accountNumber);
    }
}
