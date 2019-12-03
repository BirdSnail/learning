package com.github.effective.java.training.thread;

/**
 * @author BirdSnail
 * @date 2019/11/19
 */
public class LopLock {

    private static final Object lock1 = new Object();
    private static final Object lock2 = new Object();

    public static void main(String[] args) {
        new Thread(() -> {
            synchronized (lock1) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                synchronized (lock2) {
                    doSomething();
                }
            }
        }).start();

        new Thread(() -> {
            synchronized (lock2) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                synchronized (lock1) {
                    doSomething();
                }
            }
        }).start();

    }

    private static void doSomething() {
        // 假装自己在做一些事
    }
}
