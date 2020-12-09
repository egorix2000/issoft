package by.bychenok.building.statistics;

import by.bychenok.building.elevator.Direction;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static by.bychenok.building.elevator.Direction.*;

public final class StatisticsCollector {
    private static volatile StatisticsCollector instance;

    private final List<Integer> pressUpCountPerFloor;
    private final List<Integer> pressDownCountPerFloor;
    private final List<Integer> maxUpQueueLengthPerFloor;
    private final List<Integer> maxDownQueueLengthPerFloor;
    private int totalOverloadCount;


    private StatisticsCollector(int numberOfFloors) {
        List<Integer> initList = IntStream
                .range(0, numberOfFloors)
                .mapToObj(i -> 0)
                .collect(Collectors.toList());
        pressUpCountPerFloor = new ArrayList<>(initList);
        pressDownCountPerFloor = new ArrayList<>(initList);
        maxUpQueueLengthPerFloor = new ArrayList<>(initList);
        maxDownQueueLengthPerFloor = new ArrayList<>(initList);
        totalOverloadCount = 0;
    }

    public static StatisticsCollector getStatisticsCollectorInstance(int numberOfFloors) {
        StatisticsCollector result = instance;
        if (result != null) {
            return result;
        }
        synchronized(StatisticsCollector.class) {
            if (instance == null) {
                instance = new StatisticsCollector(numberOfFloors);
            }
            return instance;
        }
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

    public synchronized void addOverload() {
        totalOverloadCount++;
    }
}
