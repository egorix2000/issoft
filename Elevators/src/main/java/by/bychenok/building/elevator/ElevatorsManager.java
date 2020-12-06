package by.bychenok.building.elevator;

import by.bychenok.building.configuration.ElevatorConfig;
import by.bychenok.building.floor.FloorSystem;
import com.google.common.collect.ImmutableList;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;
import java.util.stream.IntStream;


@Slf4j
public class ElevatorsManager implements Runnable {
    private final BlockingQueue<ElevatorRequest> requests;
    private final List<Elevator> elevators;
    private final ElevatorSearchEngine elevatorSearchEngine;
    private final Executor notifyExecutor;

    public ElevatorsManager(BlockingQueue<ElevatorRequest> requests,
                            int numberOfElevators,
                            ElevatorConfig elevatorConfig,
                            FloorSystem floorSystem) {
        this.requests = requests;
        elevators = ImmutableList.copyOf(IntStream
                .range(0, numberOfElevators)
                .mapToObj(i -> new Elevator(i, elevatorConfig,
                        floorSystem, this))
                .collect(Collectors.toList()));
        notifyExecutor = Executors.newSingleThreadExecutor();
        elevatorSearchEngine = new ElevatorSearchEngine();
    }

    public void manageNewRequest() {
        notifyExecutor.execute(
                () -> {
                    synchronized (this) {
                        notifyAll();
                    }
                }
        );
    }

    public void startElevators() {
        elevators.stream()
                .map(Thread::new)
                .forEach(Thread::start);
    }

    @SneakyThrows
    @Override
    public synchronized void run() {
        while (!Thread.interrupted()) {
            while (!requests.isEmpty()) {
                ElevatorRequest request = requests.take();
                log.info("New request: {} taken by manager. Requests left: {}",
                        request.getId(), requests.size());

                AtomicReference<Boolean> isManaged = new AtomicReference<>(false);

                while (!isManaged.get()) {

                    // DANGER
                    Optional<Integer> elevatorId =
                            elevatorSearchEngine.findElevatorToHandleRequest(
                                    elevators, request);

                    elevatorId.ifPresent(id -> {
                        elevators.get(id).pickUpPassenger(request);
                        isManaged.set(true);
                    });
                    // END DANGER

                    if (!isManaged.get()) {
                        log.info("No elevators to handle request: {}, " +
                                    "waiting for available elevators ...", request.getId());
                        wait();
                    }
                }
            }
            log.info("No requests to handle. Waiting for new request ...");
            wait();
        }
    }
}
