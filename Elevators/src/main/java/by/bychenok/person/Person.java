package by.bychenok.person;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.Objects;
import java.util.UUID;

@Slf4j
@Getter
public class Person {

    private final UUID uuid;
    private final int currentFloor;
    private final int destinationFloor;

    public Person(UUID uuid, int floor, int destinationFloor) {
        this.uuid = uuid;
        this.currentFloor = floor;
        this.destinationFloor = destinationFloor;
        log.info("User with uuid: {}, floor: {}, destination floor: {}  was successfully created",
                uuid.toString(), floor, destinationFloor);
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