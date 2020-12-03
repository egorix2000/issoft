package by.bychenok.building.elevator;

import lombok.SneakyThrows;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ElevatorsManager implements Runnable {
    private final BlockingQueue<ElevatorRequest> requests;
    private final List<Elevator> elevators;
    private Lock lock;

    public ElevatorsManager(BlockingQueue<ElevatorRequest> requests, int elevatorCount) {
        this.requests = requests;
        // MAKE IMMUTABLE
        elevators = new ArrayList<>(elevatorCount);
        lock = new ReentrantLock();
    }

    public void manageNewTask() {
        notifyAll();
    }

    @SneakyThrows
    @Override
    public void run() {
        while (!Thread.interrupted()) {
            while (!requests.isEmpty()) {
                ElevatorRequest request = requests.take();

            }
            lock.unlock();
        }
    }
}
