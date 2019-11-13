package com.github.effective.java.training.thread;

import java.util.HashMap;
import java.util.Map;

/**
 * @author BirdSnail
 * @date 2019/11/11
 */
public class ThreadLocalTest {

    private static MyThreadLocal myThreadLocal = new MyThreadLocal();
    // Java自己实现的的ThreadLocal
    private static ThreadLocal<String> threadLocal = new ThreadLocal<>();

    public static void main(String[] args) {
        doSomething("user01");

        new Thread(() -> doSomething("user02"))
                .start();
    }

    private static void doSomething(String value) {
        myThreadLocal.set();
        threadLocal.set(value);
        System.out.println(myThreadLocal.get());
        System.out.println(threadLocal.get());
    }


    /**
     * 自己实现的一个简单ThreadLocal，用于一个线程在方法栈中的变量共享
     */
    public static class MyThreadLocal {

        private final Map<Long, String> THREAD_LOCAL = new HashMap<>();

        public void set() {
            THREAD_LOCAL.put(Thread.currentThread().getId(), Thread.currentThread().getName());
        }

        public String get() {
            return THREAD_LOCAL.get(Thread.currentThread().getId());
        }
    }


}
