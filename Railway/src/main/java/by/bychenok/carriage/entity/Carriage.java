package by.bychenok.carriage.entity;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.Objects;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

@Slf4j
@Getter
public abstract class Carriage {
    private final int number;
    private Carriage next;

    protected Carriage(int number) {
        this.number = number;
        this.next = null;
    }

    public void attachCarriage(Carriage c) {
        log.info("Attaching carriage: {} to carriage: {} ...",
                c.getNumber(), this.getNumber());
        checkArgument(this.next == null,
                "Next carriage is already attached");
        checkArgument(!(this.equals(c)),
                "Can not attach carriage to itself");
        this.next = c;
        log.info("Carriage: {} was attached successfully to carriage: {} ...",
                c.getNumber(), this.getNumber());
    }

    public Carriage detachCarriage() {
        checkNotNull(this.next, "No attached carriage found");
        log.info("Detaching carriage: {} from carriage: {} ...",
                this.next.getNumber(), this.getNumber());
        Carriage detached = this.next;
        this.next = null;
        log.info("Carriage: {} was detached successfully from carriage: {} ...",
                detached.getNumber(), this.getNumber());
        return detached;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Carriage carriage = (Carriage) o;
        return number == carriage.number;
    }

    @Override
    public int hashCode() {
        return Objects.hash(number);
    }
}
