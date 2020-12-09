package by.bychenok.building.floor;

import by.bychenok.building.elevator.Direction;
import by.bychenok.building.elevator.ElevatorRequest;
import by.bychenok.building.elevator.ElevatorsManager;
import by.bychenok.building.statistics.StatisticsCollector;
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
    private final StatisticsCollector statisticsCollector;
    @Getter
    private final int floorNumber;
    @Getter
    private final Direction direction;
    private final Queue<Person> people;
    private final FloorButton button;

    public FloorQueue(int floorNumber,
                      Direction direction,
                      BlockingQueue<ElevatorRequest> requests,
                      StatisticsCollector statisticsCollector) {
        this.floorNumber = floorNumber;
        this.direction = direction;
        this.statisticsCollector = statisticsCollector;
        this.button = new FloorButton(floorNumber, direction, requests, statisticsCollector);
        people = new LinkedList<>();
    }

    public synchronized void handleElevatorLeaveEvent(ElevatorsManager elevatorsManager) {
        button.reset();
        if (!people.isEmpty()) {
            button.press(elevatorsManager);
        }
    }

    @SneakyThrows
    public synchronized void add(Person person, ElevatorsManager elevatorsManager) {
        people.add(person);
        log.info("Person with uuid: {} was added to {} queue " +
                        "on floor: {}. Queue length: {}",
                person.getUuid(), direction.name(), floorNumber, people.size());
        statisticsCollector.updateMaxQueue(floorNumber, people.size(), direction);
        button.press(elevatorsManager);
    }

    @SneakyThrows
    public synchronized Optional<Person> poll(int maxWeight) {
        Optional<Person> p = Optional.ofNullable(people.peek());
        if (p.isPresent() && p.get().getWeight() < maxWeight) {
            p = Optional.ofNullable(people.poll());
            log.info("Person with uuid: {} was removed " +
                            "from {} queue on floor: {}. Queue length: {}",
                    p.get().getUuid(), direction.name(), floorNumber, people.size());
            return p;
        } else {
            p.ifPresent(person -> statisticsCollector.addOverload());
            return Optional.empty();
        }
    }
}
