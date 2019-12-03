package com.github.effective.java.training.thread;

import java.util.concurrent.TimeUnit;

/**
 * 读和写操作都要保持同步
 *
 * @author BirdSnail
 * @date 2019/11/14
 */
public class StopThread {

    private static boolean stopThread = false;

    public static void main(String[] args) throws InterruptedException {
        // 这个线程不会结束，因为没有同步写操作，下面代码会被vm进行优化
        Thread backgroundThread = new Thread(() -> {
            int i = 0;
            while (!requestStop()) {
                i++;
            }
            // 被优化后的代码
//            if (!stopThread) {
//                while (true){
//                    i++;
//                }
//            }

        });
        backgroundThread.start();
        TimeUnit.SECONDS.sleep(1);
        setStopThread();
    }

    private static synchronized void setStopThread() {
        stopThread = true;
    }

    private static synchronized boolean requestStop() {
        return stopThread;
    }

}
