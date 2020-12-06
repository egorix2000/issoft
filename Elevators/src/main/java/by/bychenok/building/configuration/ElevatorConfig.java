package by.bychenok.building.configuration;

import lombok.Getter;

@Getter
public class ElevatorConfig {
    private final int liftingCapacity;
    private final int doorOpenCloseTimeSeconds;
    private final int floorPassTimeSeconds;
    private final int startElevatorFloor;

    public ElevatorConfig(int liftingCapacity,
                          int doorOpenCloseTimeSeconds,
                          int floorPassTimeSeconds,
                          int startElevatorFloor) {
        this.liftingCapacity = liftingCapacity;
        this.doorOpenCloseTimeSeconds = doorOpenCloseTimeSeconds;
        this.floorPassTimeSeconds = floorPassTimeSeconds;
        this.startElevatorFloor = startElevatorFloor;
    }
}
