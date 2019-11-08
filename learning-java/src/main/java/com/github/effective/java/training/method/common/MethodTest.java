package com.github.effective.java.training.method.common;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author BirdSnail
 * @date 2019/11/7
 */
public class MethodTest {

    public static void main(String[] args) {
        List<Integer> integerList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            integerList.add(i);
        }

        List<Integer> subList = integerList.subList(0, 5);
        subList.set(0, 10);

        integerList.forEach(System.out::println);

        // 重载测试
        new Thread(System.out::println).start();

        ExecutorService exec = Executors.newCachedThreadPool();
        //exec.submit(System.out::println); // 无法通过编译

        System.out.println(min(6, 1, 3, 5));
    }

    /**
     * 可变参数的使用
     * @param args 可变参数
     * @return min—value
     */
    private static int min(int... args) {
        int min = args[0];
        for (int arg : args) {
            if (arg < min) {
                min = arg;
            }
        }
        return min;
    }

}
