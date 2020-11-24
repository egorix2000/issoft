package by.bychenok.person;

import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

public class PersonGenerator {

    public Person generateRandomPerson() {
        UUID uuid = UUID.randomUUID();
        int weight = ThreadLocalRandom.current().nextInt();
        int currentFloor = ThreadLocalRandom.current().nextInt();
        int destinationFloor = ThreadLocalRandom.current().nextInt();
        return new Person(uuid, weight, currentFloor, destinationFloor);
    }

}
