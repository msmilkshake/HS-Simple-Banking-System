package banking;

import java.util.Random;
import java.util.stream.IntStream;

public class CardIssuer {
    private static final int ACC_NR_LENGTH = 9;
    private static final int PIN_LENGTH = 4;
    private static final Random RANDOM = new Random();

    public static Card issue(String bankId) {
        String accountNumber;
        String checksum;
        String pin;
        do {
            accountNumber = generateUniqueAccountNumber(bankId);
            checksum = getChecksum(bankId, accountNumber);
            pin = generatePin();
        } while (DB.getInstance().cardExists(
                bankId + accountNumber + checksum + pin));
        return new Card(bankId, accountNumber, checksum, pin);
    }

    private static String generateUniqueAccountNumber(String bankId) {
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
        int[] nums = (bankId + accountNumber).chars()
                .map(c -> c - '0').toArray();
        int checksum = 10 - IntStream.range(0, nums.length)
                .map(i -> i % 2 == 0 ? nums[i] >= 5 ? 1 +
                        (2 * (nums[i] - 5)) : nums[i] * 2 : nums[i])
                .sum() % 10;
        return "" + (checksum % 10);
    }
}

