package moe.pgnhd.theshop;

import java.security.SecureRandom;

public class Util {
    public static String randomString(int length) {
        SecureRandom secureRandom = new SecureRandom();
        String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwzyz0123456789";
        int min_ascii = 0;
        int max_ascii = alphabet.length()-1;
        StringBuilder sb = new StringBuilder(length);
        for(int i=0; i<length; i++) {
            char ch = alphabet.charAt(secureRandom.nextInt(min_ascii, max_ascii+1));
            sb.append(ch);
        }
        return sb.toString();
    }
}
