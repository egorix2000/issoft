package by.bychenok.building.elevator;

import by.bychenok.building.elevator.Direction;
import lombok.Getter;

@Getter
public class ElevatorRequest {
    private final int floor;
    private final Direction direction;

    public ElevatorRequest(int floor, Direction direction) {
        this.floor = floor;
        this.direction = direction;
    }
}
