package by.bychenok.building.floor;

import by.bychenok.building.elevator.ElevatorRequest;
import by.bychenok.building.elevator.ElevatorsManager;
import by.bychenok.person.Person;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.UUID;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import static by.bychenok.building.elevator.Direction.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class FloorQueueTest {

    @SneakyThrows
    @Test
    void handleElevatorLeaveEvent_notEmptyQueue_success() {
        //GIVEN
        BlockingQueue<ElevatorRequest> requests = new LinkedBlockingQueue<>();
        ElevatorsManager elevatorsManager = mock(ElevatorsManager.class);
        FloorQueue people = new FloorQueue(1, UP, requests);
        Person p = new Person(UUID.randomUUID(), 1, 2, 10);
        people.add(p, elevatorsManager);
        requests.take();
        people.handleElevatorLeaveEvent(elevatorsManager);

        //EXPECT
        assertEquals(requests.size(), 1);
    }

    @Test
    void handleElevatorLeaveEvent_emptyQueue_success() {
        //GIVEN
        BlockingQueue<ElevatorRequest> requests = new LinkedBlockingQueue<>();
        ElevatorsManager elevatorsManager = mock(ElevatorsManager.class);
        FloorQueue people = new FloorQueue(1, UP, requests);
        people.handleElevatorLeaveEvent(elevatorsManager);

        //EXPECT
        assertEquals(requests.size(), 0);
    }
}