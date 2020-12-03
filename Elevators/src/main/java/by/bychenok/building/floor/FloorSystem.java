package by.bychenok.building.floor;

import by.bychenok.building.elevator.ElevatorRequest;
import by.bychenok.building.elevator.ElevatorsManager;
import by.bychenok.person.Person;
import com.google.common.collect.ImmutableList;

import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class FloorSystem {
    private final List<Floor> floors;

    public FloorSystem(int numberOfFloors,
                       BlockingQueue<ElevatorRequest> requests,
                       ElevatorsManager elevatorsManager) {
        floors = ImmutableList.copyOf(
                IntStream.range(0, numberOfFloors)
                        .mapToObj(i -> new Floor(i, requests, elevatorsManager))
                        .collect(Collectors.toList()));
    }

    public void addPerson(Person p) {
        int currentFloor = p.getCurrentFloor();
        int destinationFloor = p.getDestinationFloor();
        if (destinationFloor < currentFloor) {
            floors.get(currentFloor).addToDownQueue(p);
        } else {
            floors.get(currentFloor).addToUpQueue(p);
        }
    }
}
