package by.bychenok.person;

import by.bychenok.random.RandomNumberGenerator;
import com.google.common.base.Preconditions;

import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

import static com.google.common.base.Preconditions.checkArgument;


public class PersonGenerator {

    private final int minFloor;
    private final int maxFloor;
    private final int minWeight;
    private final int maxWeight;

    public PersonGenerator(int minFloor, int maxFloor, int minWeight, int maxWeight) {
        checkArgument(minFloor < maxFloor,
                "Min floor must be less than max floor");
        checkArgument(minWeight < maxWeight,
                "Min weight must be less than max floor");
        this.minFloor = minFloor;
        this.maxFloor = maxFloor;
        this.minWeight = minWeight;
        this.maxWeight = maxWeight;
    }

    public Person generateRandomPerson() {
        UUID uuid = UUID.randomUUID();
        int currentFloor = ThreadLocalRandom.current().nextInt(minFloor, maxFloor);
        int destinationFloor = RandomNumberGenerator
                .generateNumberInRangeWithoutValue(minFloor, maxFloor, currentFloor);
        int weight = ThreadLocalRandom.current().nextInt(minWeight, maxWeight);
        return new Person(uuid, currentFloor, destinationFloor, weight);
    }

}
