package by.bychenok.building.floor;

import by.bychenok.building.elevator.ElevatorRequest;
import by.bychenok.building.elevator.ElevatorsManager;
import by.bychenok.person.Person;
import lombok.Getter;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import static by.bychenok.building.elevator.Direction.*;

@Slf4j
public class Floor {

    @Getter private boolean isUpPressed;
    @Getter private boolean isDownPressed;
    @Getter private final int number;
    private final BlockingQueue<Person> peopleUp;
    private final BlockingQueue<Person> peopleDown;
    private final BlockingQueue<ElevatorRequest> requests;

    public Floor(int number,
                 BlockingQueue<ElevatorRequest> requests) {
        this.requests = requests;
        this.isUpPressed = false;
        this.isDownPressed = false;
        peopleUp = new LinkedBlockingQueue<>();
        peopleDown = new LinkedBlockingQueue<>();
        this.number = number;
    }

    private void pressUp(ElevatorsManager elevatorsManager) {
        isUpPressed = true;
        addRequestAndNotifyManger(
                new ElevatorRequest(number, UP, UUID.randomUUID()),
                elevatorsManager
        );
        log.info("Button UP was pressed on {} floor", number);
    }

    private void pressDown(ElevatorsManager elevatorsManager) {
        isDownPressed = true;
        addRequestAndNotifyManger(
                new ElevatorRequest(number, DOWN, UUID.randomUUID()),
                elevatorsManager
        );
        log.info("Button DOWN was pressed on {} floor", number);
    }

    @SneakyThrows
    private void addRequestAndNotifyManger(ElevatorRequest request, ElevatorsManager elevatorsManager) {
        requests.put(request);
        log.info("Request: {} was added. Tasks left: {}", request.getId(), requests.size());
        if (requests.size() == 1) {
            elevatorsManager.manageNewTask();
        }
    }

    @SneakyThrows
    public void addToUpQueue(Person person, ElevatorsManager elevatorsManager) {
        synchronized (peopleUp) {
            peopleUp.put(person);
            log.info("Person with uuid {} was added to up queue on {} floor. " +
                    "Queue length: {}", person.getUuid(), number, peopleUp.size());
            if (peopleUp.size() == 1) {
                pressUp(elevatorsManager);
            }
        }
    }

    @SneakyThrows
    public void addToDownQueue(Person person, ElevatorsManager elevatorsManager) {
        synchronized (peopleDown) {
            peopleDown.put(person);
            log.info("Person with uuid {} was added to down queue on {} floor. " +
                    "Queue length: {}", person.getUuid(), number, peopleDown.size());
            if (peopleDown.size() == 1) {
                pressDown(elevatorsManager);
            }
        }
    }

    @SneakyThrows
    public Person pollFromUpQueue() {
        synchronized (peopleUp) {
            Person p;
            if (!peopleUp.isEmpty()) {
                p = peopleUp.take();
                log.info("Person with uuid {} was removed from up queue on {} floor. " +
                        "Queue length: {}", p.getUuid(), number, peopleUp.size());
            } else {
                p = null;
            }
            return p;
        }
    }

    @SneakyThrows
    public Person pollFromDownQueue() {
        synchronized (peopleDown) {
            Person p;
            if (!peopleDown.isEmpty()) {
                p = peopleDown.take();
                log.info("Person with uuid {} was removed from down queue on {} floor. " +
                        "Queue length: {}", p.getUuid(), number, peopleDown.size());
            } else {
                p = null;
            }
            return p;
        }
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
