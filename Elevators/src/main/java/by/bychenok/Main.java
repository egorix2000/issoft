package by.bychenok;

import by.bychenok.building.Building;
import by.bychenok.building.BuildingConfig;

public class Main {

    public static void main(String[] args) {
        BuildingConfig config = new BuildingConfig(
                20,
                5,
                2,
                10,
                100,
                1,
                2,
                0,
                20,
                80);
        Building building = new Building(config);
        building.start();
    }
}
