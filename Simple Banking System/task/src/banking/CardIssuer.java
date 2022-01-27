package banking;

import java.util.*;
import java.util.stream.IntStream;

public class CardIssuer {
    private static final int ACC_NR_LENGTH = 9;
    private static final int PIN_LENGTH = 4;
    private static final Random RANDOM = new Random();
    private static String lastGeneratedPin;

    private static final Map<String, Set<String>> ACCOUNT_NUMBERS = new HashMap<>();

    public static Card issue(String bankId) {
        String accountNumber = generateUniqueAccountNumber(bankId);
        String checksum = getChecksum(bankId, accountNumber);
        lastGeneratedPin = generatePin();
        return new Card(bankId, accountNumber, checksum, lastGeneratedPin);
    }

    private static String generateUniqueAccountNumber(String bankId) {
        ACCOUNT_NUMBERS.putIfAbsent(bankId, new HashSet<>());
        String number;
        do {
            number = generateDigits(ACC_NR_LENGTH);
        } while (ACCOUNT_NUMBERS.get(bankId).contains(number));

        ACCOUNT_NUMBERS.get(bankId).add(number);
        return number;
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
                .map(i -> i % 2 == 0 ? nums[i] >= 5 ? 1 + (2 * (nums[i] - 5)) : nums[i] * 2 : nums[i])
                .sum() % 10;
        return "" + (checksum % 10);
    }

    public static String getLastGeneratedPin() {
        String pin = lastGeneratedPin;
        lastGeneratedPin = "";
        return pin;
    }
}

