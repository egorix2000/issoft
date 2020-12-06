package by.bychenok.building.configuration;

import lombok.Getter;

@Getter
public class BuildingConfig {
    public static final int MIN_FLOOR = 0;
    private final int numberOfFloors;
    private final int numberOfElevators;
    private final int minSecondsIntervalBetweenPersons;
    private final int maxSecondsIntervalBetweenPersons;
    private final int minPersonWeight;
    private final int maxPersonWeight;
    private final ElevatorConfig elevatorConfig;

    public BuildingConfig(int numberOfFloors,
                          int numberOfElevators,
                          int minSecondsIntervalBetweenPersons,
                          int maxSecondsIntervalBetweenPersons,
                          int minPersonWeight,
                          int maxPersonWeight,
                          ElevatorConfig elevatorConfig) {
        this.numberOfFloors = numberOfFloors;
        this.numberOfElevators = numberOfElevators;
        this.elevatorConfig = elevatorConfig;
        this.minSecondsIntervalBetweenPersons = minSecondsIntervalBetweenPersons;
        this.maxSecondsIntervalBetweenPersons = maxSecondsIntervalBetweenPersons;
        this.minPersonWeight = minPersonWeight;
        this.maxPersonWeight = maxPersonWeight;
    }
}
