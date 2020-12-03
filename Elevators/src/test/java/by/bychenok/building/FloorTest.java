package by.bychenok.building;

import by.bychenok.building.elevator.ElevatorRequest;
import by.bychenok.building.elevator.ElevatorsManager;
import by.bychenok.building.floor.Floor;
import by.bychenok.person.Person;
import org.junit.jupiter.api.Test;

import java.util.UUID;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import static org.junit.jupiter.api.Assertions.*;

class FloorTest {

    @Test
    void pollFromUpQueue_empty_success() {
        // Add to down queue, poll from up queue
        //GIVEN
        BlockingQueue<ElevatorRequest> requests = new LinkedBlockingQueue<>();
        ElevatorsManager elevatorsManager = new ElevatorsManager(requests, 10);
        Floor floor = new Floor(2, requests, elevatorsManager);
        Person downPerson = new Person(UUID.randomUUID(), 1, 3, 2);
        floor.addToDownQueue(downPerson);

        //EXPECT
        assertNull(floor.pollFromUpQueue());
    }

    @Test
    void pollFromUpQueue_success() {
        //GIVEN
        BlockingQueue<ElevatorRequest> requests = new LinkedBlockingQueue<>();
        ElevatorsManager elevatorsManager = new ElevatorsManager(requests, 10);
        Floor floor = new Floor(2, requests, elevatorsManager);
        Person person = new Person(UUID.randomUUID(), 1, 1, 2);
        floor.addToUpQueue(person);
        Person pooledPerson = floor.pollFromUpQueue();

        //EXPECT
        assertEquals(pooledPerson, person);
        assertTrue(floor.isUpEmpty());
    }

    @Test
    void pollFromDownQueue_empty_success() {
        // Add to up queue, poll from down queue
        //GIVEN
        BlockingQueue<ElevatorRequest> requests = new LinkedBlockingQueue<>();
        ElevatorsManager elevatorsManager = new ElevatorsManager(requests, 10);
        Floor floor = new Floor(2, requests, elevatorsManager);
        Person upPerson = new Person(UUID.randomUUID(), 1, 1, 3);
        floor.addToUpQueue(upPerson);

        //EXPECT
        assertNull(floor.pollFromDownQueue());

    }

    @Test
    void pollFromDownQueue_success() {
        //GIVEN
        BlockingQueue<ElevatorRequest> requests = new LinkedBlockingQueue<>();
        ElevatorsManager elevatorsManager = new ElevatorsManager(requests, 10);
        Floor floor = new Floor(2, requests, elevatorsManager);
        Person person = new Person(UUID.randomUUID(), 1, 4, 2);
        floor.addToDownQueue(person);
        Person pooledPerson = floor.pollFromDownQueue();

        //EXPECT
        assertEquals(pooledPerson, person);
        assertTrue(floor.isDownEmpty());
    }
}