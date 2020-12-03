package by.bychenok.building.floor;

import by.bychenok.building.elevator.ElevatorRequest;
import by.bychenok.building.elevator.ElevatorsManager;
import by.bychenok.person.Person;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.BlockingQueue;

import static by.bychenok.building.elevator.Direction.*;

@Slf4j
public class Floor {

    @Getter private boolean isUpPressed;
    @Getter private boolean isDownPressed;
    private final Queue<Person> peopleUp;
    private final Queue<Person> peopleDown;
    private final int number;
    private final BlockingQueue<ElevatorRequest> requests;
    private final ElevatorsManager elevatorsManager;

    public Floor(int number,
                 BlockingQueue<ElevatorRequest> requests,
                 ElevatorsManager elevatorsManager) {
        this.requests = requests;
        this.elevatorsManager = elevatorsManager;
        this.isUpPressed = false;
        this.isDownPressed = false;
        peopleUp = new LinkedList<>();
        peopleDown = new LinkedList<>();
        this.number = number;
    }

    public void pressUp() {
        isUpPressed = true;
        requests.add(new ElevatorRequest(number, UP));
        log.info("Button UP was pressed on {} floor", number);
    }

    public void pressDown() {
        isDownPressed = true;
        addRequestAndNotifyManger(new ElevatorRequest(number, DOWN));
        log.info("Button DOWN was pressed on {} floor", number);
    }

    private void addRequestAndNotifyManger(ElevatorRequest request) {
        requests.add(request);
        if (requests.size() == 1) {
            elevatorsManager.manageNewTask();
        }
    }

    public void addToUpQueue(Person person) {
        peopleUp.add(person);
        if (peopleUp.size() == 1) {
            pressUp();
        }
        log.info("Person with uuid {} was added to up queue on {} floor", person.getUuid(), number);
    }

    public void addToDownQueue(Person person) {
        peopleDown.add(person);
        if (peopleDown.size() == 1) {
            pressDown();
        }
        log.info("Person with uuid {} was added to down queue on {} floor", person.getUuid(), number);
    }

    public Person peekFromUpQueue() {
        return peopleUp.peek();
    }

    public Person peekFromDownQueue() {
        return peopleDown.peek();
    }

    public Person pollFromUpQueue() {
        Person p = peopleUp.poll();
        if (p != null) {
            log.info("Person with uuid {} was removed from up queue on {} floor. ",
                    p.getUuid(),
                    number);
        }
        return p;
    }

    public Person pollFromDownQueue() {
        Person p = peopleDown.poll();
        if (p != null) {
            log.info("Person with uuid {} was removed from down queue on {} floor",
                    p.getUuid(),
                    number);
        }
        return p;
    }

    public boolean isUpEmpty() {
        return peopleUp.isEmpty();
    }

    public boolean isDownEmpty() {
        return peopleDown.isEmpty();
    }
}
