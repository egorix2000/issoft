package by.bychenok.carriage.entity;

import by.bychenok.cargo.entity.Cargo;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FreightCarriageTest {

    @Test
    void attachDetachCarriage_success() {
        //GIVEN
        FreightCarriage freightCarriage = FreightCarriage.of(0, 10);
        FreightCarriage fToAdd = FreightCarriage.of(1, 20);
        Locomotive locoToAdd = Locomotive.of(2);
        PassengerCarriage pToAdd = PassengerCarriage.of(3, 10);

        //EXPECT
        freightCarriage.attachCarriage(fToAdd);
        assertEquals(freightCarriage.getNext(), fToAdd);

        freightCarriage.detachCarriage();
        assertNull(freightCarriage.getNext());

        freightCarriage.attachCarriage(locoToAdd);
        assertEquals(freightCarriage.getNext(), locoToAdd);

        freightCarriage.detachCarriage();
        assertNull(freightCarriage.getNext());

        freightCarriage.attachCarriage(pToAdd);
        assertEquals(freightCarriage.getNext(), pToAdd);

        freightCarriage.detachCarriage();
        assertNull(freightCarriage.getNext());
    }

    @Test
    void attachCarriage_fail() {
        //GIVEN
        FreightCarriage freightCarriage = FreightCarriage.of(0, 10);
        FreightCarriage fToAdd = FreightCarriage.of(1, 20);
        Locomotive locoToAdd = Locomotive.of(2);

        //EXPECT
        assertThrows(IllegalArgumentException.class,
                () -> freightCarriage.attachCarriage(freightCarriage));

        freightCarriage.attachCarriage(fToAdd);
        assertThrows(IllegalArgumentException.class,
                () -> freightCarriage.attachCarriage(locoToAdd));
    }

    @Test
    void detachCarriage_fail() {
        //GIVEN
        FreightCarriage freightCarriage = FreightCarriage.of(0, 50);

        //EXPECT
        assertThrows(NullPointerException.class, freightCarriage::detachCarriage);
    }

    @Test
    void load_success() {
        //GIVEN
        FreightCarriage freightCarriage = FreightCarriage.of(0, 50);
        Cargo cargo = Cargo.of(0, 30);
        Cargo limitCargo = Cargo.of(1, 50);

        //EXPECTED
        freightCarriage.load(cargo);
        Cargo unloadedCargo = freightCarriage.unload();
        assertEquals(unloadedCargo, cargo);

        freightCarriage.load(limitCargo);
        unloadedCargo = freightCarriage.unload();
        assertEquals(unloadedCargo, limitCargo);
    }

    @Test
    void load_fail() {
        //GIVEN
        FreightCarriage freightCarriage = FreightCarriage.of(0, 50);
        Cargo tooHeavyCargo = Cargo.of(0, 51);

        //EXPECTED
        assertThrows(IllegalArgumentException.class,
                () -> freightCarriage.load(tooHeavyCargo));
    }

    @Test
    void unloadEmpty_fail() {
        //GIVEN
        FreightCarriage freightCarriage = FreightCarriage.of(0, 50);

        //EXPECT
        assertThrows(NullPointerException.class, freightCarriage::unload);
    }

    @Test
    void of_success() {
        //GIVEN
        int liftingCapacity = 10;
        FreightCarriage freightCarriage = FreightCarriage.of(0, liftingCapacity);

        //EXPECT
        assertEquals(freightCarriage.getLiftingCapacity(), liftingCapacity);
    }

    @Test
    void of_fail() {
        //GIVEN
        int negativeCapacity = -1;
        int zeroCapacity = 0;

        //EXPECT
        assertThrows(IllegalArgumentException.class,
                () -> FreightCarriage.of(1, negativeCapacity));

        assertThrows(IllegalArgumentException.class,
                () -> FreightCarriage.of(2, zeroCapacity));
    }
}