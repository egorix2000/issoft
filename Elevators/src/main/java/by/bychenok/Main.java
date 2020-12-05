package by.bychenok;

import by.bychenok.building.Building;
import by.bychenok.building.BuildingConfig;

public class Main {

    public static void main(String[] args) {
        BuildingConfig config = new BuildingConfig(
                10,
                5,
                0,
                8,
                100,
                1,
                3,
                0,
                40,
                80);
        Building building = new Building(config);
        building.start();
    }
}
