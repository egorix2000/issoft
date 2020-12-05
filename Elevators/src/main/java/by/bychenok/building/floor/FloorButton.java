package by.bychenok.building.floor;

import by.bychenok.building.elevator.Direction;
import by.bychenok.building.elevator.ElevatorRequest;
import by.bychenok.building.elevator.ElevatorsManager;
import lombok.Getter;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.util.UUID;
import java.util.concurrent.BlockingQueue;

import static by.bychenok.building.elevator.Direction.UP;

@Slf4j
@Getter
public class FloorButton {
    private boolean isPressed;
    private final Direction direction;
    private final int floorNumber;
    private final BlockingQueue<ElevatorRequest> requests;

    public FloorButton(Direction direction,
                       int floorNumber,
                       BlockingQueue<ElevatorRequest> requests) {
        this.direction = direction;
        this.floorNumber = floorNumber;
        this.requests = requests;
        isPressed = false;
    }

    public synchronized void press(ElevatorsManager elevatorsManager) {
        if (!isPressed) {
            isPressed = true;
            addRequestAndNotifyManger(
                    new ElevatorRequest(UUID.randomUUID(), floorNumber, direction),
                    elevatorsManager
            );
            log.info("Button {} was pressed on floor: {}", direction.name(), floorNumber);
        }
    }

    @SneakyThrows
    private void addRequestAndNotifyManger(ElevatorRequest request, ElevatorsManager elevatorsManager) {
        synchronized (requests) {
            requests.put(request);
            log.info("Request: {} was added. Requests left: {}",
                    request.getId(), requests.size());
            if (requests.size() == 1) {
                elevatorsManager.manageNewRequest();
            }
        }
    }

    public synchronized void reset() {
        isPressed = false;
    }


}
