package by.bychenok.building.floor;

import by.bychenok.building.elevator.ElevatorRequest;
import by.bychenok.building.elevator.ElevatorsManager;
import by.bychenok.person.Person;
import lombok.Getter;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.util.*;
import java.util.concurrent.BlockingQueue;

import static by.bychenok.building.elevator.Direction.*;

@Slf4j
public class Floor {

    @Getter
    private final int number;

    private FloorButton upButton;
    private FloorButton downButton;
    private final Queue<Person> peopleUp;
    private final Queue<Person> peopleDown;

    public Floor(int number, BlockingQueue<ElevatorRequest> requests) {
        upButton = new FloorButton(UP, number, requests);
        downButton = new FloorButton(DOWN, number, requests);
        peopleUp = new LinkedList<>();
        peopleDown = new LinkedList<>();
        this.number = number;
    }

    public void handleElevatorLeaveDownEvent(ElevatorsManager elevatorsManager) {
        downButton.reset();
        log.info("Button DOWN was reset on floor: {}", number);
        synchronized (peopleDown) {
            if (!peopleDown.isEmpty()) {
                downButton.press(elevatorsManager);
            }
        }
    }

    public void handleElevatorLeaveUpEvent(ElevatorsManager elevatorsManager) {
        upButton.reset();
        log.info("Button UP was reset on floor: {}", number);
        synchronized (peopleUp) {
            if (!peopleUp.isEmpty()) {
                upButton.press(elevatorsManager);
            }
        }
    }

    @SneakyThrows
    public void addToUpQueue(Person person, ElevatorsManager elevatorsManager) {
        synchronized (peopleUp) {
            peopleUp.add(person);
            log.info("Person with uuid: {} was added to up queue on floor: {}. " +
                    "Queue length: {}", person.getUuid(), number, peopleUp.size());
            upButton.press(elevatorsManager);
        }
    }

    @SneakyThrows
    public void addToDownQueue(Person person, ElevatorsManager elevatorsManager) {
        synchronized (peopleDown) {
            peopleDown.add(person);
            log.info("Person with uuid: {} was added to down queue on floor: {}. " +
                    "Queue length: {}", person.getUuid(), number, peopleDown.size());
            downButton.press(elevatorsManager);
        }
    }

    @SneakyThrows
    public Optional<Person> pollFromUpQueue() {
        Optional<Person> p = Optional.ofNullable(peopleUp.poll());
        p.ifPresent(person -> log.info("Person with uuid: {} was removed " +
                    "from up queue on floor: {}. Queue length: {}",
                    person.getUuid(), number, peopleUp.size()));
        return p;
    }

    @SneakyThrows
    public Optional<Person> pollFromDownQueue() {
        Optional<Person> p = Optional.ofNullable(peopleDown.poll());
        p.ifPresent(person -> log.info("Person with uuid: {} was removed " +
                    "from down queue on floor: {}. Queue length: {}",
                    person.getUuid(), number, peopleDown.size()));
        return p;
    }

    public Optional<Person> peekFromUpQueue() {
        return Optional.ofNullable(peopleUp.peek());
    }

    public Optional<Person> peekFromDownQueue() {
        return Optional.ofNullable(peopleDown.peek());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Floor floor = (Floor) o;
        return number == floor.number;
    }

    @Override
    public int hashCode() {
        return Objects.hash(number);
    }
}
