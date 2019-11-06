package com.github.effective.java.training.generic;

import java.util.Arrays;

/**
 * 使用泛型的另一种写法，每次取出数据是才进行类性转换
 * @author BirdSnail
 * @date 2019/11/1
 */
public class StackWithGeneric1<T> {

    private Object[] elements;
    private int size;
    private static final int DEFAULT_INITIAL_CAPACITY = 16;

    public StackWithGeneric1() {
        elements = new Object[DEFAULT_INITIAL_CAPACITY];
    }

    public void push(T o) {
        ensureCapacity();
        elements[size++] = o;
    }

    public T pop() {
        if (size == 0) {
            return null;
        }
        @SuppressWarnings("unchecked")
        T e = (T) elements[--size];
        elements[size] = null; // 防止内存泄漏
        return e;
    }

    private void ensureCapacity() {
        if (size == elements.length) {
            elements = Arrays.copyOf(elements, 2 * size + 1);
        }
    }
}
