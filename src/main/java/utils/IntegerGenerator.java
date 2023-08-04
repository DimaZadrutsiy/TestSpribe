package utils;

import java.util.Random;

public class IntegerGenerator {
    public static int getValue(int min, int max) {
        Random random = new Random();
        return random.nextInt((max - min) + 1) + min;
    }
}