package by.bychenok.building.floor;

import by.bychenok.building.elevator.ElevatorRequest;
import by.bychenok.building.statistics.StatisticsCollector;
import com.google.common.collect.ImmutableList;

import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class FloorSystem {
    private final List<Floor> floors;

    public FloorSystem(int numberOfFloors,
                       BlockingQueue<ElevatorRequest> requests,
                       StatisticsCollector statisticsCollector) {
        floors = ImmutableList.copyOf(
                IntStream.range(0, numberOfFloors)
                        .mapToObj(i -> new Floor(i, requests, statisticsCollector))
                        .collect(Collectors.toList()));
    }

    public synchronized Floor getFloor(int number) {
        return floors.get(number);
    }
}
