package by.bychenok.building;

import by.bychenok.building.elevator.Elevator;
import by.bychenok.building.elevator.ElevatorRequest;
import by.bychenok.building.elevator.ElevatorsManager;
import by.bychenok.building.floor.FloorSystem;
import by.bychenok.person.Person;
import by.bychenok.person.PersonGenerator;
import lombok.SneakyThrows;

import java.util.concurrent.*;

import static java.lang.Thread.sleep;

public class Building {
    BuildingConfig config;
    private final FloorSystem floorSystem;
    private final PersonGenerator personGenerator;
    private final Executor generatePeopleExecutor;
    private final ElevatorsManager elevatorsManager;

    private final BlockingQueue<ElevatorRequest> requests;

    public Building(BuildingConfig config) {
        this.config = config;
        requests = new LinkedBlockingQueue<>();
        elevatorsManager = new ElevatorsManager(requests, config.getNumberOfElevators());
        floorSystem = new FloorSystem(config.getNumberOfFloors(), requests, elevatorsManager);
        personGenerator = new PersonGenerator(BuildingConfig.MIN_FLOOR,
                config.getNumberOfFloors(),
                config.getMinPersonWeight(),
                config.getMaxPersonWeight());
        generatePeopleExecutor  = Executors.newSingleThreadExecutor();
    }

    public void start() {
        generatePeopleExecutor.execute(getPeopleGenerator());
    }


    private Runnable getPeopleGenerator() {
        Runnable generate = () -> {
            while (!Thread.interrupted()) {
                Person p = personGenerator.generateRandomPerson();
                floorSystem.addPerson(p);
                int sleepTime = ThreadLocalRandom.current().nextInt(
                        config.getMinSecondsIntervalBetweenPersons(),
                        config.getMaxSecondsIntervalBetweenPersons());
                try {
                    TimeUnit.SECONDS.sleep(sleepTime);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        return generate;
    }

}
