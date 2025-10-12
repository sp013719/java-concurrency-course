package org.example;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class DeadlockExample {

    private final Lock lock1 = new ReentrantLock(true);
    private final Lock lock2 = new ReentrantLock(true);

    static void main() {
        DeadlockExample example = new DeadlockExample();

        new Thread(example::worker1).start();
        new Thread(example::worker2).start();
    }

    public void worker1() {
        this.lock1.lock();
        IO.println("Worker 1: Holding lock 1...");

        try {
            // Simulate some work with lock1
            Thread.sleep(300);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        this.lock2.lock();
        IO.println("Worker 1: Acquired lock 2!");
        this.lock2.unlock();
        this.lock1.unlock();
    }

    public void worker2() {
        this.lock2.lock();
        IO.println("Worker 2: Holding lock 2...");

        try {
            // Simulate some work with lock2
            Thread.sleep(300);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        this.lock1.lock();
        IO.println("Worker 2: Acquired lock 1!");
        this.lock1.unlock();
        this.lock2.unlock();
    }
}
