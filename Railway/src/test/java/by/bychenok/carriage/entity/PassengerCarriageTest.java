package by.bychenok.carriage.entity;

import by.bychenok.user.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class PassengerCarriageTest {

    @Test
    void attachDetachCarriage_success() {
        //GIVEN
        PassengerCarriage passengerCarriage = PassengerCarriage.of(0, 10);
        FreighCarriage fToAdd = FreighCarriage.of(1, 20);
        Locomotive locoToAdd = Locomotive.of(2);
        PassengerCarriage pToAdd = PassengerCarriage.of(3, 10);

        //EXPECT
        passengerCarriage.attachCarriage(fToAdd);
        assertEquals(passengerCarriage.getNext(), fToAdd);

        passengerCarriage.detachCarriage();
        assertEquals(passengerCarriage.getNext(), null);

        passengerCarriage.attachCarriage(locoToAdd);
        assertEquals(passengerCarriage.getNext(), locoToAdd);

        passengerCarriage.detachCarriage();
        assertEquals(passengerCarriage.getNext(), null);

        passengerCarriage.attachCarriage(pToAdd);
        assertEquals(passengerCarriage.getNext(), pToAdd);

        passengerCarriage.detachCarriage();
        assertEquals(passengerCarriage.getNext(), null);
    }

    @Test
    void attachCarriage_fail() {
        //GIVEN
        PassengerCarriage passengerCarriage = PassengerCarriage.of(0, 10);
        FreighCarriage fToAdd = FreighCarriage.of(1, 20);
        Locomotive locoToAdd = Locomotive.of(2);

        //EXPECT
        assertThrows(IllegalArgumentException.class, () -> {
            passengerCarriage.attachCarriage(passengerCarriage);
        });

        passengerCarriage.attachCarriage(fToAdd);
        assertThrows(IllegalArgumentException.class, () -> {
            passengerCarriage.attachCarriage(locoToAdd);
        });
    }

    @Test
    void detachCarriage_fail() {
        //GIVEN
        PassengerCarriage passengerCarriage = PassengerCarriage.of(0, 30);

        //EXPEXT
        assertThrows(NullPointerException.class, () -> {
            passengerCarriage.detachCarriage();
        });
    }

    @Test
    void of_success() {
        //GIVEN
        int size = 10;
        PassengerCarriage passengerCarriage = PassengerCarriage.of(0, size);

        //EXPECT
        assertEquals(passengerCarriage.getSize(), size);
    }

    @Test
    void of_fail() {
        //EXPECT
        assertThrows(IllegalArgumentException.class, () -> {
            PassengerCarriage passengerCarriage = PassengerCarriage.of(1, -1);
        });

        assertThrows(IllegalArgumentException.class, () -> {
            PassengerCarriage passengerCarriage = PassengerCarriage.of(2, 0);
        });
    }

    @Test
    void addPassenger_success() {
        //GIVEN
        PassengerCarriage passengerCarriage = PassengerCarriage.of(0, 10);
        User passenger = User.ofSsn(UUID.randomUUID().toString());
        User one_more_passenger = User.ofSsn(UUID.randomUUID().toString());

        //EXPECT
        passengerCarriage.addPassenger(2, passenger);
        assertEquals(passengerCarriage.getPassenger(2), passenger);

        passengerCarriage.addPassenger(10, one_more_passenger);
        assertEquals(passengerCarriage.getPassenger(10), one_more_passenger);
    }

    @Test
    void addPassenger_fail() {
        //GIVEN
        PassengerCarriage passengerCarriage = PassengerCarriage.of(0, 10);
        User passenger = User.ofSsn(UUID.randomUUID().toString());
        User extra_passenger = User.ofSsn(UUID.randomUUID().toString());

        //EXPECT
        passengerCarriage.addPassenger(2, passenger);
        assertThrows(IllegalArgumentException.class, () -> {
            passengerCarriage.addPassenger(2, extra_passenger);
        });

        assertThrows(IllegalArgumentException.class, () -> {
            passengerCarriage.addPassenger(11, extra_passenger);
        });
    }

    @Test
    void getSeatList_immutable() {
        //GIVEN
        PassengerCarriage passengerCarriage = PassengerCarriage.of(0, 10);
        User passenger = User.ofSsn(UUID.randomUUID().toString());

        //EXPECT
        passengerCarriage.getSeatList().get(0).accommodatePassenger(passenger);
        assertEquals(passengerCarriage.getSeatList()
                .stream().filter(s -> !s.isFree()).count(), 0);
    }
}