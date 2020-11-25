package by.bychenok.person;

import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class PersonTest {

    @Test
    void Person_negativeAndZeroWeight_fail() {
        //GIVEN
        UUID uuid = UUID.randomUUID();
        int negativeWeight = -1;
        int zeroWeight = 0;
        int floor = 1;
        int destinationFloor = 7;

        //EXPECT
        assertThrows(IllegalArgumentException.class,
                () -> new Person(uuid, negativeWeight, floor, destinationFloor));

        //EXPECT
        assertThrows(IllegalArgumentException.class,
                () -> new Person(uuid, zeroWeight, floor, destinationFloor));
    }

    @Test
    void Person_floorsEquality_fail() {
        //GIVEN
        UUID uuid = UUID.randomUUID();
        int weight = 10;
        int floor = 2;
        int destinationFloor = 2;

        //EXPECT
        assertThrows(IllegalArgumentException.class,
                () -> new Person(uuid, weight, floor, destinationFloor));
    }

    @Test
    void Person_success() {
        //GIVEN
        UUID uuid = UUID.randomUUID();
        int weight = 10;
        int floor = 2;
        int destinationFloor = 5;

        //EXPECT
        assertDoesNotThrow(
                () -> new Person(uuid, weight, floor, destinationFloor));
    }

}