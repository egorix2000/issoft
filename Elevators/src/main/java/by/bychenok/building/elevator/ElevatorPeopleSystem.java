package by.bychenok.building.elevator;

import by.bychenok.building.floor.Floor;
import by.bychenok.building.floor.FloorSystem;
import by.bychenok.person.Person;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static by.bychenok.building.elevator.ElevatorState.*;

@Slf4j
public class ElevatorPeopleSystem {
    private final FloorSystem floorSystem;
    private final List<Person> people;
    private final int elevatorId;
    private final int liftingCapacity;

    public ElevatorPeopleSystem(FloorSystem floorSystem, int elevatorId, int liftingCapacity) {
        this.floorSystem = floorSystem;
        this.elevatorId = elevatorId;
        this.liftingCapacity = liftingCapacity;
        people = new ArrayList<>();
    }

    private Optional<Person> pollFromQueue(Floor floor, ElevatorState state) {
        if (state == CARRYING_DOWN) {
            return floor.pollFromDownQueue(calculateMaxNewPassengerWeight());
        } else {
            return floor.pollFromUpQueue(calculateMaxNewPassengerWeight());
        }
    }

    private int calculateMaxNewPassengerWeight() {
        int passengersWeight = people
                .stream()
                .mapToInt(Person::getWeight)
                .sum();
        int leftLiftingCapacity = liftingCapacity - passengersWeight;
        int maxWeightExcluded = leftLiftingCapacity + 1;
        return maxWeightExcluded;
    }

    public int getNumberOfPassengers() {
        return people.size();
    }

    public void unload(int currentFloor) {
        log.info("Elevator: {} started unloading passengers. Number of passengers: {}",
                elevatorId, people.size());
        people.removeIf(person -> person.getDestinationFloor() == currentFloor);
        log.info("Elevator: {} ended unloading passengers. Number of passengers: {}",
                elevatorId, people.size());
    }

    public void load(int currentFloor, Set<Integer> stopFloors, ElevatorState state) {
        log.info("Elevator: {} started loading passengers. Number of passengers: {}",
                elevatorId, people.size());
        Floor floor = floorSystem.getFloor(currentFloor);
        Optional<Person> p = pollFromQueue(floor, state);
        while (p.isPresent()) {
            people.add(p.get());
            stopFloors.add(p.get().getDestinationFloor());
            log.info("Person: {} entered elevator: {}", p.get().getUuid(), elevatorId);
            p = pollFromQueue(floor, state);
        }
        log.info("Elevator: {} ended loading passengers. Number of passengers: {}",
                elevatorId, people.size());
    }

    public void leaveFloor(ElevatorState state,
                           int currentFloor,
                           ElevatorsManager elevatorsManager) {
        Floor floor = floorSystem.getFloor(currentFloor);
        if (state == CARRYING_DOWN) {
            floor.handleElevatorLeaveDownEvent(elevatorsManager);
        } else {
            floor.handleElevatorLeaveUpEvent(elevatorsManager);
        }
    }
}
