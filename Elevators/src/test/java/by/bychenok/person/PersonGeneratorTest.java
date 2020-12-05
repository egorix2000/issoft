package by.bychenok.person;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PersonGeneratorTest {

    @Test
    void personGenerator_minFloorGreaterOrEqualsToMaxFloor_exceptionThrown() {
        //GIVEN
        int minFloorGreater = 4;
        int minFloorEquals = 3;
        int maxFloor = 3;

        //EXPECT
        assertThrows(IllegalArgumentException.class,
                () -> new PersonGenerator(minFloorGreater, maxFloor, 1, 10));

        //AND
        assertThrows(IllegalArgumentException.class,
                () -> new PersonGenerator(minFloorEquals, maxFloor, 1, 10));
    }

    @Test
    void personGenerator_minWeightGreaterOrEqualsToMaxWeight_exceptionThrown() {
        //GIVEN
        int minWeightGreater = 41;
        int minWeightEquals = 40;
        int maxWeight = 40;

        //EXPECT
        assertThrows(IllegalArgumentException.class,
                () -> new PersonGenerator(minWeightGreater, maxWeight, 1, 10));

        //AND
        assertThrows(IllegalArgumentException.class,
                () -> new PersonGenerator(minWeightEquals, maxWeight, 1, 10));
    }

    @Test
    void personGenerator_closeMinMaxValues_success() {
        //GIVEN
        int validMinFloor = 1;
        int validMaxFloor = 2;
        int validMinWeight = 20;
        int validMaxWeight = 21;

        //EXPECT
        assertDoesNotThrow(
                () -> new PersonGenerator(validMinFloor, validMaxFloor,
                        validMinWeight, validMaxWeight));
    }
}