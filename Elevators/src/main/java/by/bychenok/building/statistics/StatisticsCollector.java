package by.bychenok.building.statistics;

import by.bychenok.building.configuration.BuildingConfig;
import by.bychenok.building.elevator.Direction;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static by.bychenok.building.elevator.Direction.*;

public class StatisticsCollector {
    private final List<Integer> pressUpCountPerFloor;
    private final List<Integer> pressDownCountPerFloor;
    private final List<Integer> maxUpQueueLengthPerFloor;
    private final List<Integer> maxDownQueueLengthPerFloor;
    private int totalLoadCount;
    private int totalOverloadCount;

    public StatisticsCollector(BuildingConfig config) {
        List<Integer> initList = IntStream
                .range(0, config.getNumberOfFloors())
                .mapToObj(i -> 0)
                .collect(Collectors.toList());
        pressUpCountPerFloor = new ArrayList<>(initList);
        pressDownCountPerFloor = new ArrayList<>(initList);
        maxUpQueueLengthPerFloor = new ArrayList<>(initList);
        maxDownQueueLengthPerFloor = new ArrayList<>(initList);
        totalLoadCount = 0;
        totalOverloadCount = 0;
    }

    private synchronized void addPressInList(int floorNumber, List<Integer> pressCount) {
        int newValue = pressCount.get(floorNumber) + 1;
        pressCount.set(floorNumber, newValue);
    }

    public synchronized void addPress(int floorNumber, Direction direction) {
        if (direction == UP) {
            addPressInList(floorNumber, pressUpCountPerFloor);
        } else {
            addPressInList(floorNumber, pressDownCountPerFloor);
        }
    }

    private synchronized void updateMaxQueueInList(int floorNumber,
                                             int queueLength,
                                             List<Integer> maxQueue) {
        int currentMax = maxQueue.get(floorNumber);
        if (queueLength > currentMax) {
            maxQueue.set(floorNumber, queueLength);
        }
    }

    public synchronized void updateMaxQueue(int floorNumber,
                                            int queueLength,
                                            Direction direction) {
        if (direction == UP) {
            updateMaxQueueInList(floorNumber, queueLength, maxUpQueueLengthPerFloor);
        } else {
            updateMaxQueueInList(floorNumber, queueLength, maxDownQueueLengthPerFloor);
        }
    }

    public synchronized void addLoad() {
        totalLoadCount++;
    }

    public synchronized void addOverload() {
        totalOverloadCount++;
    }
}
