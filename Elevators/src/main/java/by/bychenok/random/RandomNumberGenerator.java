package by.bychenok.random;

import java.util.concurrent.ThreadLocalRandom;

import static com.google.common.base.Preconditions.*;

public class RandomNumberGenerator {

    public static int generateNumberInRangeWithoutValue(int min, int max, int excludeValue) {
        checkArgument(min < max,
                "Min value must be less than max value");
        checkArgument(excludeValue <= max && excludeValue >= min,
                "Exclude value must be between min and max values");
        int generated = ThreadLocalRandom.current().nextInt(min, max);
        if (generated >= excludeValue) {
            generated += 1;
        }
        return generated;
    }

}
