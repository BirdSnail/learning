package com.github.effective.java.training.generic;

import java.util.Arrays;
import java.util.EmptyStackException;

/**
 * 数组实现一个基本的Stack数据结构
 * @author BirdSnail
 * @date 2019/11/1
 */
public class Stack {

    private Object[] elements;
    private int size;
    private static final int DEFAULT_INITIAL_CAPACITY = 16;

    public Stack() {
        elements = new Object[DEFAULT_INITIAL_CAPACITY];
    }

    public void push(Object o) {
        ensureCapacity();
        elements[size++] = o;
    }

    public Object pop() {
        if (size == 0) throw new EmptyStackException();
        Object e = elements[--size];
        elements[size] = null; // 防止内存泄漏
        return e;
    }

    private void ensureCapacity() {
        if (size == elements.length) {
            elements = Arrays.copyOf(elements, 2 * size + 1);
        }
    }

    public boolean isEmpty() {
        return size == 0;
    }
}
