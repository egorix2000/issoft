package by.bychenok.building;

import by.bychenok.person.Person;
import lombok.Getter;

import java.util.LinkedList;
import java.util.Queue;


public class Floor {
    @Getter
    private boolean isUpPressed;
    @Getter
    private boolean isDownPressed;
    private Queue<Person> peopleUp;
    private Queue<Person> peopleDown;

    public Floor() {
        this.isUpPressed = false;
        this.isDownPressed = false;
        peopleUp = new LinkedList<>();
        peopleDown = new LinkedList<>();
    }

    public void pressUp() {
        isUpPressed = true;
    }

    public void pressDown() {
        isDownPressed = true;
    }
}
