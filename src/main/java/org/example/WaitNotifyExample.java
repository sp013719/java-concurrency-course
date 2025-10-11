package org.example;

public class WaitNotifyExample {

    private static class Process {

        public void produce() throws InterruptedException {
            synchronized (this) {
                IO.println("Running the produce method...");
                wait();
                IO.println("Again in the produce method...");
            }
        }

        public void consume() throws InterruptedException {
            Thread.sleep(1000);

            synchronized (this) {
                IO.println("Running the consume method...");
                notify();
                IO.println("After the notify() method call in the consume method");
            }
        }
    }

    static void main() {
        var process = new Process();

        var t1 = new Thread(() -> {
            try {
                process.produce();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });

        t1.start();

        var t2 = new Thread(() -> {
            try {
                process.consume();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });

        t2.start();
    }
}
