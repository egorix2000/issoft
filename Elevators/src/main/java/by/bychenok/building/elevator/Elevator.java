package by.bychenok.building.elevator;

import by.bychenok.person.Person;
import lombok.AccessLevel;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

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

    @Getter(AccessLevel.NONE)
    private final List<Person> people;
    @Getter(AccessLevel.NONE)
    private final List<Integer> stopFloors;


    public Elevator(int liftingCapacity, int doorOpenCloseTimeSeconds,
                    int floorPassTimeSeconds, int currentFloor) {
        this.liftingCapacity = liftingCapacity;
        this.doorOpenCloseTimeSeconds = doorOpenCloseTimeSeconds;
        this.floorPassTimeSeconds = floorPassTimeSeconds;
        this.currentFloor = currentFloor;
        people = new ArrayList<>();
        stopFloors = new ArrayList<>();
        state = AVAILABLE;
        direction = STATIC;
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
