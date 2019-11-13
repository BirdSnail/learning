package com.github.effective.java.training.general;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.Set;

/**
 * @author BirdSnail
 * @date 2019/11/12
 */
public class ReflectionTest {

    public static void main(String[] args) {
        // 将一个class name转换成一个Class对象
        Class<? extends Set<String>> cl = null;
        try {
            cl = (Class<? extends Set<String>>) Class.forName(args[0]);
        } catch (ClassNotFoundException e) {
            printError("Class not found");
        }

        // 获取构造函数
        Constructor<? extends Set<String>> cons = null;
        try {
            cons = cl.getDeclaredConstructor();
        } catch (NoSuchMethodException e) {
           printError("No parameter constructor");
        }

        Set<String> s = null;
        try {
            s = cons.newInstance();
        } catch (InstantiationException e) {
            printError("Class not instantiation");
        } catch (IllegalAccessException e) {
            printError("Constructor not accessible");
        } catch (InvocationTargetException e) {
            printError("Class don't implement set");
        }

        s.addAll(Arrays.asList(args).subList(1, args.length));
        System.out.println(s);
    }

    private static void printError(String msg) {
        System.out.println(msg);
        System.exit(1);
    }
}
