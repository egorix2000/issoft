package by.bychenok.building.elevator;

import by.bychenok.building.configuration.ElevatorConfig;
import by.bychenok.building.floor.FloorSystem;
import by.bychenok.building.statistics.StatisticsCollector;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static by.bychenok.building.elevator.Direction.*;
import static org.mockito.Mockito.mock;
import static org.junit.jupiter.api.Assertions.*;

class ElevatorTest {

    @Test
    void pickUpPassenger_availableDownRequest_success() {
        //GIVEN
        StatisticsCollector statisticsCollector = new StatisticsCollector(20);
        ElevatorsManager elevatorsManager = mock(ElevatorsManager.class);
        FloorSystem floorSystem = mock(FloorSystem.class);
        ElevatorConfig config = new ElevatorConfig(
                100,
                1,
                1,
                0
        );
        Elevator elevator = new Elevator(1,
                config,
                floorSystem,
                elevatorsManager,
                statisticsCollector);
        ElevatorRequest request = new ElevatorRequest(UUID.randomUUID(), 1, DOWN);
        elevator.lock();
            elevator.pickUpPassenger(request);

            //EXPECT
            assertTrue(elevator.isCarryingDown());
        elevator.unlock();
    }

    @Test
    void pickUpPassenger_availableUpRequest_success() {
        //GIVEN
        StatisticsCollector statisticsCollector = new StatisticsCollector(20);
        ElevatorsManager elevatorsManager = mock(ElevatorsManager.class);
        FloorSystem floorSystem = mock(FloorSystem.class);
        ElevatorConfig config = new ElevatorConfig(
                100,
                1,
                1,
                0
        );
        Elevator elevator = new Elevator(1,
                config,
                floorSystem,
                elevatorsManager,
                statisticsCollector);
        ElevatorRequest request = new ElevatorRequest(UUID.randomUUID(), 1, UP);
        elevator.lock();
            elevator.pickUpPassenger(request);

            //EXPECT
            assertTrue(elevator.isCarryingUp());
        elevator.unlock();
    }

    @Test
    void pickUpPassenger_carryingWrongDirection_exceptionThrown() {
        //GIVEN
        StatisticsCollector statisticsCollector = new StatisticsCollector(20);
        ElevatorsManager elevatorsManager = mock(ElevatorsManager.class);
        FloorSystem floorSystem = mock(FloorSystem.class);
        ElevatorConfig config = new ElevatorConfig(
                100,
                1,
                1,
                0
        );
        Elevator elevator = new Elevator(1,
                config,
                floorSystem,
                elevatorsManager,
                statisticsCollector);
        ElevatorRequest requestUp = new ElevatorRequest(UUID.randomUUID(), 1, UP);
        ElevatorRequest requestDown = new ElevatorRequest(UUID.randomUUID(), 1, DOWN);
        elevator.lock();
            elevator.pickUpPassenger(requestUp);

            //EXPECT
            assertThrows(IllegalArgumentException.class,
                    () -> elevator.pickUpPassenger(requestDown));
        elevator.unlock();
    }

    @Test
    void pickUpPassenger_carryingSameDirectionUp_success() {
        //GIVEN
        StatisticsCollector statisticsCollector = new StatisticsCollector(20);
        ElevatorsManager elevatorsManager = mock(ElevatorsManager.class);
        FloorSystem floorSystem = mock(FloorSystem.class);
        ElevatorConfig config = new ElevatorConfig(
                100,
                1,
                1,
                0
        );
        Elevator elevator = new Elevator(1,
                config,
                floorSystem,
                elevatorsManager,
                statisticsCollector);
        ElevatorRequest requestUp = new ElevatorRequest(UUID.randomUUID(), 1, UP);
        ElevatorRequest additionalRequestUp = new ElevatorRequest(UUID.randomUUID(), 2, UP);
        elevator.lock();
            elevator.pickUpPassenger(requestUp);

            //EXPECT
            assertDoesNotThrow(() -> elevator.pickUpPassenger(additionalRequestUp));
        elevator.unlock();
    }

    @Test
    void pickUpPassenger_carryingSameDirectionDown_success() {
        //GIVEN
        StatisticsCollector statisticsCollector = new StatisticsCollector(20);
        ElevatorsManager elevatorsManager = mock(ElevatorsManager.class);
        FloorSystem floorSystem = mock(FloorSystem.class);
        ElevatorConfig config = new ElevatorConfig(
                100,
                1,
                1,
                0
        );
        Elevator elevator = new Elevator(1,
                config,
                floorSystem,
                elevatorsManager,
                statisticsCollector);
        ElevatorRequest requestDown = new ElevatorRequest(UUID.randomUUID(), 1, UP);
        ElevatorRequest additionalRequestDown = new ElevatorRequest(UUID.randomUUID(), 2, UP);
        elevator.lock();
            elevator.pickUpPassenger(requestDown);

            //EXPECT
            assertDoesNotThrow(() -> elevator.pickUpPassenger(additionalRequestDown));
        elevator.unlock();
    }
}