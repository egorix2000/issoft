package by.bychenok.carriage.entity;

import by.bychenok.user.entity.User;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class LocomotiveTest {

    @Test
    void attachDetachCarriage_success() {
        //GIVEN
        Locomotive loco = Locomotive.of(0);
        FreightCarriage fToAdd = FreightCarriage.of(1, 20);
        Locomotive locoToAdd = Locomotive.of(2);
        PassengerCarriage pToAdd = PassengerCarriage.of(3, 10);

        //EXPECT
        loco.attachCarriage(fToAdd);
        assertEquals(loco.getNext(), fToAdd);

        loco.detachCarriage();
        assertNull(loco.getNext());

        loco.attachCarriage(locoToAdd);
        assertEquals(loco.getNext(), locoToAdd);

        loco.detachCarriage();
        assertNull(loco.getNext());

        loco.attachCarriage(pToAdd);
        assertEquals(loco.getNext(), pToAdd);

        loco.detachCarriage();
        assertNull(loco.getNext());
    }

    @Test
    void attachCarriage_fail() {
        //GIVEN
        Locomotive loco = Locomotive.of(0);
        FreightCarriage fToAdd = FreightCarriage.of(1, 20);
        Locomotive locoToAdd = Locomotive.of(2);

        //EXPECT
        assertThrows(IllegalArgumentException.class, () -> loco.attachCarriage(loco));

        loco.attachCarriage(fToAdd);
        assertThrows(IllegalArgumentException.class, () -> loco.attachCarriage(locoToAdd));
    }

    @Test
    void detachCarriage_fail() {
        //GIVEN
        Locomotive loco = Locomotive.of(0);

        //EXPECT
        assertThrows(NullPointerException.class, loco::detachCarriage);
    }


    @Test
    void setDriver_success() {
        //GIVEN
        Locomotive loco = Locomotive.of(0);
        User driver = User.ofSsn(UUID.randomUUID().toString());
        driver.passDrivingExam();

        //EXPECT
        loco.setDriver(driver);
        assertEquals(loco.getDriver(), driver);
    }

    @Test
    void setDriver_fail() {
        //GIVEN
        Locomotive loco = Locomotive.of(0);
        User driver = User.ofSsn(UUID.randomUUID().toString());

        //EXPECT
        assertThrows(IllegalArgumentException.class, () -> loco.setDriver(driver));
    }
}