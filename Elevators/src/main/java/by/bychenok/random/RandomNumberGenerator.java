package by.bychenok.random;

import java.util.concurrent.ThreadLocalRandom;

import static com.google.common.base.Preconditions.checkArgument;

public class RandomNumberGenerator {

    public static int generateNumberInRangeWithoutValue(int min, int max, int excludeValue) {
        checkArgument(min < max,
                "Min value must be less than max");
        checkArgument(excludeValue >= min,
                "Exclude value can't be less than min");
        checkArgument(excludeValue < max,
                "Exclude value must be less than max");
        checkArgument(max-min > 1,
                "No interval to generate from");

        int generated = ThreadLocalRandom.current().nextInt(min, max - 1);
        if (generated >= excludeValue) {
            generated += 1;
        }
        return generated;
    }

}
