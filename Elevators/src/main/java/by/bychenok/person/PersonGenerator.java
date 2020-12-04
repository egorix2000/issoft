package by.bychenok.person;

import by.bychenok.random.RandomNumberGenerator;

import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;


public class PersonGenerator {

    private final int minFloor;
    private final int maxFloor;

    public PersonGenerator(int minFloor, int maxFloor) {
        this.minFloor = minFloor;
        this.maxFloor = maxFloor;
    }

    public Person generateRandomPerson() {
        UUID uuid = UUID.randomUUID();
        int currentFloor = ThreadLocalRandom.current().nextInt(minFloor, maxFloor);
        int destinationFloor = RandomNumberGenerator
                .generateNumberInRangeWithoutValue(minFloor, maxFloor, currentFloor);
        return new Person(uuid, currentFloor, destinationFloor);
    }

}
