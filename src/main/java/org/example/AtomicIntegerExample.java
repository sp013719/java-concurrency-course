package org.example;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Example demonstrating the use of AtomicInteger for thread-safe operations
 * compared to a non-atomic integer.
 */
public class AtomicIntegerExample {

    private static int counter = 0;
    private static final AtomicInteger atomicCounter = new AtomicInteger(0);

    static void main() {
        Thread t1 = new Thread(AtomicIntegerExample::incrementAtomic);
        Thread t2 = new Thread(AtomicIntegerExample::incrementAtomic);

        t1.start();
        t2.start();

        try {
            t1.join();
            t2.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        IO.println("Final Counter Value: " + atomicCounter.get());
    }

    /**
     * Non-atomic increment method, not thread-safe
     * To resolve race conditions, consider using synchronized blocks or methods.
     */
    private static void increment() {
        for (int i = 0; i < 10000; i++) {
            counter++;
        }
    }

    /**
     * Atomic increment method, thread-safe
     */
    private static void incrementAtomic() {
        for (int i = 0; i < 10000; i++) {
            atomicCounter.incrementAndGet();
        }
    }
}
