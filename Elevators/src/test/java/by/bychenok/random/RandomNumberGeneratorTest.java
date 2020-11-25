package by.bychenok.random;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RandomNumberGeneratorTest {

    @Test
    void generateNumberInRangeWithoutValue_upperBound_success() {
        //GIVEN
        int min = 1;
        int max = 2;
        int excludeValue = 1;
        int value = RandomNumberGenerator.generateNumberInRangeWithoutValue(min, max, excludeValue);

        //EXPECT
        assertEquals(max, value);
    }

    @Test
    void generateNumberInRangeWithoutValue_lowerBound_success() {
        //GIVEN
        int min = 1;
        int max = 2;
        int excludeValue = 2;
        int value = RandomNumberGenerator.generateNumberInRangeWithoutValue(min, max, excludeValue);

        //EXPECT
        assertEquals(min, value);
    }

    @Test
    void generateNumberInRangeWithoutValue_minEqualsMax_fail() {
        //GIVEN
        int min = 5;
        int max = min;
        int excludeValue = 5;

        //EXPECT
        assertThrows(IllegalArgumentException.class,
                () -> RandomNumberGenerator.generateNumberInRangeWithoutValue(min, max, excludeValue));

    }

    @Test
    void generateNumberInRangeWithoutValue_minGreaterMax_fail() {
        //GIVEN
        int min = 5;
        int max = 2;
        int excludeValue = 5;

        //EXPECT
        assertThrows(IllegalArgumentException.class,
                () -> RandomNumberGenerator.generateNumberInRangeWithoutValue(min, max, excludeValue));

    }

    @Test
    void generateNumberInRangeWithoutValue_excludeValueNotInRange_fail() {
        //GIVEN
        int min = 2;
        int max = 5;
        int excludeValueGreater = 6;
        int excludeValueLess = 1;

        //EXPECT
        assertThrows(IllegalArgumentException.class,
                () -> RandomNumberGenerator.generateNumberInRangeWithoutValue(min, max, excludeValueGreater));

        assertThrows(IllegalArgumentException.class,
                () -> RandomNumberGenerator.generateNumberInRangeWithoutValue(min, max, excludeValueLess));

    }

    @Test
    void generateNumberInRangeWithoutValue_excludeValueOnBounds_success() {
        //GIVEN
        int min = 2;
        int max = 5;
        int excludeValueUpperBound = 5;
        int excludeValueLowerBound = 2;

        //EXPECT
        assertDoesNotThrow(
                () -> RandomNumberGenerator.generateNumberInRangeWithoutValue(min, max, excludeValueUpperBound));

        assertDoesNotThrow(
                () -> RandomNumberGenerator.generateNumberInRangeWithoutValue(min, max, excludeValueLowerBound));

    }
}