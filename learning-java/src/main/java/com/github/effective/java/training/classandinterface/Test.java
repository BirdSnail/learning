package com.github.effective.java.training.classandinterface;

import java.util.*;

/**
 * @author BirdSnail
 * @date 2019/10/25
 */
public class Test {

    public static void main(String[] args) {
//        Time time = new Time(12, 10);
        InstrumentedSet<String> mySet = new InstrumentedSet(new HashSet());
        mySet.addAll(Arrays.asList("a", "b", "c"));
        System.out.println(mySet.getAddCount());

        List<Integer> ints = new ArrayList<>();
        Chooser<Object> chooser = new Chooser(ints);
    }

}
