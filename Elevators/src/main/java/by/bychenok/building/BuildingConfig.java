package by.bychenok.building;

import lombok.Getter;

@Getter
public class BuildingConfig {
    public static final int MIN_FLOOR = 0;
    private final int numberOfFloors;
    private final int numberOfElevators;
    private final int minSecondsIntervalBetweenPersons;
    private final int maxSecondsIntervalBetweenPersons;
    private final int liftingCapacity;
    private final int doorOpenCloseTimeSeconds;
    private final int floorPassTimeSeconds;

    /**
     * Creates new building configuration with
     * min values included and max values excluded
     */
    public BuildingConfig(int numberOfFloors, int numberOfElevators,
                          int minSecondsIntervalBetweenPersons, int maxSecondsIntervalBetweenPersons,
                          int liftingCapacity, int doorOpenCloseTimeSeconds, int floorPassTimeSeconds) {
        this.numberOfFloors = numberOfFloors;
        this.numberOfElevators = numberOfElevators;
        this.minSecondsIntervalBetweenPersons = minSecondsIntervalBetweenPersons;
        this.maxSecondsIntervalBetweenPersons = maxSecondsIntervalBetweenPersons;
        this.liftingCapacity = liftingCapacity;
        this.doorOpenCloseTimeSeconds = doorOpenCloseTimeSeconds;
        this.floorPassTimeSeconds = floorPassTimeSeconds;
    }
}
