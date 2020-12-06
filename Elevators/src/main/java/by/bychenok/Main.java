package by.bychenok;

import by.bychenok.building.Building;
import by.bychenok.building.BuildingConfig;
import com.google.common.collect.ImmutableList;

public class Main {

    public static void main(String[] args) {
        BuildingConfig config = new BuildingConfig(
                10,
                4,
                0,
                4,
                100,
                1,
                3,
                0,
                30,
                80);
        Building building = new Building(config);
        building.start();
    }
}
