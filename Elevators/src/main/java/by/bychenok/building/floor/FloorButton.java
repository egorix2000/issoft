package by.bychenok.building.floor;

import by.bychenok.building.elevator.Direction;
import by.bychenok.building.elevator.ElevatorRequest;
import by.bychenok.building.elevator.ElevatorsManager;
import by.bychenok.building.statistics.StatisticsCollector;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.util.UUID;
import java.util.concurrent.BlockingQueue;


@Slf4j
public class FloorButton {
    private final StatisticsCollector statisticsCollector;
    private boolean isPressed;
    private final Direction direction;
    private final int floorNumber;
    private final BlockingQueue<ElevatorRequest> requests;

    public FloorButton(int floorNumber,
                       Direction direction,
                       BlockingQueue<ElevatorRequest> requests,
                       StatisticsCollector statisticsCollector) {
        this.floorNumber = floorNumber;
        this.direction = direction;
        this.requests = requests;
        this.statisticsCollector = statisticsCollector;
        isPressed = false;
    }

    public synchronized void press(ElevatorsManager elevatorsManager) {
        if (!isPressed) {
            isPressed = true;
            log.info("Button {} was pressed on floor: {}", direction.name(), floorNumber);
            statisticsCollector.addPress(floorNumber, direction);
            addRequestAndNotifyManger(
                    new ElevatorRequest(UUID.randomUUID(), floorNumber, direction),
                    elevatorsManager
            );
        }
    }

    @SneakyThrows
    private void addRequestAndNotifyManger(ElevatorRequest request, ElevatorsManager elevatorsManager) {
        requests.put(request);
        log.info("Request: {} was added. Requests left: {}",
                request.getId(), requests.size());
        elevatorsManager.manageNewRequest();
    }

    public synchronized void reset() {
        isPressed = false;
        log.info("Button {} was reset on floor: {}", direction.name(), floorNumber);
    }


}
