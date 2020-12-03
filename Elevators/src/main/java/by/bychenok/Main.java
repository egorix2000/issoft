package by.bychenok;

import by.bychenok.building.Building;
import by.bychenok.building.BuildingConfig;

public class Main {

    public static void main(String[] args) {
        BuildingConfig config = new BuildingConfig(
                4,
                10,
                20,
                80,
                1,
                4);
        Building building = new Building(config);
        building.start();
    }
}
