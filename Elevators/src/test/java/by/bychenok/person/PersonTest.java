package by.bychenok.person;

import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class PersonTest {

    @Test
    void person_negativeAndZeroWeight_exceptionThrown() {
        //GIVEN
        int negativeWeight = -1;
        int zeroWeight = 0;

        //EXPECT
        assertThrows(IllegalArgumentException.class,
                () -> new Person(UUID.randomUUID(), 1, 2, negativeWeight));

        //AND
        assertThrows(IllegalArgumentException.class,
                () -> new Person(UUID.randomUUID(), 1, 2, zeroWeight));
    }

    @Test
    void person_floorEqualsDestinationFloor_exceptionThrown() {
        //GIVEN
        int floor = 4;
        int destinationFloor = 4;

        //EXPECT
        assertThrows(IllegalArgumentException.class,
                () -> new Person(UUID.randomUUID(), floor, destinationFloor, 10));
    }

    @Test
    void person_success() {
        //GIVEN
        int validWeight = 10;
        int validFloor = 5;
        int validDestinationFloor = 3;

        //EXPECT
        assertDoesNotThrow(() -> new Person(UUID.randomUUID(),
                                            validFloor,
                                            validDestinationFloor,
                                            validWeight));
    }
}