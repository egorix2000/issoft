package by.bychenok.building;

import by.bychenok.building.configuration.BuildingConfig;
import by.bychenok.building.elevator.ElevatorRequest;
import by.bychenok.building.elevator.ElevatorsManager;
import by.bychenok.building.floor.FloorSystem;
import by.bychenok.building.statistics.StatisticsCollector;
import by.bychenok.person.Person;
import by.bychenok.person.PersonGenerator;
import lombok.SneakyThrows;

import java.util.concurrent.*;


public class Building {
    private final BuildingConfig config;
    private final FloorSystem floorSystem;
    private final PersonGenerator personGenerator;
    private final Executor generatePeopleExecutor;
    private final Executor manageElevatorsExecutor;
    private final ElevatorsManager elevatorsManager;
    private final StatisticsCollector statisticsCollector;

    public Building(BuildingConfig config) {
        this.config = config;
        statisticsCollector = new StatisticsCollector(config.getNumberOfFloors());
        BlockingQueue<ElevatorRequest> requests = new LinkedBlockingQueue<>();
        floorSystem = new FloorSystem(config.getNumberOfFloors(),
                requests,
                statisticsCollector);
        elevatorsManager = new ElevatorsManager(requests,
                config.getNumberOfElevators(),
                config.getElevatorConfig(),
                floorSystem,
                statisticsCollector);
        personGenerator = new PersonGenerator(BuildingConfig.MIN_FLOOR,
                config.getNumberOfFloors(),
                config.getMinPersonWeight(),
                config.getMaxPersonWeight());
        generatePeopleExecutor  = Executors.newSingleThreadExecutor();
        manageElevatorsExecutor = Executors.newSingleThreadExecutor();
    }

    @SneakyThrows
    public void start() {
        elevatorsManager.startElevators();
        manageElevatorsExecutor.execute(elevatorsManager);
        generatePeopleExecutor.execute(getPeopleGenerator());
    }

    private Runnable getPeopleGenerator() {
        return () -> {
            while (!Thread.interrupted()) {
                Person p = personGenerator.generateRandomPerson();
                floorSystem.getFloor(p.getCurrentFloor()).addPerson(p, elevatorsManager);
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
    }

}
