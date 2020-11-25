package by.bychenok.building;

import by.bychenok.person.Person;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

public class Elevator {
    @Getter
    private final int liftingCapacity;
    @Getter
    private final int doorOpenCloseTime;
    @Getter
    private final int floorPassTime;
    private List<Person> people;

    public Elevator(int liftingCapacity, int doorOpenCloseTime, int floorPassTime) {
        this.liftingCapacity = liftingCapacity;
        this.doorOpenCloseTime = doorOpenCloseTime;
        this.floorPassTime = floorPassTime;
        this.people = new ArrayList<>();
    }
}
