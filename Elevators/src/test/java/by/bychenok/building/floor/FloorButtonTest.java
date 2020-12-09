package by.bychenok.building.floor;

import by.bychenok.building.elevator.ElevatorRequest;
import by.bychenok.building.elevator.ElevatorsManager;
import by.bychenok.building.statistics.StatisticsCollector;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import static by.bychenok.building.elevator.Direction.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class FloorButtonTest {

    @Nested
    class SingleFloor {
        @Test
        void press_success() {
            //GIVEN
            StatisticsCollector statisticsCollector = new StatisticsCollector(20);
            ElevatorsManager elevatorsManager = mock(ElevatorsManager.class);
            BlockingQueue<ElevatorRequest> requests = new LinkedBlockingQueue<>();
            FloorButton button = new FloorButton(1, UP, requests, statisticsCollector);
            button.press(elevatorsManager);

            //EXPECT
            verify(elevatorsManager).manageNewRequest();

            //AND
            assertEquals(requests.size(), 1);
        }

        @SneakyThrows
        @Test
        void press_doublePress_success() {
            //GIVEN
            StatisticsCollector statisticsCollector = new StatisticsCollector(20);
            ElevatorsManager elevatorsManager = mock(ElevatorsManager.class);
            BlockingQueue<ElevatorRequest> requests = new LinkedBlockingQueue<>();
            FloorButton button = new FloorButton(1, UP, requests, statisticsCollector);
            button.press(elevatorsManager);
            button.press(elevatorsManager);

            //EXPECT
            verify(elevatorsManager, times(1)).manageNewRequest();

            //AND
            assertEquals(requests.size(), 1);
        }


        @SneakyThrows
        @Test
        void press_pressPollRequestPress_success() {
            //GIVEN
            StatisticsCollector statisticsCollector = new StatisticsCollector(20);
            ElevatorsManager elevatorsManager = mock(ElevatorsManager.class);
            BlockingQueue<ElevatorRequest> requests = new LinkedBlockingQueue<>();
            FloorButton button = new FloorButton(1, UP, requests, statisticsCollector);
            button.press(elevatorsManager);
            requests.take();
            button.press(elevatorsManager);

            //EXPECT
            verify(elevatorsManager, times(1)).manageNewRequest();

            //AND
            assertEquals(requests.size(), 0);
        }

        @Test
        void press_pressResetPress_success() {
            //GIVEN
            StatisticsCollector statisticsCollector = new StatisticsCollector(20);
            ElevatorsManager elevatorsManager = mock(ElevatorsManager.class);
            BlockingQueue<ElevatorRequest> requests = new LinkedBlockingQueue<>();
            FloorButton button = new FloorButton(1, UP, requests, statisticsCollector);
            button.press(elevatorsManager);
            button.reset();
            button.press(elevatorsManager);

            //EXPECT
            verify(elevatorsManager, times(2)).manageNewRequest();

            //AND
            assertEquals(requests.size(), 2);
        }

        @SneakyThrows
        @Test
        void press_pressResetPollRequestPress_success() {
            //GIVEN
            StatisticsCollector statisticsCollector = new StatisticsCollector(20);
            ElevatorsManager elevatorsManager = mock(ElevatorsManager.class);
            BlockingQueue<ElevatorRequest> requests = new LinkedBlockingQueue<>();
            FloorButton button = new FloorButton(1, UP, requests, statisticsCollector);
            button.press(elevatorsManager);
            button.reset();
            requests.take();
            button.press(elevatorsManager);

            //EXPECT
            verify(elevatorsManager, times(2)).manageNewRequest();

            //AND
            assertEquals(requests.size(), 1);
        }
    }

    @Nested
    class TwoFloors {
        @SneakyThrows
        @Test
        void press_pressOnDifferentFloors_success() {
            //GIVEN
            StatisticsCollector statisticsCollector = new StatisticsCollector(20);
            ElevatorsManager elevatorsManager = mock(ElevatorsManager.class);
            BlockingQueue<ElevatorRequest> requests = new LinkedBlockingQueue<>();
            FloorButton button1 = new FloorButton(1, UP, requests, statisticsCollector);
            FloorButton button2 = new FloorButton(2, UP, requests, statisticsCollector);
            button1.press(elevatorsManager);
            button2.press(elevatorsManager);

            //EXPECT
            verify(elevatorsManager, times(2)).manageNewRequest();

            //AND
            assertEquals(requests.size(), 2);
        }
    }
}