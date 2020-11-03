package by.bychenok.carriage.entity;

import by.bychenok.cargo.entity.Cargo;
import org.junit.jupiter.api.Test;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import static org.junit.jupiter.api.Assertions.*;

class FreighCarriageTest {

    @Test
    void attachDetachCarriage_success() {
        //GIVEN
        FreighCarriage freighCarriage = FreighCarriage.of(0, 10);
        FreighCarriage fToAdd = FreighCarriage.of(1, 20);
        Locomotive locoToAdd = Locomotive.of(2);
        PassengerCarriage pToAdd = PassengerCarriage.of(3, 10);

        //EXPECT
        freighCarriage.attachCarriage(fToAdd);
        assertEquals(freighCarriage.getNext(), fToAdd);

        freighCarriage.detachCarriage();
        assertEquals(freighCarriage.getNext(), null);

        freighCarriage.attachCarriage(locoToAdd);
        assertEquals(freighCarriage.getNext(), locoToAdd);

        freighCarriage.detachCarriage();
        assertEquals(freighCarriage.getNext(), null);

        freighCarriage.attachCarriage(pToAdd);
        assertEquals(freighCarriage.getNext(), pToAdd);

        freighCarriage.detachCarriage();
        assertEquals(freighCarriage.getNext(), null);
    }

    @Test
    void attachCarriage_fail() {
        //GIVEN
        FreighCarriage freighCarriage = FreighCarriage.of(0, 10);
        FreighCarriage fToAdd = FreighCarriage.of(1, 20);
        Locomotive locoToAdd = Locomotive.of(2);

        //EXPECT
        assertThrows(IllegalArgumentException.class, () -> {
            freighCarriage.attachCarriage(freighCarriage);
        });

        freighCarriage.attachCarriage(fToAdd);
        assertThrows(IllegalArgumentException.class, () -> {
            freighCarriage.attachCarriage(locoToAdd);
        });
    }

    @Test
    void detachCarriage_fail() {
        //GIVEN
        FreighCarriage freighCarriage = FreighCarriage.of(0, 50);

        //EXPECT
        assertThrows(NullPointerException.class, () -> {
            freighCarriage.detachCarriage();
        });
    }

    @Test
    void load_success() {
        //GIVEN
        FreighCarriage freighCarriage = FreighCarriage.of(0, 50);
        Cargo cargo = Cargo.of(0, 30);
        Cargo limitCargo = Cargo.of(1, 50);

        //EXPECTED
        freighCarriage.load(cargo);
        Cargo unloadedCargo = freighCarriage.unload();
        assertEquals(unloadedCargo, cargo);

        freighCarriage.load(limitCargo);
        unloadedCargo = freighCarriage.unload();
        assertEquals(unloadedCargo, limitCargo);
    }

    @Test
    void load_fail() {
        //GIVEN
        FreighCarriage freighCarriage = FreighCarriage.of(0, 50);
        Cargo tooHeavyCargo = Cargo.of(0, 51);

        //EXPECTED
        assertThrows(IllegalArgumentException.class, () -> {
            freighCarriage.load(tooHeavyCargo);
        });
    }

    @Test
    void unloadEmpty_fail() {
        //GIVEN
        FreighCarriage freighCarriage = FreighCarriage.of(0, 50);

        //EXPECT
        assertThrows(NullPointerException.class, () -> {
            freighCarriage.unload();
        });
    }

    @Test
    void of_success() {
        //GIVEN
        int liftingCapacity = 10;
        FreighCarriage freighCarriage = FreighCarriage.of(0, liftingCapacity);

        //EXPECT
        assertEquals(freighCarriage.getLiftingСapacity(), liftingCapacity);
    }

    @Test
    void of_fail() {
        //GIVEN
        int negativeCapacity = -1;
        int zeroCapacity = 0;

        //EXPECT
        assertThrows(IllegalArgumentException.class, () -> {
            FreighCarriage freighCarriage = FreighCarriage.of(1, negativeCapacity);
        });

        assertThrows(IllegalArgumentException.class, () -> {
            FreighCarriage freighCarriage = FreighCarriage.of(2, zeroCapacity);
        });
    }
}