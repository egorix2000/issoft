package by.bychenok.building.elevator;

import by.bychenok.building.floor.Floor;
import by.bychenok.building.floor.FloorSystem;
import by.bychenok.person.Person;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.util.*;
import java.util.concurrent.TimeUnit;

import static by.bychenok.building.elevator.Direction.*;
import static by.bychenok.building.elevator.ElevatorState.*;
import static com.google.common.collect.Iterables.getFirst;

@Slf4j
public class Elevator implements Runnable {

    private final int doorOpenCloseTimeSeconds;
    private final int floorPassTimeSeconds;
    private final int liftingCapacity;
    private ElevatorState state;
    @Getter
    private int currentFloor;
    private final FloorSystem floorSystem;
    private final int id;
    private final ElevatorsManager elevatorsManager;
    private final List<Person> people;
    private final Set<Integer> stopFloors;


    public Elevator(int id,
                    int doorOpenCloseTimeSeconds,
                    int floorPassTimeSeconds,
                    int startFloor,
                    int liftingCapacity,
                    FloorSystem floorSystem,
                    ElevatorsManager elevatorsManager) {
        this.id = id;
        this.doorOpenCloseTimeSeconds = doorOpenCloseTimeSeconds;
        this.floorPassTimeSeconds = floorPassTimeSeconds;
        this.liftingCapacity = liftingCapacity;
        this.currentFloor = startFloor;
        this.floorSystem = floorSystem;
        this.elevatorsManager = elevatorsManager;
        people = new ArrayList<>();
        stopFloors = new HashSet<>();
        state = AVAILABLE;
    }

    public boolean isAvailable() {
        return state == AVAILABLE;
    }

    public boolean isCarryingUp() {
        return state == CARRYING_UP;
    }

    public boolean isCarryingDown() {
        return state == CARRYING_DOWN;
    }

    public void pickUpPassenger(ElevatorRequest request) {
        if (!isAvailable()) {
            stopFloors.add(request.getFloor());
            log.info("Elevator: {} get request: {}", id, request.getId());
        } else {
            synchronized (this) {
                stopFloors.add(request.getFloor());
                if (request.getDirection() == UP) {
                    state = CARRYING_UP;
                } else {
                    state = CARRYING_DOWN;
                }
                log.info("Elevator: {} get first request: {}", id, request.getId());
                notifyAll();
            }
        }
    }

    private void updateFloor() {
        if (currentFloor > getFirst(stopFloors, 0)) {
            currentFloor--;
        } else {
            currentFloor++;
        }
    }

    private Optional<Person> pollFromQueue(Floor floor) {
        if (isCarryingDown()) {
            return floor.pollFromDownQueue(calculateMaxNewPassengerWeight());
        } else {
            return floor.pollFromUpQueue(calculateMaxNewPassengerWeight());
        }
    }

    @SneakyThrows
    @Override
    public void run() {
        log.info("Elevator: {} start working", id);
        while (!Thread.interrupted()) {
            while (!stopFloors.isEmpty()) {
                while (!stopFloors.contains(currentFloor)) {
                    updateFloor();
                    TimeUnit.SECONDS.sleep(floorPassTimeSeconds);
                    log.info("Elevator: {} passing floor: {}, number of passengers: {}",
                            id, currentFloor, people.size());
                }

                log.info("Elevator: {} stopped on floor: {}, number of passengers: {}",
                        id, currentFloor, people.size());
                TimeUnit.SECONDS.sleep(doorOpenCloseTimeSeconds);

                log.info("Elevator: {} started unloading passengers. Number of passengers: {}",
                        id, people.size());
                people.removeIf(person -> person.getDestinationFloor() == currentFloor);
                log.info("Elevator: {} ended unloading passengers. Number of passengers: {}",
                        id, people.size());

                log.info("Elevator: {} started loading passengers. Number of passengers: {}",
                        id, people.size());
                Floor floor = floorSystem.getFloor(currentFloor);
                Optional<Person> p = pollFromQueue(floor);
                while (p.isPresent()) {
                    people.add(p.get());
                    stopFloors.add(p.get().getDestinationFloor());
                    log.info("Person: {} entered elevator: {}", p.get().getUuid(), id);
                    p = pollFromQueue(floor);
                }
                log.info("Elevator: {} ended loading passengers. Number of passengers: {}",
                        id, people.size());

                if (isCarryingDown()) {
                    floor.handleElevatorLeaveDownEvent(elevatorsManager);
                } else {
                    floor.handleElevatorLeaveUpEvent(elevatorsManager);
                }
                TimeUnit.SECONDS.sleep(doorOpenCloseTimeSeconds);

                stopFloors.remove(currentFloor);
                log.info("Elevator: {} left floor: {}, number of passengers: {}",
                        id, currentFloor, people.size());
            }
            synchronized (this) {
                state = AVAILABLE;
                log.info("Elevator: {} available", id);
                elevatorsManager.manageNewRequest();
                wait();
            }
        }
    }

    private int calculateMaxNewPassengerWeight() {
        int passengersWeight = people
                .stream()
                .mapToInt(Person::getWeight)
                .sum();
        int leftLiftingCapacity = liftingCapacity - passengersWeight;
        int maxWeightExcluded = leftLiftingCapacity + 1;
        return maxWeightExcluded;
    }
}
