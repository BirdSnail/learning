package com.github.effective.java.training.method.common;

import java.util.Arrays;
import java.util.Collection;
import java.util.Optional;
import java.util.OptionalInt;

/**
 * @author BirdSnail
 * @date 2019/11/8
 */
public class OptionalTest {

    public static void main(String[] args) {
        System.out.println(max(Arrays.asList(4, 5, 8, 6)));

        OptionalInt opInt = OptionalInt.of(2);
    }

    /**
     * 使用{@link Optional} 代替 null返回值
     */
    private static <E extends Comparable<E>> Optional<E> max(Collection<E> c) {
        if (c.isEmpty()) {
            return Optional.empty();
        }

        E result = null;
        for (E e : c) {
            if (result == null || e.compareTo(result) > 0) {
                result = e;
            }
        }

        return Optional.of(result);
    }
}
