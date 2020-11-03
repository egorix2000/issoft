package by.bychenok.user.entity;

import lombok.Getter;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;

import java.util.Objects;

@Slf4j
@Getter
public class User {
    private final String ssn;
    @Accessors(fluent = true)
    private boolean hasTrainLicence;

    private User(String ssn) {
        this.ssn = ssn;
        this.hasTrainLicence = false;
    }

    public static User ofSsn(String ssn) {
        log.info("User with ssn: {} is creating ...", ssn);
        User u = new User(ssn);
        log.info("User with ssn: {} was successfully created", ssn);
        return u;
    }

    public void passDrivingExam() {
        log.info("User with ssn: {} getting licence ...", ssn);
        this.hasTrainLicence = true;
        log.info("User with ssn: {} successfully got licence", ssn);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return ssn.equals(user.ssn);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ssn);
    }
}
