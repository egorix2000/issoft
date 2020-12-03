package by.bychenok.person;

import by.bychenok.random.RandomNumberGenerator;

import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

import static com.google.common.base.Preconditions.*;

public class PersonGenerator {

    private final int minFloor;
    private final int maxFloor;
    private final int minWeight;
    private final int maxWeight;

    public PersonGenerator(int minFloor, int maxFloor, int minWeight, int maxWeight) {
        checkArgument(minFloor < maxFloor,
                "Min floor must be less than max floor");
        checkArgument(minWeight < maxWeight,
                "Min weight must be less than max weight");
        this.minFloor = minFloor;
        this.maxFloor = maxFloor;
        this.minWeight = minWeight;
        this.maxWeight = maxWeight;
    }

    public Person generateRandomPerson() {
        UUID uuid = UUID.randomUUID();
        int weight = ThreadLocalRandom.current().nextInt(minWeight, maxWeight);
        int currentFloor = ThreadLocalRandom.current().nextInt(minFloor, maxFloor);
        int destinationFloor = RandomNumberGenerator.generateNumberInRangeWithoutValue(
                minFloor, maxFloor, currentFloor);
        return new Person(uuid, weight, currentFloor, destinationFloor);
    }

}
