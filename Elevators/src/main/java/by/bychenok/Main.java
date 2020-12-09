package by.bychenok;

import by.bychenok.building.Building;
import by.bychenok.building.configuration.BuildingConfig;
import by.bychenok.building.configuration.ElevatorConfig;

public class Main {

    public static void main(String[] args) {
        ElevatorConfig elevatorConfig = new ElevatorConfig(
                150,
                1,
                1,
                0);

        BuildingConfig config = new BuildingConfig(
                8,
                4,
                1,
                3,
                30,
                80,
                elevatorConfig);

        Building building = new Building(config);
        building.start();
    }
}
