package by.bychenok.building.elevator;

import by.bychenok.person.Person;
import lombok.AccessLevel;
import lombok.Getter;
import sun.security.x509.AVA;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.BlockingQueue;

import static by.bychenok.building.elevator.Direction.*;
import static by.bychenok.building.elevator.ElevatorState.*;

@Getter
public class Elevator implements Runnable {

    private final int liftingCapacity;
    private final int doorOpenCloseTimeSeconds;
    private final int floorPassTimeSeconds;
    private ElevatorState state;
    private int currentFloor;
    private Direction direction;
    private final BlockingQueue<ElevatorRequest> requests;

    @Getter(AccessLevel.NONE)
    private final List<Person> people;
    @Getter(AccessLevel.NONE)
    private final List<Integer> stopFloors;


    public Elevator(int liftingCapacity, int doorOpenCloseTimeSeconds,
                    int floorPassTimeSeconds, int currentFloor,
                    BlockingQueue<ElevatorRequest> requests) {
        this.liftingCapacity = liftingCapacity;
        this.doorOpenCloseTimeSeconds = doorOpenCloseTimeSeconds;
        this.floorPassTimeSeconds = floorPassTimeSeconds;
        this.currentFloor = currentFloor;
        this.requests = requests;
        people = new ArrayList<>();
        stopFloors = new ArrayList<>();
        state = AVAILABLE;
        direction = STATIC;
    }

    public boolean isAvailable() {
        return state == AVAILABLE;
    }

    public void pickUpPassenger(int floor) {
        if (state == AVAILABLE) {
            state = MOVING_TO_FIRST_PASSENGER;
        }
        stopFloors.add(floor);
    }

    @Override
    public void run() {

    }
}
