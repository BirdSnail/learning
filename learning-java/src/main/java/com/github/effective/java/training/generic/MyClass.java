package com.github.effective.java.training.generic;

import java.util.List;

/**
 * @author BirdSnail
 * @date 2019/12/9
 */
public class MyClass<X> {
    public <T> MyClass(T t) {
    }

    void printList(List<?> list) {
        for (Object o : list) {
            System.out.println(o);
        }
    }

    protected <T extends Number> void printList1(List<T> list) {
        for (T t : list) {
            System.out.println(t);
        }
    }

    public static void main(String[] args) {
        new MyClass<Integer>("hello");
    }
}
