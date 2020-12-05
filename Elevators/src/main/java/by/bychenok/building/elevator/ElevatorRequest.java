package by.bychenok.building.elevator;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.UUID;

import static by.bychenok.building.elevator.Direction.*;

@Slf4j
@Getter
public class ElevatorRequest {
    private final int floor;
    private final Direction direction;
    private final UUID id;

    public ElevatorRequest(int floor, Direction direction, UUID id) {
        this.floor = floor;
        this.direction = direction;
        this.id = id;
        log.info("Request: {} with floor: {}, direction: {} created",
                id, floor, direction == UP ? "UP" : "DOWN");
    }
}
