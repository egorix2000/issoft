package by.bychenok.building;

import lombok.Getter;

@Getter
public class BuildingConfig {
    private final int numberOfFloors;
    private final int numberOfElevators;
    private final int minPersonWeight;
    private final int maxPersonWeight;

    public BuildingConfig(int numberOfFloors, int numberOfElevators,
                          int minPersonWeight, int maxPersonWeight) {
        this.numberOfFloors = numberOfFloors;
        this.numberOfElevators = numberOfElevators;
        this.minPersonWeight = minPersonWeight;
        this.maxPersonWeight = maxPersonWeight;
    }
}
