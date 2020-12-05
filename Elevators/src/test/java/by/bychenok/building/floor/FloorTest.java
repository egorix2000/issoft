package by.bychenok.building.floor;

import by.bychenok.building.elevator.ElevatorRequest;
import by.bychenok.building.elevator.ElevatorsManager;
import by.bychenok.person.Person;
import org.junit.jupiter.api.Test;

import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class FloorTest {

    @Test
    void addPerson_up_success() {
        //GIVEN
        BlockingQueue<ElevatorRequest> elevatorRequests = new LinkedBlockingQueue<>();
        ElevatorsManager elevatorsManager = mock(ElevatorsManager.class);
        Floor floor = new Floor(3, elevatorRequests);
        Person personUp = new Person(UUID.randomUUID(),3, 4, 10);
        floor.addPerson(personUp, elevatorsManager);
        Optional<Person> extractedPerson = floor.pollFromUpQueue(20);

        //EXPECT
        assertTrue(extractedPerson.isPresent());

        //AND
        assertEquals(personUp, extractedPerson.get());
    }

    @Test
    void addPerson_down_success() {
        //GIVEN
        BlockingQueue<ElevatorRequest> elevatorRequests = new LinkedBlockingQueue<>();
        ElevatorsManager elevatorsManager = mock(ElevatorsManager.class);
        Floor floor = new Floor(4, elevatorRequests);
        Person personDown = new Person(UUID.randomUUID(),4, 3, 10);
        floor.addPerson(personDown, elevatorsManager);
        Optional<Person> extractedPerson = floor.pollFromDownQueue(20);

        //EXPECT
        assertTrue(extractedPerson.isPresent());

        //AND
        assertEquals(personDown, extractedPerson.get());
    }

    @Test
    void addPerson_wrongFloor_exceptionThrown() {
        //GIVEN
        BlockingQueue<ElevatorRequest> elevatorRequests = new LinkedBlockingQueue<>();
        ElevatorsManager elevatorsManager = mock(ElevatorsManager.class);
        int floorNumber = 3;
        int personFloorNumber = 5;
        Floor floor = new Floor(floorNumber, elevatorRequests);
        Person personUp = new Person(UUID.randomUUID(),
                personFloorNumber,
                10,
                10);

        //EXPECT
        assertThrows(IllegalArgumentException.class,
                () -> floor.addPerson(personUp, elevatorsManager));
    }
}