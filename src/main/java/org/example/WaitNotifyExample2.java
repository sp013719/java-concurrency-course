package org.example;

import java.util.LinkedList;
import java.util.List;

public class WaitNotifyExample2 {

    static class SharedBuffer {
        private List<Integer> buffer = new LinkedList<>();
        private final int capacity = 10;

        public synchronized void produce() throws InterruptedException {
            if (this.buffer.size() == this.capacity) {
                IO.println("Buffer full, producer waiting...");
                wait();
            }

            IO.println("Adding items with the producer...");

            for (int i = 0; i <this.capacity; i++) {
                this.buffer.add(i);
                IO.println("Add value: " + i);
            }

            // wake up the consumer
            notify();
        }

        public synchronized void consume() throws InterruptedException {
            if (this.buffer.size() < this.capacity ) {
                IO.println("Buffer not full yet, consumer waiting...");
                wait();
            }

            while (!this.buffer.isEmpty()) {
                int item = this.buffer.removeFirst();
                IO.println("Consumer removes:" + item);
                Thread.sleep(300);
            }

            notify();
        }
    }

    static class Consumer implements Runnable {

        private final SharedBuffer sharedBuffer;

        public Consumer(SharedBuffer sharedBuffer) {
            this.sharedBuffer = sharedBuffer;
        }

        @Override
        public void run() {
            try {
                while (true) {
                    this.sharedBuffer.consume();
                    Thread.sleep(500);
                }
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    static class Producer implements Runnable {

        private SharedBuffer sharedBuffer;

        public Producer(SharedBuffer sharedBuffer) {
            this.sharedBuffer = sharedBuffer;
        }

        @Override
        public void run() {
            try {
                while (true) {
                    this.sharedBuffer.produce();
                    Thread.sleep(500);
                }
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    static void main() {
        var sharedBuffer = new SharedBuffer();

        Thread t1 = new Thread(new Producer(sharedBuffer));
        Thread t2 = new Thread(new Consumer(sharedBuffer));

        t1.start();
        t2.start();
    }
}
