package com.github.effective.java.training.generic;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.function.Predicate;

/**
 * @author BirdSnail
 * @date 2019/12/9
 */
public class RulePredicate {

    static <T> void verify(Collection<T> c, Predicate<T> p) {
        int n = 0;
        for (T t : c) {
            if (p.test(t)) {
                n++;
            }
        }
        System.out.println(n);
    }

    public static void main(String[] args) {
        verify(Arrays.asList(1, 2), RulePredicate::isOdd);
        verify(Arrays.asList(1, 2), new OddCount());
    }

    private static class OddCount implements Predicate<Integer> {

        @Override
        public boolean test(Integer integer) {
            return integer % 2 != 0;
        }
    }

    static boolean isOdd(int n) {
        return n % 2 != 0;
    }

    /**这种泛型写法是为保持与之前的方法的兼容性，当类型擦除时，T被设置为Object*/
    public static <T extends Object & Comparable<? super T>> T max(List<? extends T> list, int begin, int end) {

        T maxElem = list.get(begin);

        for (++begin; begin < end; ++begin) {
            if (maxElem.compareTo(list.get(begin)) < 0) {
                maxElem = list.get(begin);
            }
        }
        return maxElem;
    }
}
