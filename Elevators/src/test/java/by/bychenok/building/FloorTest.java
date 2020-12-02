package by.bychenok.building;

import by.bychenok.building.floor.Floor;
import by.bychenok.person.Person;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class FloorTest {

    @Test
    void pollFromUpQueue_empty_fail() {
        // Add to down queue, poll from up queue
        //GIVEN
        Floor floor = new Floor(2);
        Person downPerson = new Person(UUID.randomUUID(), 1, 3, 2);
        floor.addToDownQueue(downPerson);

        //EXPECT
        assertThrows(IllegalStateException.class,
                floor::pollFromUpQueue);
    }

    @Test
    void pollFromUpQueue_success() {
        //GIVEN
        Floor floor = new Floor(2);
        Person person = new Person(UUID.randomUUID(), 1, 1, 2);
        floor.addToUpQueue(person);
        Person pooledPerson = floor.pollFromUpQueue();

        //EXPECT
        assertEquals(pooledPerson, person);
        assertTrue(floor.isUpEmpty());
    }

    @Test
    void pollFromDownQueue_empty_fail() {
        // Add to up queue, poll from down queue
        //GIVEN
        Floor floor = new Floor(2);
        Person upPerson = new Person(UUID.randomUUID(), 1, 1, 3);
        floor.addToUpQueue(upPerson);

        //EXPECT
        assertThrows(IllegalStateException.class,
                floor::pollFromDownQueue);
    }

    @Test
    void pollFromDownQueue_success() {
        //GIVEN
        Floor floor = new Floor(2);
        Person person = new Person(UUID.randomUUID(), 1, 4, 2);
        floor.addToDownQueue(person);
        Person pooledPerson = floor.pollFromDownQueue();

        //EXPECT
        assertEquals(pooledPerson, person);
        assertTrue(floor.isDownEmpty());
    }
}