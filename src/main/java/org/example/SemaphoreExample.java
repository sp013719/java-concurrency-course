package org.example;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

/**
 * Example demonstrating the use of Semaphore to limit concurrent access
 * to a resource (e.g., downloading data) to a fixed number of threads.
 */
public class SemaphoreExample {

    private enum Downloader {
        // Singleton instance
        INSTANCE;

        private final Semaphore semaphore = new Semaphore(3, true);

        public void download() {
            try {
                semaphore.acquire();
                downloadData();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            } finally {
                semaphore.release();
            }
        }

        /**
         * Simulates downloading data with a delay.
         */
        private void downloadData() {
            IO.println("Downloading data...");
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            IO.println("Data downloaded.");
        }
    }

    static void main() {
        try (ExecutorService service = Executors.newCachedThreadPool()) {
            for (int i = 0; i < 12; i++) {
//                service.submit(Downloader.INSTANCE::download);
                service.execute(Downloader.INSTANCE::download);
            }
        }
    }
}
