package by.bychenok.building.elevator;

import by.bychenok.building.configuration.ElevatorConfig;
import by.bychenok.building.floor.FloorSystem;
import com.google.common.collect.ImmutableList;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static by.bychenok.building.elevator.Direction.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class ElevatorSearchEngineTest {

    @Test
    void findElevatorToHandleRequest_empty_returnEmpty() {
        //GIVEN
        ElevatorSearchEngine searchEngine = new ElevatorSearchEngine();
        List<Elevator> elevators = ImmutableList.of();
        ElevatorRequest request = new ElevatorRequest(UUID.randomUUID(), 1, UP);
        Optional<Integer> foundElevatorId =
                searchEngine.findElevatorToHandleRequest(elevators, request);

        //EXPECT
        assertFalse(foundElevatorId.isPresent());
    }

    @Test
    void findElevatorToHandleRequest_available_success() {
        //GIVEN
        ElevatorSearchEngine searchEngine = new ElevatorSearchEngine();
        FloorSystem floorSystem = mock(FloorSystem.class);
        ElevatorsManager elevatorsManager = mock(ElevatorsManager.class);
        int id = 0;
        ElevatorConfig config = new ElevatorConfig(
                0,
                0,
                0,
                0
        );
        Elevator availableElevator = new Elevator(
                id, config, floorSystem, elevatorsManager);
        List<Elevator> elevators = ImmutableList.of(availableElevator);
        ElevatorRequest request = new ElevatorRequest(UUID.randomUUID(), 1, UP);
        Optional<Integer> foundElevatorId =
                searchEngine.findElevatorToHandleRequest(elevators, request);

        //EXPECT
        assertTrue(foundElevatorId.isPresent());

        //AND
        assertEquals(foundElevatorId.get(), id);
    }

    @Test
    void findElevatorToHandleRequest_carryingUp_success() {
        //GIVEN
        ElevatorSearchEngine searchEngine = new ElevatorSearchEngine();
        FloorSystem floorSystem = mock(FloorSystem.class);
        ElevatorsManager elevatorsManager = mock(ElevatorsManager.class);
        int id = 0;
        int startFloor = 2;
        ElevatorConfig config = new ElevatorConfig(
                0,
                0,
                0,
                startFloor
        );
        Elevator carryingElevator = new Elevator(
                id, config, floorSystem, elevatorsManager);
        ElevatorRequest elevatorRequest = new ElevatorRequest(UUID.randomUUID(), 1, UP);
        carryingElevator.pickUpPassenger(elevatorRequest);

        List<Elevator> elevators = ImmutableList.of(carryingElevator);
        ElevatorRequest request = new ElevatorRequest(UUID.randomUUID(), 3, UP);
        Optional<Integer> foundElevatorId =
                searchEngine.findElevatorToHandleRequest(elevators, request);

        //EXPECT
        assertTrue(foundElevatorId.isPresent());

        //AND
        assertEquals(foundElevatorId.get(), id);
    }

    @Test
    void findElevatorToHandleRequest_carryingDown_success() {
        //GIVEN
        ElevatorSearchEngine searchEngine = new ElevatorSearchEngine();
        FloorSystem floorSystem = mock(FloorSystem.class);
        ElevatorsManager elevatorsManager = mock(ElevatorsManager.class);
        int id = 0;
        int startFloor = 2;
        ElevatorConfig config = new ElevatorConfig(
                0,
                0,
                0,
                startFloor
        );
        Elevator carryingElevator = new Elevator(
                id, config, floorSystem, elevatorsManager);
        ElevatorRequest elevatorRequest = new ElevatorRequest(UUID.randomUUID(), 3, DOWN);
        carryingElevator.pickUpPassenger(elevatorRequest);

        List<Elevator> elevators = ImmutableList.of(carryingElevator);
        ElevatorRequest request = new ElevatorRequest(UUID.randomUUID(), 1, DOWN);
        Optional<Integer> foundElevatorId =
                searchEngine.findElevatorToHandleRequest(elevators, request);

        //EXPECT
        assertTrue(foundElevatorId.isPresent());

        //AND
        assertEquals(foundElevatorId.get(), id);
    }

    @Test
    void findElevatorToHandleRequest_noSuitable_returnEmpty() {
        //GIVEN
        ElevatorSearchEngine searchEngine = new ElevatorSearchEngine();
        FloorSystem floorSystem = mock(FloorSystem.class);
        ElevatorsManager elevatorsManager = mock(ElevatorsManager.class);

        int startUpElevatorFloor = 2;
        ElevatorConfig configUp = new ElevatorConfig(
                0,
                0,
                0,
                startUpElevatorFloor
        );
        Elevator carryingElevatorUp = new Elevator(
                0, configUp, floorSystem, elevatorsManager);
        ElevatorRequest elevatorUpRequest = new ElevatorRequest(UUID.randomUUID(), 4, UP);
        carryingElevatorUp.pickUpPassenger(elevatorUpRequest);

        int startDownElevatorFloor = 3;
        ElevatorConfig configDown = new ElevatorConfig(
                0,
                0,
                0,
                startDownElevatorFloor
        );
        Elevator carryingElevatorDown = new Elevator(
                0, configDown, floorSystem, elevatorsManager);
        ElevatorRequest elevatorDownRequest = new ElevatorRequest(UUID.randomUUID(), 3, DOWN);
        carryingElevatorDown.pickUpPassenger(elevatorDownRequest);

        List<Elevator> elevators = ImmutableList.of(carryingElevatorUp, carryingElevatorDown);
        ElevatorRequest request = new ElevatorRequest(UUID.randomUUID(), 2, UP);
        Optional<Integer> foundElevatorId =
                searchEngine.findElevatorToHandleRequest(elevators, request);

        //EXPECT
        assertFalse(foundElevatorId.isPresent());
    }
}