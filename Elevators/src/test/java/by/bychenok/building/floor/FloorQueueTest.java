package by.bychenok.building.floor;

import by.bychenok.building.elevator.ElevatorRequest;
import by.bychenok.building.elevator.ElevatorsManager;
import by.bychenok.building.statistics.StatisticsCollector;
import by.bychenok.person.Person;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;

import java.util.Optional;
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
        StatisticsCollector statisticsCollector = new StatisticsCollector(20);
        BlockingQueue<ElevatorRequest> requests = new LinkedBlockingQueue<>();
        ElevatorsManager elevatorsManager = mock(ElevatorsManager.class);
        FloorQueue people = new FloorQueue(1, UP, requests, statisticsCollector);
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
        StatisticsCollector statisticsCollector = new StatisticsCollector(20);
        BlockingQueue<ElevatorRequest> requests = new LinkedBlockingQueue<>();
        ElevatorsManager elevatorsManager = mock(ElevatorsManager.class);
        FloorQueue people = new FloorQueue(1, UP, requests, statisticsCollector);
        people.handleElevatorLeaveEvent(elevatorsManager);

        //EXPECT
        assertEquals(requests.size(), 0);
    }

    @Test
    void poll_heavyPerson_returnNull() {
        //GIVEN
        StatisticsCollector statisticsCollector = new StatisticsCollector(20);
        int personWeight = 10;
        int maxWeight = 10;
        BlockingQueue<ElevatorRequest> requests = new LinkedBlockingQueue<>();
        ElevatorsManager elevatorsManager = mock(ElevatorsManager.class);
        FloorQueue people = new FloorQueue(1, UP, requests, statisticsCollector);
        Person p = new Person(UUID.randomUUID(), 1, 2, personWeight);
        people.add(p, elevatorsManager);
        Optional<Person> polled = people.poll(maxWeight);

        //EXPECT
        assertFalse(polled.isPresent());
    }

    @Test
    void poll_empty_returnNull() {
        //GIVEN
        StatisticsCollector statisticsCollector = new StatisticsCollector(20);
        BlockingQueue<ElevatorRequest> requests = new LinkedBlockingQueue<>();
        FloorQueue people = new FloorQueue(1, UP, requests, statisticsCollector);
        Optional<Person> polled = people.poll(100);

        //EXPECT
        assertFalse(polled.isPresent());
    }

    @Test
    void poll_closeToHeavyPerson_success() {
        //GIVEN
        StatisticsCollector statisticsCollector = new StatisticsCollector(20);
        int personWeight = 10;
        int maxWeight = 11;
        BlockingQueue<ElevatorRequest> requests = new LinkedBlockingQueue<>();
        ElevatorsManager elevatorsManager = mock(ElevatorsManager.class);
        FloorQueue people = new FloorQueue(1, UP, requests, statisticsCollector);
        Person p = new Person(UUID.randomUUID(), 1, 2, personWeight);
        people.add(p, elevatorsManager);
        Optional<Person> polled = people.poll(maxWeight);

        //EXPECT
        assertTrue(polled.isPresent());

        //AND
        assertEquals(polled.get(), p);
    }
}