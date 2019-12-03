package com.github.effective.java.training.thread;

import lombok.val;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executor;

/**
 * 倒计时的一个锁，在构造CountDownLatch的时候需要传入一个整数n，在这个整数“倒数”到0之前，主线程需要等待在门口，
 * 而这个“倒数”过程则是由各个执行线程驱动的，每个线程执行完一个任务“倒数”一次。
 * 总结来说，CountDownLatch的作用就是等待其他的线程都执行完任务，必要时可以对各个任务的执行结果进行汇总，然后主线程才继续往下执行。
 *
 * @author BirdSnail
 * @date 2019/11/15
 */
public class CountDownLatchTest {

    /**
     * 给一个任务同时并发的执行，并且返回最后一个线程完成的时间
     * @param executor 线程的执行器，并且这个线程池创建的线程数量不能少于 concurrent
     * @param concurrency 并发数量
     * @param action 执行的task
     */
    public static long time(Executor executor, int concurrency, Runnable action) throws InterruptedException {
        CountDownLatch ready = new CountDownLatch(concurrency);
        CountDownLatch start = new CountDownLatch(1);
        CountDownLatch done = new CountDownLatch(concurrency);

        for (int i = 0; i < concurrency; i++) {
            executor.execute(() -> {
                ready.countDown(); // 我准备好了
                try {
                    start.await(); // 开始跑
                    action.run(); // 正在跑
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }finally {
                    done.countDown(); // 我跑完了
                }
            });
        }

        ready.await(); // 让所有线程在同意起跑线上准备好
        long nanoTime = System.nanoTime();
        start.countDown(); // 所用线程同时开始执行
        done.await();
        return nanoTime;
    }
}
