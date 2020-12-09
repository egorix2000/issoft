package by.bychenok.building.elevator;

import by.bychenok.building.floor.Floor;
import by.bychenok.building.statistics.StatisticsCollector;
import by.bychenok.building.statistics.StatisticsCollectorFactory;
import by.bychenok.person.Person;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static by.bychenok.building.elevator.ElevatorState.*;
import static com.google.common.base.Preconditions.checkArgument;

@Slf4j
public class ElevatorPeopleSystem {
    private final StatisticsCollector collector = StatisticsCollectorFactory.getStatisticsCollector();

    private final List<Person> people;
    private final int elevatorId;
    private final int liftingCapacity;

    public ElevatorPeopleSystem(int elevatorId, int liftingCapacity) {
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

    public void load(Floor currentFloor, Set<Integer> stopFloors, ElevatorState state) {
        checkArgument(state != AVAILABLE,
                "Can not load during available state");
        log.info("Elevator: {} started loading passengers. Number of passengers: {}",
                elevatorId, people.size());
        Optional<Person> p = pollFromQueue(currentFloor, state);
        while (p.isPresent()) {
            people.add(p.get());
            stopFloors.add(p.get().getDestinationFloor());
            log.info("Person: {} entered elevator: {}", p.get().getUuid(), elevatorId);
            p = pollFromQueue(currentFloor, state);
        }
        log.info("Elevator: {} ended loading passengers. Number of passengers: {}",
                elevatorId, people.size());
        collector.addLoad();
    }

    public void leaveFloor(ElevatorState state,
                           Floor currentFloor,
                           ElevatorsManager elevatorsManager) {
        checkArgument(state != AVAILABLE,
                "Can not leave floor during available state");
        if (state == CARRYING_DOWN) {
            currentFloor.handleElevatorLeaveDownEvent(elevatorsManager);
        } else {
            currentFloor.handleElevatorLeaveUpEvent(elevatorsManager);
        }
    }
}
