package by.bychenok.building;

import by.bychenok.building.floor.FloorSystem;
import by.bychenok.person.PersonGenerator;

public class Building {
    private final int minFloor = 0;
    private final FloorSystem floorSystem;
    private final PersonGenerator personGenerator;

    public Building(BuildingConfig config) {
        floorSystem = new FloorSystem(config.getNumberOfFloors());
        personGenerator = new PersonGenerator(minFloor,
                config.getNumberOfFloors()-1,
                config.getMinPersonWeight(),
                config.getMinPersonWeight());
    }

}
