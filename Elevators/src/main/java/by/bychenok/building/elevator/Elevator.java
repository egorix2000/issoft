package by.bychenok.building.elevator;

import by.bychenok.building.configuration.ElevatorConfig;
import by.bychenok.building.floor.FloorSystem;
import lombok.Getter;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

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
    private int currentFloor;

    private final FloorSystem floorSystem;
    private final ElevatorPeopleSystem peopleSystem;
    private final ElevatorsManager elevatorsManager;
    private final Set<Integer> stopFloors;

    private final ReentrantLock elevatorLock;

    public Elevator(int id,
                    ElevatorConfig elevatorConfig,
                    FloorSystem floorSystem,
                    ElevatorsManager elevatorsManager) {
        this.id = id;
        this.doorOpenCloseTimeSeconds = elevatorConfig.getDoorOpenCloseTimeSeconds();
        this.floorPassTimeSeconds = elevatorConfig.getFloorPassTimeSeconds();
        this.currentFloor = elevatorConfig.getStartElevatorFloor();
        this.floorSystem = floorSystem;
        this.elevatorsManager = elevatorsManager;
        peopleSystem = new ElevatorPeopleSystem(id,
                elevatorConfig.getLiftingCapacity());
        stopFloors = new HashSet<>();
        elevatorLock = new ReentrantLock();
        state = AVAILABLE;
    }

    public void lock() {
        elevatorLock.lock();
    }

    public void unlock() {
        elevatorLock.unlock();
    }

    /**
     * You must lock elevator before calling this method
     * and unlock it after result of this method is no longer needed
     */
    public int getCurrentFloor() {
        return currentFloor;
    }

    /**
     * You must lock elevator before calling this method
     * and unlock it after result of this method is no longer needed
     */
    public boolean isAvailable() {
        return state == AVAILABLE;
    }

    /**
     * You must lock elevator before calling this method
     * and unlock it after result of this method is no longer needed
     */
    public boolean isCarryingUp() {
        return state == CARRYING_UP;
    }

    /**
     * You must lock elevator before calling this method
     * and unlock it after result of this method is no longer needed
     */
    public boolean isCarryingDown() {
        return state == CARRYING_DOWN;
    }

    /**
     * You must lock elevator before calling this method
     * and unlock it after result of this method is no longer needed
     */
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
        lock();
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
        unlock();
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
        log.info("Elevator: {} started working", id);
        while (!Thread.interrupted()) {
            lock();
            while (!stopFloors.isEmpty()) {
                unlock();
                lock();
                while (!stopFloors.contains(currentFloor)) {
                    updateFloor();
                    unlock();
                    TimeUnit.SECONDS.sleep(floorPassTimeSeconds);
                    log.info("Elevator: {} passing floor: {}, number of passengers: {}",
                            id, currentFloor, peopleSystem.getNumberOfPassengers());
                    lock();
                }
                unlock();
                log.info("Elevator: {} stopped on floor: {}, number of passengers: {}",
                        id, currentFloor, peopleSystem.getNumberOfPassengers());
                TimeUnit.SECONDS.sleep(doorOpenCloseTimeSeconds);

                lock();
                    peopleSystem.unload(currentFloor);
                    peopleSystem.load(floorSystem.getFloor(currentFloor), stopFloors, state);
                    peopleSystem.leaveFloor(state, floorSystem.getFloor(currentFloor), elevatorsManager);
                unlock();

                TimeUnit.SECONDS.sleep(doorOpenCloseTimeSeconds);

                lock();
                    stopFloors.remove(currentFloor);
                    log.info("Elevator: {} left floor: {}, number of passengers: {}",
                            id, currentFloor, peopleSystem.getNumberOfPassengers());
            }
            synchronized (this) {
                state = AVAILABLE;
                log.info("Elevator: {} is available", id);
                elevatorsManager.manageNewRequest();
                unlock();
                wait();
            }
        }
        log.info("Elevator: {} ended working", id);
    }
}
