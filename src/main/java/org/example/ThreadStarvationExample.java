package org.example;

import java.util.concurrent.locks.ReentrantLock;

public class ThreadStarvationExample {

    private static final ReentrantLock lock = new ReentrantLock(true); // false = 不公平鎖

    static void main(String[] args) {
        // 啟動 5 個執行緒
        for (int i = 1; i <= 5; i++) {
            Thread t = new Thread(new Worker(), "Worker-" + i);
            t.start();
        }
    }

    static class Worker implements Runnable {
        @Override
        public void run() {
            while (true) {
                try {
                    if (lock.tryLock()) {
                        try {
                            System.out.println(Thread.currentThread().getName() + " 獲得鎖");
                            // 模擬長任務：佔用鎖 500ms
                            Thread.sleep(500);
                        } finally {
                            lock.unlock();
                        }
                    } else {
                        // 沒搶到鎖
                        System.out.println(Thread.currentThread().getName() + " 被餓住...");
                    }
                    Thread.sleep(100); // 稍作等待再嘗試
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        }
    }
}
