package by.bychenok.building.elevator;

import by.bychenok.building.floor.Floor;
import by.bychenok.building.floor.FloorSystem;
import by.bychenok.person.Person;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import static by.bychenok.building.elevator.Direction.*;
import static by.bychenok.building.elevator.ElevatorState.*;
import static com.google.common.collect.Iterables.getFirst;

@Slf4j
@Getter
public class Elevator implements Runnable {

    private final int doorOpenCloseTimeSeconds;
    private final int floorPassTimeSeconds;
    private ElevatorState state;
    private int currentFloor;
    private final FloorSystem floorSystem;
    private final int id;
    private final ElevatorsManager elevatorsManager;

    @Getter(AccessLevel.NONE)
    private final List<Person> people;
    @Getter(AccessLevel.NONE)
    private final Set<Integer> stopFloors;


    public Elevator(int doorOpenCloseTimeSeconds,
                    int floorPassTimeSeconds,
                    int currentFloor,
                    FloorSystem floorSystem,
                    int id,
                    ElevatorsManager elevatorsManager) {
        this.doorOpenCloseTimeSeconds = doorOpenCloseTimeSeconds;
        this.floorPassTimeSeconds = floorPassTimeSeconds;
        this.currentFloor = currentFloor;
        this.floorSystem = floorSystem;
        this.id = id;
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

    @SneakyThrows
    @Override
    public void run() {
        log.info("Elevator: {} start working", id);
        while (!Thread.interrupted()) {
            while (!stopFloors.isEmpty()) {
                while (!stopFloors.contains(currentFloor)) {
                    if (currentFloor > getFirst(stopFloors, 0)) {
                        currentFloor--;
                    } else {
                        currentFloor++;
                    }
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
                if (isCarryingDown()) {
                    Person p = floor.pollFromDownQueue();
                    while (p != null) {
                        people.add(p);
                        stopFloors.add(p.getDestinationFloor());
                        log.info("Person: {} entered elevator: {}", p.getUuid(), id);
                        p = floor.pollFromDownQueue();
                    }
                } else {
                    Person p = floor.pollFromUpQueue();
                    while (p != null) {
                        people.add(p);
                        stopFloors.add(p.getDestinationFloor());
                        log.info("Person: {} entered elevator: {}", p.getUuid(), id);
                        p = floor.pollFromUpQueue();
                    }
                }
                log.info("Elevator: {} ended loading passengers. Number of passengers: {}",
                        id, people.size());
                TimeUnit.SECONDS.sleep(doorOpenCloseTimeSeconds);
                stopFloors.remove(currentFloor);
                log.info("Elevator: {} left floor: {}, number of passengers: {}",
                        id, currentFloor, people.size());
            }
            synchronized (this) {
                state = AVAILABLE;
                log.info("Elevator: {} available", id);
                elevatorsManager.manageNewTask();
                wait();
            }
        }
    }
}
