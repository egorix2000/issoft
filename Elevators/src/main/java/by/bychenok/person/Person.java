package by.bychenok.person;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.Objects;
import java.util.UUID;

import static com.google.common.base.Preconditions.checkArgument;

@Slf4j
@Getter
public class Person {

    private final UUID uuid;
    private final int currentFloor;
    private final int destinationFloor;
    private final int weight;

    public Person(UUID uuid, int floor, int destinationFloor, int weight) {
        checkArgument(weight > 0,
                "Weight must be more than 0");
        checkArgument(floor != destinationFloor,
                "Floor and destination floor must be different");

        this.uuid = uuid;
        this.currentFloor = floor;
        this.destinationFloor = destinationFloor;
        this.weight = weight;
        log.info("User with uuid: {}, floor: {}, " +
                        "destination floor: {}, weight: {}  was successfully created",
                uuid.toString(), floor, destinationFloor, weight);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Person person = (Person) o;
        return uuid.equals(person.uuid);
    }

    @Override
    public int hashCode() {
        return Objects.hash(uuid);
    }
}