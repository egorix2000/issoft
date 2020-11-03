package by.bychenok.cargo.entity;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import static com.google.common.base.Preconditions.checkArgument;

@Slf4j
@Getter
public class Cargo {
    private final int id;
    private final int weight;

    private Cargo(int id, int weight) {
        this.id = id;
        this.weight = weight;
    }

    public static Cargo of(int id, int weight) {
        log.info("Cargo with id: {} is creating ...", id);
        checkArgument(weight > 0,
                "Weight must be more that zero");
        Cargo c =  new Cargo(id, weight);
        log.info("Cargo with id: {} was successfully created", id);
        return c;
    }
}
