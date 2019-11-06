package com.github.effective.java.training.generic;

import java.util.Arrays;
import java.util.Collection;
import java.util.EmptyStackException;
import java.util.List;

/**
 * 使用泛型不使用Object实现Stack
 * @author BirdSnail
 * @date 2019/11/1
 */
public class StackWithGeneric<T> {
    private T[] elements;
    private int size;
    private static final int DEFAULT_INITIAL_CAPACITY = 16;

    // 类型参数一定使T类型，可以取保强制类型转换可以成功
    @SuppressWarnings("unchecked")
    public StackWithGeneric() {
        elements = (T[]) new Object[DEFAULT_INITIAL_CAPACITY];
    }

    public void push(T o) {
        ensureCapacity();
        elements[size++] = o;
    }

    public T pop() {
        if (size == 0) throw new EmptyStackException();
        T e = elements[--size];
        elements[size] = null; // 防止内存泄漏
        return e;
    }

    /**
     * 将一个{@code Collection} 添加进来
     * 改进：
     *     增加灵活性，是E的子类也可以被添加进来，这样做确实是有道理的
     *     producer extends
     * @param src
     */
    public void pushAll(Collection<? extends T> src) {
        for (T t : src) {
            push(t);
        }
    }

    /**
     * 将所有元素弹出，放入到指定的{@code Collection}
     * 改进：
     *      增加灵活性，弹出的元素可以放入E的父类的集合中，这样做也是有道理的
     *     producer extends
     * @param dsc
     */
    public void popAll(Collection<? super T> dsc) {
        while (!isEmpty()) {
            dsc.add(pop());
        }
    }

    private void ensureCapacity() {
        if (size == elements.length) {
            elements = Arrays.copyOf(elements, 2 * size + 1);
        }
    }

    public boolean isEmpty() {
        return size == 0;
    }

    // =================Static====================
    public static <T extends Comparable<? super T>> T max(List<? extends T> list) {
        if (list.isEmpty()) {
            throw new IllegalArgumentException("Empty collection");
        }
        T result = null;
        for (T t : list) {
            if (result == null || t.compareTo(result) > 0) {
                result = t;
            }
        }
        return result;
    }
}
