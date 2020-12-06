package by.bychenok.building.elevator;

import by.bychenok.building.configuration.ElevatorConfig;
import by.bychenok.building.floor.FloorSystem;
import lombok.Getter;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.util.*;
import java.util.concurrent.TimeUnit;

import static by.bychenok.building.elevator.Direction.*;
import static by.bychenok.building.elevator.ElevatorState.*;
import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.collect.Iterables.getFirst;

@Slf4j
public class Elevator implements Runnable {

    @Getter
    private final int id;

    private final int doorOpenCloseTimeSeconds;
    private final int floorPassTimeSeconds;
    private ElevatorState state;

    private final ElevatorPeopleSystem peopleSystem;
    private final ElevatorsManager elevatorsManager;
    private final Set<Integer> stopFloors;

    @Getter
    private int currentFloor;

    public Elevator(int id,
                    ElevatorConfig elevatorConfig,
                    FloorSystem floorSystem,
                    ElevatorsManager elevatorsManager) {
        this.id = id;
        this.doorOpenCloseTimeSeconds = elevatorConfig.getDoorOpenCloseTimeSeconds();
        this.floorPassTimeSeconds = elevatorConfig.getFloorPassTimeSeconds();
        this.currentFloor = elevatorConfig.getStartElevatorFloor();
        this.elevatorsManager = elevatorsManager;
        peopleSystem = new ElevatorPeopleSystem(
                floorSystem, id, elevatorConfig.getLiftingCapacity());
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

    private boolean checkDirection(ElevatorRequest request) {
        boolean result = false;
        if (request.getDirection() == UP
                && isCarryingUp()) {
            result = true;
        }
        if (request.getDirection() == DOWN
                && isCarryingDown()) {
            result = true;
        }
        return result;
    }

    public void pickUpPassenger(ElevatorRequest request) {
        if (!isAvailable()) {
            checkArgument(checkDirection(request),
                    "Direction of request must be the same as elevator carrying direction");
            stopFloors.add(request.getFloor());
            log.info("Elevator: {} get additional request: {}", id, request.getId());
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

    @SneakyThrows
    @Override
    public void run() {
        log.info("Elevator: {} start working", id);
        while (!Thread.interrupted()) {
            while (!stopFloors.isEmpty()) {
                while (!stopFloors.contains(currentFloor)) {
                    // DANGER
                    updateFloor();
                    // DANGER
                    TimeUnit.SECONDS.sleep(floorPassTimeSeconds);
                    log.info("Elevator: {} passing floor: {}, number of passengers: {}",
                            id, currentFloor, peopleSystem.getNumberOfPassengers());
                }

                log.info("Elevator: {} stopped on floor: {}, number of passengers: {}",
                        id, currentFloor, peopleSystem.getNumberOfPassengers());
                TimeUnit.SECONDS.sleep(doorOpenCloseTimeSeconds);

                peopleSystem.unload(currentFloor);

                peopleSystem.load(currentFloor, stopFloors, state);

                peopleSystem.leaveFloor(state, currentFloor, elevatorsManager);
                TimeUnit.SECONDS.sleep(doorOpenCloseTimeSeconds);

                stopFloors.remove(currentFloor);
                log.info("Elevator: {} left floor: {}, number of passengers: {}",
                        id, currentFloor, peopleSystem.getNumberOfPassengers());
            }
            synchronized (this) {
                state = AVAILABLE;
                log.info("Elevator: {} available", id);
                elevatorsManager.manageNewRequest();
                wait();
            }
        }
    }
}
