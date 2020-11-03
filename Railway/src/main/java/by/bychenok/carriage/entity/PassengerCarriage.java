package by.bychenok.carriage.entity;

import by.bychenok.user.entity.User;
import com.google.common.collect.ImmutableList;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.*;
import java.util.stream.Collectors;

import static com.google.common.base.Preconditions.checkArgument;

@Slf4j
public class PassengerCarriage extends Carriage {
    private final Map<Integer, Seat> seatMap;
    private final int size;

    private PassengerCarriage(int number, int size) {
        super(number);
        this.size = size;
        seatMap = new HashMap<>();
        for (int index = 1; index <= size; index++) {
            seatMap.put(index, Seat.of(index));
        }
    }

    public static PassengerCarriage of(int number, int size) {
        log.info("Passenger carriage with number: {} is creating ...", number);
        checkArgument(size > 0,
                "Size must be more that zero");
        PassengerCarriage p = new PassengerCarriage(number, size);
        log.info("Passenger carriage with number: {} was successfully created", number);
        return p;
    }

    public void addPassenger(int placeNumber, User passenger) {
        log.info("Try to accommodate passenger: {} into carriage: {} on place {}",
                passenger.getSsn(), this.getNumber(), placeNumber);
        checkArgument(placeNumber <= this.size);
        checkArgument(seatMap.get(placeNumber).isFree());
        seatMap.get(placeNumber).accommodatePassenger(passenger);
        log.info("Passenger was successfully accommodated : {} into carriage: {} on place {}",
                passenger.getSsn(), this.getNumber(), placeNumber);
    }

    public ImmutableList<Seat> getSeatList() {
        return ImmutableList.copyOf(seatMap.values()
                .stream().map(Seat::of).collect(Collectors.toList()));
    }

    public User getPassenger(int placeNumber) {
        return seatMap.get(placeNumber).getPassenger();
    }

    public int getSize() {
        return size;
    }

    @Getter
    public static class Seat {
        private final int number;
        private User passenger;

        private Seat(int number) {
            this.number = number;
            this.passenger = null;
        }

        private Seat(Seat seat) {
            this.number = seat.getNumber();
            this.passenger = seat.getPassenger();
        }

        public static Seat of(int number) {
            return new Seat(number);
        }

        public static Seat of(Seat seat) {
            return new Seat(seat);
        }

        public boolean isFree() {
            return this.passenger == null;
        }

        public void accommodatePassenger(User passenger) {
            this.passenger = passenger;
        }

        public void freeSeat() {
            this.passenger = null;
        }

    }
}
