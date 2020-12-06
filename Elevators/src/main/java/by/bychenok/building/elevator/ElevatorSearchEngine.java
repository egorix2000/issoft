package by.bychenok.building.elevator;

import java.util.List;
import java.util.Optional;

import static by.bychenok.building.elevator.Direction.UP;

public class ElevatorSearchEngine {

    private Optional<Integer> getAvailableElevatorId(List<Elevator> elevators) {
        Optional<Integer> id = Optional.empty();
        for (Elevator elevator : elevators) {
            elevator.lock();
            if (elevator.isAvailable()) {
                id = Optional.of(elevator.getId());
                break;
            } else {
                elevator.unlock();
            }
        }
        return id;
    }

    private Optional<Integer> getCarryingElevatorToHandleId(List<Elevator> elevators,
                                                           ElevatorRequest request) {
        Optional<Integer> id = Optional.empty();
        if (request.getDirection() == UP) {
            for (Elevator elevator : elevators) {
                elevator.lock();
                    if (elevator.isCarryingUp()
                            && elevator.getCurrentFloor() < request.getFloor()) {
                        id = Optional.of(elevator.getId());
                        break;
                } else {
                    elevator.unlock();
                }
            }
        } else {
            for (Elevator elevator : elevators) {
                elevator.lock();
                    if (elevator.isCarryingDown()
                            && elevator.getCurrentFloor() > request.getFloor()) {
                        id = Optional.of(elevator.getId());
                        break;
                } else {
                    elevator.unlock();
                }
            }
        }
        return id;
    }

    public Optional<Integer> findElevatorToHandleRequest(List<Elevator> elevators,
                                           ElevatorRequest request) {
        Optional<Integer> suitableElevatorId = getAvailableElevatorId(elevators);

        if (!suitableElevatorId.isPresent()) {
            suitableElevatorId = getCarryingElevatorToHandleId(elevators, request);
        }

        return suitableElevatorId;
    }
}
