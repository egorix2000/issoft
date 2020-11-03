package by.bychenok.carriage.entity;

import by.bychenok.cargo.entity.Cargo;
import by.bychenok.user.entity.User;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import static com.google.common.base.Preconditions.checkArgument;

@Slf4j
@Getter
public class Locomotive extends Carriage {
    private User driver;

    private Locomotive(int number) {
        super(number);
    }

    public static Locomotive of(int number) {
        log.info("Locomotive with number: {} is creating ...", number);
        Locomotive loco = new Locomotive(number);
        log.info("Locomotive with number: {} was successfully created", number);
        return loco;
    }

    public void setDriver(User driver) {
        log.info("Setting driver: {} into locomotive: {} ...",
                driver.getSsn(),
                this.getNumber());
        checkArgument(driver.hasTrainLicence() == true,
                "User without train licence can not drive train");
        this.driver = driver;
        log.info("Driver: {} was set successfully into locomotive: {} ...",
                driver.getSsn(),
                this.getNumber());
    }
}
