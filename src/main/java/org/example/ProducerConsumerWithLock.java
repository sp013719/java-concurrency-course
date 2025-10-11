package org.example;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ProducerConsumerWithLock {

    private static class Worker {

        private final Lock lock = new ReentrantLock();
        private final Condition condition = lock.newCondition();

        public void produce() throws InterruptedException {
            this.lock.lock();
            IO.println("Produce method...");
            this.condition.await();
            IO.println("Again the produce method");
            this.lock.unlock();
        }

        public void consume() throws InterruptedException {
            // make sure produce method runs before consume method
            Thread.sleep(2000);
            this.lock.lock();
            IO.println("Consume method...");
            Thread.sleep(3000);
            // notify
            this.condition.signal();
            this.lock.unlock();
        }
    }

    static void main() {
        Worker worker = new Worker();

        Thread t1 = new Thread(() -> {
            try {
                worker.produce();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });

        Thread t2 = new Thread(() -> {
            try {
                worker.consume();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });

        t1.start();
        t2.start();

        try {
            t1.join();
            t2.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        IO.println("Main method is completed");
    }
}
