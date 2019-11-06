package com.github.effective.java.training.generic;

import com.github.effective.java.training.enu.Operation;

import java.util.ArrayList;
import java.util.List;
import java.util.function.UnaryOperator;

/**
 * @author BirdSnail
 * @date 2019/11/1
 */
public class GenericMain {

    public static void main(String[] args) {
        StackWithGeneric<String> stack = new StackWithGeneric<>();
        for (String arg : args) {
            stack.push(arg);
        }

        while (!stack.isEmpty()) {
            System.out.println(stack.pop().toUpperCase());
        }

        // 子类型添加到使用限制通配符的泛型方法中
        StackWithGeneric<Object> objectStack = new StackWithGeneric<>();
        List<String> strings = new ArrayList<>();
        objectStack.pushAll(strings);

        // 从使用限制通配符的泛型方法中取出元素
        List<Object> objectList = new ArrayList<>();
        stack.popAll(objectList);

        UnaryOperator<String> sameString = UnaryOperator.identity();
        UnaryOperator<Integer> sameNumber = UnaryOperator.identity();

        //swap(strings,0 , 1);

        double x = 2.0;
        double y = 1.0;
        for (Operation op : Operation.values()) {
            System.out.printf("%f %s %f = %f%n", x, op, y, op.apply(x, y));
        }
    }

    /**
     * 即导出了 简洁的公共API,又是类型安全的
     */
    public static void swap(List<?> list, int i, int j) {
        swapHelper(list, i, j);
    }

    private static <E> void swapHelper(List<E> list, int i, int j) {
        list.set(i, list.set(j, list.get(i)));
    }
}
