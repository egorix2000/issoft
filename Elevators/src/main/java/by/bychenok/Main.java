package by.bychenok;

import by.bychenok.building.Building;
import by.bychenok.building.configuration.BuildingConfig;
import by.bychenok.building.configuration.ElevatorConfig;

public class Main {

    public static void main(String[] args) {
        ElevatorConfig elevatorConfig = new ElevatorConfig(
                100,
                1,
                2,
                0);

        BuildingConfig config = new BuildingConfig(
                10,
                4,
                0,
                8,
                30,
                80,
                elevatorConfig);

        Building building = new Building(config);
        building.start();
    }
}
