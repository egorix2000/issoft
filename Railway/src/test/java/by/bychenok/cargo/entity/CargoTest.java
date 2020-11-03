package by.bychenok.cargo.entity;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CargoTest {

    @Test
    void of_success() {
        //GIVEN
        int weight = 10;
        Cargo cargo = Cargo.of(0, weight);

        //EXPECT
        assertEquals(cargo.getWeight(), weight);
    }

    @Test
    void of_fail() {
        //EXPECT
        assertThrows(IllegalArgumentException.class, () -> {
            Cargo cargo = Cargo.of(1, -2);
        });

        assertThrows(IllegalArgumentException.class, () -> {
            Cargo cargo = Cargo.of(2, 0);
        });
    }
}