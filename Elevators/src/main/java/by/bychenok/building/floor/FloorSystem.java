package by.bychenok.building.floor;

import by.bychenok.building.elevator.ElevatorRequest;
import com.google.common.collect.ImmutableList;

import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class FloorSystem {
    private final List<Floor> floors;

    public FloorSystem(int numberOfFloors,
                       BlockingQueue<ElevatorRequest> requests) {
        floors = ImmutableList.copyOf(
                IntStream.range(0, numberOfFloors)
                        .mapToObj(i -> new Floor(i, requests))
                        .collect(Collectors.toList()));
    }

    public Floor getFloor(int number) {
        return floors.get(number);
    }
}
