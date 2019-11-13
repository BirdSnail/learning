package com.github.effective.java.training.general;

import java.util.Comparator;

/**
 * @author BirdSnail
 * @date 2019/11/11
 */
public class BoxTypeTest {

    private static final Comparator<Integer> naturalOrder = (i, j) -> (i < j) ? -1 : (i == j ? 0 : 1);

    public static void main(String[] args) {
        System.out.println(naturalOrder.compare(new Integer(49), new Integer(49))); // 包装类型使用了 == 比较导致结果为1
        boxing();
    }

    /**进行了反复的自动装箱拆箱，导致很慢*/
    private static void boxing() {
        Long sum = 0L;
        for (long i = 0; i < Integer.MAX_VALUE; i++) {
            sum += i;
        }

        System.out.println(sum);
    }
}
