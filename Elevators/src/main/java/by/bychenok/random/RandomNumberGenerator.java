package by.bychenok.random;

import java.util.concurrent.ThreadLocalRandom;

public class RandomNumberGenerator {

    public static int generateNumberInRangeWithoutValue(int min, int max, int excludeValue) {
        int generated;
        if (excludeValue >= min && excludeValue < max) {
            generated = ThreadLocalRandom.current().nextInt(min, max - 1);
            if (generated >= excludeValue) {
                generated += 1;
            }
        } else {
            generated = ThreadLocalRandom.current().nextInt(min, max);
        }
        return generated;
    }

}
