package com.github.effective.java.training.thread;

import java.util.concurrent.TimeUnit;

/**
 * volatile关键字可以保证可见性：
 *  写入volatile变量会直接写入主内存
 *  读取volatile变量会直接从主内存读取
 *
 * @author BirdSnail
 * @date 2019/11/14
 */
public class ThreadOfVolatile {

    private static volatile boolean stopThread = false;

    public static void main(String[] args) throws InterruptedException {
        Thread backgroundThread = new Thread(() -> {
            int i = 0;
            while (!stopThread) {
                i++;
            }
        });
        backgroundThread.start();
        TimeUnit.SECONDS.sleep(1);
        stopThread = true;
    }

}
