package banking;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.IntStream;

public class Util {

    // Sad... Can't save PIN as hashes in this stage... (ಥ︵ಥ)
    public static String sha256Hash(String s) {
        StringBuilder hexStr = new StringBuilder(64);
        try {
            byte[] hash = MessageDigest.getInstance("SHA-256")
                    .digest(s.getBytes(StandardCharsets.UTF_8));
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) {
                    hexStr.append('0');
                }
                hexStr.append(hex);
            }
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return hexStr.toString();
    }

    public static Map<String, String> getArgsMap(String[] args) {
        Map<String, String> argsMap = new HashMap<>();
        for (int i = 0; i < args.length; ++i) {
            if (args[i].startsWith("-")) {
                String key = args[i].substring(1);
                String val;
                if (i + 1 < args.length && !args[i + 1].startsWith("-")) {
                    val = args[++i];
                } else {
                    val = "";
                }
                argsMap.put(key, val);
            }
        }
        return argsMap;
    }

    public static String getLuhnDigit(String number) {
        int[] nums =number.chars()
                .map(c -> c - '0').toArray();
        int checksum = 10 - IntStream.range(0, nums.length)
                .map(i -> i % 2 == 0 ? nums[i] >= 5 ? 1 +
                        (2 * (nums[i] - 5)) : nums[i] * 2 : nums[i])
                .sum() % 10;
        return "" + (checksum % 10);
    }
}
