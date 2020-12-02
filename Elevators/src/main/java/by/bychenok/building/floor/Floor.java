package by.bychenok.building.floor;

import by.bychenok.person.Person;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.LinkedList;
import java.util.Queue;

import static com.google.common.base.Preconditions.*;

@Slf4j
public class Floor {

    @Getter private boolean isUpPressed;
    @Getter private boolean isDownPressed;
    private final Queue<Person> peopleUp;
    private final Queue<Person> peopleDown;
    private final int number;

    public Floor(int number) {
        this.isUpPressed = false;
        this.isDownPressed = false;
        peopleUp = new LinkedList<>();
        peopleDown = new LinkedList<>();
        this.number = number;
    }

    public void pressUp() {
        isUpPressed = true;
        log.info("Button UP was pressed on {} floor", number);
    }

    public void pressDown() {
        isDownPressed = true;
        log.info("Button DOWN was pressed on {} floor", number);
    }

    public synchronized void addToUpQueue(Person person) {
        peopleUp.add(person);
        log.info("Person with uuid {} was added to up queue on {} floor", person.getUuid(), number);
    }

    public synchronized void addToDownQueue(Person person) {
        peopleDown.add(person);
        log.info("Person with uuid {} was added to down queue on {} floor", person.getUuid(), number);
    }

    public synchronized Person peekFromUpQueue() {
        return peopleUp.peek();
    }

    public synchronized Person peekFromDownQueue() {
        return peopleDown.peek();
    }

    public synchronized Person pollFromUpQueue() {
        checkState(!peopleUp.isEmpty(), "Up queue is empty");
        Person p = peopleUp.poll();
        log.info("Person with uuid {} was removed from up queue on {} floor", p.getUuid(), number);
        return p;
    }

    public synchronized Person pollFromDownQueue() {
        checkState(!peopleDown.isEmpty(), "Down queue is empty");
        Person p = peopleDown.poll();
        log.info("Person with uuid {} was removed from down queue on {} floor", p.getUuid(), number);
        return p;
    }

    public boolean isUpEmpty() {
        return peopleUp.isEmpty();
    }

    public boolean isDownEmpty() {
        return peopleDown.isEmpty();
    }
}
