package by.bychenok.building.elevator;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static by.bychenok.building.elevator.Direction.UP;

public class ElevatorSearchEngine {

    private Optional<Integer> getAvailableElevatorId(List<Elevator> elevators) {
        return elevators.stream()
                .filter(Elevator::isAvailable)
                .map(Elevator::getId)
                .findAny();
    }

    private Optional<Integer> getCarryingElevatorToHandleId(List<Elevator> elevators,
                                                           ElevatorRequest request) {
        Stream<Elevator> suitableElevators;
        if (request.getDirection() == UP) {
            suitableElevators = elevators.stream()
                    .filter(Elevator::isCarryingUp)
                    .filter(elevator -> elevator.getCurrentFloor() < request.getFloor());
        } else {
            suitableElevators = elevators.stream()
                    .filter(Elevator::isCarryingDown)
                    .filter(elevator -> elevator.getCurrentFloor() > request.getFloor());
        }
        return suitableElevators
                .map(Elevator::getId)
                .findAny();
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
