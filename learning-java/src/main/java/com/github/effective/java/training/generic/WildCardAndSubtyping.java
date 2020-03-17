package com.github.effective.java.training.generic;

import java.util.ArrayList;
import java.util.List;

/**
 * @author BirdSnail
 * @date 2019/12/9
 */
public class WildCardAndSubtyping {

    private static class A {

    }

    private static class B extends A {

    }

    public static void main(String[] args) {
        List<B> bList = new ArrayList<>();
//        List<A> aList = bList; // 不能编译，
        List<? extends A> aList = bList;
    }
}
