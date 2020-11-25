package by.bychenok.person;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PersonGeneratorTest {

    @Test
    void PersonGenerator_MinFloorEqualsOrGreaterThanMax_fail() {
        //GIVEN
        int minFloorGreater = 3;
        int minFloorEquals = 2;
        int maxFloor = 2;
        int minWeight = 20;
        int maxWeight = 50;

        //EXPECT
        assertThrows(IllegalArgumentException.class,
                () -> new PersonGenerator(minFloorGreater, maxFloor, minWeight, maxWeight));

        //EXPECT
        assertThrows(IllegalArgumentException.class,
                () -> new PersonGenerator(minFloorEquals, maxFloor, minWeight, maxWeight));
    }

    @Test
    void PersonGenerator_MinWeightEqualsOrGreaterThanMax_fail() {
        //GIVEN
        int minFloor = 2;
        int maxFloor = 3;
        int minWeightGreater = 51;
        int minWeightEquals = 50;
        int maxWeight = 50;

        //EXPECT
        assertThrows(IllegalArgumentException.class,
                () -> new PersonGenerator(minFloor, maxFloor, minWeightGreater, maxWeight));

        //EXPECT
        assertThrows(IllegalArgumentException.class,
                () -> new PersonGenerator(minFloor, maxFloor, minWeightEquals, maxWeight));
    }

    @Test
    void PersonGenerator_success() {
        //GIVEN
        int minFloor = 2;
        int maxFloor = 3;
        int minWeight = 49;
        int maxWeight = 50;

        //EXPECT
        assertDoesNotThrow(
                () -> new PersonGenerator(minFloor, maxFloor, minWeight, maxWeight));

    }

}