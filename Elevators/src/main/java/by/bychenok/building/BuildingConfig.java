package by.bychenok.building;

import lombok.Getter;

@Getter
public class BuildingConfig {
    public static final int MIN_FLOOR = 0;
    private final int numberOfFloors;
    private final int numberOfElevators;
    private final int minPersonWeight;
    private final int maxPersonWeight;
    private final int minSecondsIntervalBetweenPersons;
    private final int maxSecondsIntervalBetweenPersons;

    /**
     * Creates new building configuration with
     * min values included and max values excluded
     */
    public BuildingConfig(int numberOfFloors, int numberOfElevators,
                          int minPersonWeight, int maxPersonWeight,
                          int minSecondsIntervalBetweenPersons, int maxSecondsIntervalBetweenPersons) {
        this.numberOfFloors = numberOfFloors;
        this.numberOfElevators = numberOfElevators;
        this.minPersonWeight = minPersonWeight;
        this.maxPersonWeight = maxPersonWeight;
        this.minSecondsIntervalBetweenPersons = minSecondsIntervalBetweenPersons;
        this.maxSecondsIntervalBetweenPersons = maxSecondsIntervalBetweenPersons;
    }
}
