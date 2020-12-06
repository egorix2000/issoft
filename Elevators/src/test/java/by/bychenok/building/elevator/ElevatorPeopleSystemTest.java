package by.bychenok.building.elevator;

import by.bychenok.building.floor.Floor;
import by.bychenok.person.Person;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.LinkedBlockingQueue;

import static by.bychenok.building.elevator.ElevatorState.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ElevatorPeopleSystemTest {

    @Test
    void unload_empty_success() {
        //GIVEN
        ElevatorPeopleSystem system = new ElevatorPeopleSystem(1, 100);
        system.unload(0);

        //EXPECT
        assertEquals(system.getNumberOfPassengers(), 0);
    }

    @Test
    void unload_noPeopleToUnload_success() {
        //GIVEN
        ElevatorsManager elevatorsManager = mock(ElevatorsManager.class);
        ElevatorPeopleSystem system = new ElevatorPeopleSystem(1, 100);
        Floor floor = new Floor(1, new LinkedBlockingQueue<>());
        Set<Integer> stopFloors = new HashSet<>();
        int unloadFloor = 3;
        Person anotherFloorPerson =
                new Person(UUID.randomUUID(), 3, 5, 10);
        floor.addPerson(anotherFloorPerson, elevatorsManager);
        system.load(floor, stopFloors, CARRYING_UP);
        system.unload(unloadFloor);

        //EXPECT
        assertEquals(system.getNumberOfPassengers(), 1);
    }

    @Test
    void unload_allPeople_success() {
        //GIVEN
        ElevatorsManager elevatorsManager = mock(ElevatorsManager.class);
        ElevatorPeopleSystem system = new ElevatorPeopleSystem(1, 100);
        Set<Integer> stopFloors = new HashSet<>();
        Floor floor = new Floor(1, new LinkedBlockingQueue<>());
        int unloadFloor = 5;
        Person person1 =
                new Person(UUID.randomUUID(), 3, unloadFloor, 10);
        Person person2 =
                new Person(UUID.randomUUID(), 1, unloadFloor, 10);
        floor.addPerson(person1, elevatorsManager);
        floor.addPerson(person2, elevatorsManager);
        system.load(floor, stopFloors, CARRYING_UP);
        system.unload(unloadFloor);

        //EXPECT
        assertEquals(system.getNumberOfPassengers(), 0);
    }

    @Test
    void unload_notAllPeople_success() {
        //GIVEN
        ElevatorsManager elevatorsManager = mock(ElevatorsManager.class);
        ElevatorPeopleSystem system = new ElevatorPeopleSystem(1, 100);
        Set<Integer> stopFloors = new HashSet<>();
        Floor floor = new Floor(1, new LinkedBlockingQueue<>());
        int unloadFloor = 5;
        Person personRightFloor =
                new Person(UUID.randomUUID(), 3, unloadFloor, 10);
        Person personAnotherFloor =
                new Person(UUID.randomUUID(), 1, 6, 10);
        floor.addPerson(personRightFloor, elevatorsManager);
        floor.addPerson(personAnotherFloor, elevatorsManager);
        system.load(floor, stopFloors, CARRYING_UP);
        system.unload(unloadFloor);

        //EXPECT
        assertEquals(system.getNumberOfPassengers(), 1);
    }

    @Test
    void load_empty_success() {
        //GIVEN
        ElevatorPeopleSystem system = new ElevatorPeopleSystem(1, 100);
        Floor floor = new Floor(1, new LinkedBlockingQueue<>());
        Set<Integer> stopFloors = new HashSet<>();
        system.load(floor, stopFloors, CARRYING_UP);

        //EXPECT
        assertEquals(system.getNumberOfPassengers(), 0);
    }

    @Test
    void load_available_exceptionThrown() {
        //GIVEN
        ElevatorPeopleSystem system = new ElevatorPeopleSystem(1, 100);
        Floor floor = mock(Floor.class);
        Set<Integer> stopFloors = new HashSet<>();

        //EXPECT
        assertThrows(IllegalArgumentException.class,
                () -> system.load(floor, stopFloors, AVAILABLE));
    }

    @Test
    void load_peopleDifferentQueues_success() {
        //GIVEN
        ElevatorsManager elevatorsManager = mock(ElevatorsManager.class);
        ElevatorPeopleSystem system = new ElevatorPeopleSystem(1, 100);
        Set<Integer> stopFloors = new HashSet<>();
        Floor floor = new Floor(1, new LinkedBlockingQueue<>());
        Person person1 =
                new Person(UUID.randomUUID(), 3, 5, 10);
        Person person2 =
                new Person(UUID.randomUUID(), 10, 1, 10);
        floor.addPerson(person1, elevatorsManager);
        floor.addPerson(person2, elevatorsManager);
        system.load(floor, stopFloors, CARRYING_UP);

        //EXPECT
        assertEquals(system.getNumberOfPassengers(), 1);
    }

    @Test
    void load_peopleCloseToAndOverWeightLimit_success() {
        //GIVEN
        ElevatorsManager elevatorsManager = mock(ElevatorsManager.class);
        ElevatorPeopleSystem system = new ElevatorPeopleSystem(1, 100);
        Set<Integer> stopFloors = new HashSet<>();
        Floor floor = new Floor(1, new LinkedBlockingQueue<>());
        Person person =
                new Person(UUID.randomUUID(), 3, 5, 50);
        Person personCloseToLimit =
                new Person(UUID.randomUUID(), 1, 10, 50);
        Person personOverLimit =
                new Person(UUID.randomUUID(), 3, 7, 1);
        floor.addPerson(person, elevatorsManager);
        floor.addPerson(personCloseToLimit, elevatorsManager);
        floor.addPerson(personOverLimit, elevatorsManager);
        system.load(floor, stopFloors, CARRYING_UP);

        //EXPECT
        assertEquals(system.getNumberOfPassengers(), 2);
    }

    @Test
    void leaveFloor_down_success() {
        //GIVEN
        ElevatorPeopleSystem system = new ElevatorPeopleSystem(1, 100);
        ElevatorsManager elevatorsManager = mock(ElevatorsManager.class);
        Floor floor = mock(Floor.class);
        system.leaveFloor(CARRYING_DOWN, floor, elevatorsManager);

        //EXPECT
        verify(floor, times(1)).handleElevatorLeaveDownEvent(elevatorsManager);
    }

    @Test
    void leaveFloor_up_success() {
        //GIVEN
        ElevatorPeopleSystem system = new ElevatorPeopleSystem(1, 100);
        ElevatorsManager elevatorsManager = mock(ElevatorsManager.class);
        Floor floor = mock(Floor.class);
        system.leaveFloor(CARRYING_UP, floor, elevatorsManager);

        //EXPECT
        verify(floor, times(1)).handleElevatorLeaveUpEvent(elevatorsManager);
    }

    @Test
    void leaveFloor_available_exceptionThrown() {
        //GIVEN
        ElevatorPeopleSystem system = new ElevatorPeopleSystem(1, 100);
        ElevatorsManager elevatorsManager = mock(ElevatorsManager.class);
        Floor floor = mock(Floor.class);

        //EXPECT
        assertThrows(IllegalArgumentException.class,
                () -> system.leaveFloor(AVAILABLE, floor, elevatorsManager));
    }
}