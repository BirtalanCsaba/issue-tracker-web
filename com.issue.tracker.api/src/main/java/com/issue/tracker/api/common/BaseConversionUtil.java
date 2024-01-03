package com.issue.tracker.api.common;

import java.util.ArrayList;
import java.util.List;

public class BaseConversionUtil {

    public static final char MIN_DIGIT = '0';

    public static final char MAX_DIGIT = 'Z';

    public static final String MIN_VALUE = "00000";

    public static final String MAX_VALUE = "ZZZZZ";

    public static final int BASE = 43;

    public static final int DEFAULT_RANKING_SIZE = 5;

    public static List<String> DEFAULT_RANKS = new ArrayList<>();


    public static long convertBase43to10(String value) {
        long decimalNumber = 0;
        int power = 0;

        for (int i = 0; i < value.length(); i++) {
            char currentDigit = value.charAt(i);

            long decimalValue = currentDigit - MIN_DIGIT;
            decimalNumber += (long) (decimalValue * Math.pow(BASE, power));
            power++;
        }
        return decimalNumber;
    }

    public static String convertBase10to43(long decimalNumber) {
        StringBuilder baseNumberBuilder = new StringBuilder();

        while (decimalNumber > 0) {
            int remainder = (int) (decimalNumber % BASE);
            char value = (char) (MIN_DIGIT + remainder);
            baseNumberBuilder.insert(0, value);
            decimalNumber /= BASE;
        }

        return baseNumberBuilder.toString();
    }

    public static void calculateDefaultRanks(long rankingSize) {
        long rankingStep = convertBase43to10(MAX_VALUE) / rankingSize;

        DEFAULT_RANKS.clear();
        DEFAULT_RANKS.add(MIN_VALUE);

        long curr = 0;
        for (int i = 0; i < DEFAULT_RANKING_SIZE - 1; i++) {
            curr = curr + rankingStep;
            String strValue = BaseConversionUtil.convertBase10to43(curr);
            DEFAULT_RANKS.add(strValue);
        }
    }
}
