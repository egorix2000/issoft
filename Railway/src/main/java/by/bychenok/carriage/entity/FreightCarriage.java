package by.bychenok.carriage.entity;

import by.bychenok.cargo.entity.Cargo;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

@Slf4j
public class FreightCarriage extends Carriage {
    @Getter
    private final int liftingСapacity;
    private Cargo cargo;

    private FreightCarriage(int number, int liftingСapacity) {
        super(number);
        this.liftingСapacity = liftingСapacity;
        this.cargo = null;
    }

    public static FreightCarriage of(int number, int liftingСapacity) {
        log.info("Freight carriage with number: {} is creating ...", number);
        checkArgument(liftingСapacity > 0,
                "Lifting capacity must be more that zero");
        FreightCarriage f = new FreightCarriage(number, liftingСapacity);
        log.info("Freight carriage with number: {} was successfully created", number);
        return f;
    }

    public void load(Cargo cargo) {
        log.info("Loading cargo: {} into freight carriage: {} ...",
                cargo.getId(),
                this.getNumber());
        checkArgument(cargo.getWeight() <= liftingСapacity,
                "Cargo is too heavy");
        checkArgument(this.cargo == null,
                "Carriage is already loaded");
        this.cargo = cargo;
        log.info("Cargo: {} was loaded successfully into freight carriage: {} ...",
                cargo.getId(),
                this.getNumber());
    }

    public Cargo unload() {
        checkNotNull(cargo, "No cargo to unload");
        log.info("Unloading cargo: {} from freight carriage: {} ...",
                cargo.getId(),
                this.getNumber());
        Cargo c = this.cargo;
        this.cargo = null;
        log.info("Cargo: {} was unloading successfully from freight carriage: {} ...",
                c.getId(),
                this.getNumber());
        return c;
    }
}
