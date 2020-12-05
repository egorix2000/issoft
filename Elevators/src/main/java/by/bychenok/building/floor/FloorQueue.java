package by.bychenok.building.floor;

import by.bychenok.building.elevator.Direction;
import by.bychenok.building.elevator.ElevatorRequest;
import by.bychenok.building.elevator.ElevatorsManager;
import by.bychenok.person.Person;
import lombok.Getter;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.util.LinkedList;
import java.util.Optional;
import java.util.Queue;
import java.util.concurrent.BlockingQueue;

@Slf4j
public class FloorQueue {
    @Getter
    private final int floorNumber;
    @Getter
    private final Direction direction;
    private final Queue<Person> people;
    private final FloorButton button;

    public FloorQueue(int floorNumber, Direction direction, BlockingQueue<ElevatorRequest> requests) {
        this.floorNumber = floorNumber;
        this.direction = direction;
        this.button = new FloorButton(floorNumber, direction, requests);
        people = new LinkedList<>();
    }

    public void handleElevatorLeaveEvent(ElevatorsManager elevatorsManager) {
        synchronized (people) {
            button.reset();
            log.info("Button {} was reset on floor: {}", direction.name(), floorNumber);
            if (!people.isEmpty()) {
                button.press(elevatorsManager);
            }
        }
    }

    @SneakyThrows
    public void add(Person person, ElevatorsManager elevatorsManager) {
        synchronized (people) {
            people.add(person);
            log.info("Person with uuid: {} was added to {} queue " +
                    "on floor: {}. Queue length: {}",
                    person.getUuid(), direction.name(), floorNumber, people.size());
            button.press(elevatorsManager);
        }
    }

    @SneakyThrows
    public Optional<Person> poll() {
        synchronized (people) {
            Optional<Person> p = Optional.ofNullable(people.poll());
            p.ifPresent(person -> log.info("Person with uuid: {} was removed " +
                            "from {} queue on floor: {}. Queue length: {}",
                    person.getUuid(), direction.name(), floorNumber, people.size()));
            return p;
        }
    }

    public Optional<Person> peek() {
        synchronized (people) {
            return Optional.ofNullable(people.peek());
        }
    }
}
