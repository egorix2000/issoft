package by.bychenok.user.entity;

import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    @Test
    void passDrivingExam() {
        //GIVEN
        User user = User.ofSsn(UUID.randomUUID().toString());

        //EXPECT
        assertFalse(user.hasTrainLicence());

        user.passDrivingExam();
        assertTrue(user.hasTrainLicence());
    }
}