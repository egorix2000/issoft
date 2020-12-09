package by.bychenok.building.floor;

import by.bychenok.building.elevator.ElevatorRequest;
import by.bychenok.building.elevator.ElevatorsManager;
import by.bychenok.person.Person;
import lombok.Getter;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.util.*;
import java.util.concurrent.BlockingQueue;

import static by.bychenok.building.elevator.Direction.*;

@Slf4j
public class Floor {

    @Getter
    private final int number;

    private final FloorQueue peopleUp;
    private final FloorQueue peopleDown;

    public Floor(int number, BlockingQueue<ElevatorRequest> requests) {
        peopleUp = new FloorQueue(number, UP, requests);
        peopleDown = new FloorQueue(number, DOWN, requests);
        this.number = number;
    }

    public void handleElevatorLeaveDownEvent(ElevatorsManager elevatorsManager) {
        peopleDown.handleElevatorLeaveEvent(elevatorsManager);
    }

    public void handleElevatorLeaveUpEvent(ElevatorsManager elevatorsManager) {
        peopleUp.handleElevatorLeaveEvent(elevatorsManager);
    }

    public void addPerson(Person person, ElevatorsManager elevatorsManager) {
        int currentFloor = person.getCurrentFloor();
        int destinationFloor = person.getDestinationFloor();
        if (destinationFloor < currentFloor) {
            addToDownQueue(person, elevatorsManager);
        } else {
            addToUpQueue(person, elevatorsManager);
        }
    }

    @SneakyThrows
    private void addToUpQueue(Person person, ElevatorsManager elevatorsManager) {
        peopleUp.add(person, elevatorsManager);
    }

    @SneakyThrows
    private void addToDownQueue(Person person, ElevatorsManager elevatorsManager) {
        peopleDown.add(person, elevatorsManager);
    }

    @SneakyThrows
    public Optional<Person> pollFromUpQueue(int maxWeight) {
        return peopleUp.poll(maxWeight);
    }

    @SneakyThrows
    public Optional<Person> pollFromDownQueue(int maxWeight) {
        return peopleDown.poll(maxWeight);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Floor floor = (Floor) o;
        return number == floor.number;
    }

    @Override
    public int hashCode() {
        return Objects.hash(number);
    }
}
