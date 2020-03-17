package com.github.effective.java.training.generic;

import java.io.Closeable;
import java.util.Collections;
import java.util.List;

/**
 * @author BirdSnail
 * @date 2019/12/9
 */
public class GenericExercises {

    /**
     * 使用泛型统计一个集合中指定属性的元素的个数，如奇整数，素数，回文数
     *
     * @param numbers list of number
     */
    private static void countOfNumber(List<Integer> numbers) {
        int oddCount = 0;
        int primeCount = 0;
        int palindromes = 0;

        for (Integer n : numbers) {
            if (isOdd()) {
                oddCount++;
            }
            if (isPrime()) {
                primeCount++;
            }

            if (isPalindromes()) {
                primeCount++;
            }
        }
        System.out.println(oddCount + primeCount + palindromes);
    }

    private static boolean isPrime() {
        return false;
    }

    private static boolean isOdd() {
        return false;
    }

    private static boolean isPalindromes() {
        return false;
    }

    static <T> void swapInArray(T[] arr, int first, int second) {
        T temp = arr[first];
        arr[first] = arr[second];
        arr[second] = temp;
    }

    static <T extends Comparable<T>> int findFirstGreaterThan(T[] at, T elem) {
        return 0;
    }

    static void print(List<? extends Number> lists) {
        for (Number num : lists) {
            System.out.println(num + "");
        }
        System.out.println();
    }

    static <T extends Comparable<T>> T findMaximal(List<T> list) {
        return Collections.max(list);
    }

    static class Node<T> implements Comparable<T> {
        @Override
        public int compareTo(T obj) {
            // ...
            return 0;
        }
    }

    public static void main(String[] args) {
        Node<String> node = new Node<>();
        Comparable<String> comparable = node;
    }
}
