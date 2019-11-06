package com.github.effective.java.training.generic;

import java.util.ArrayList;
import java.util.List;

/**
 * @author BirdSnail
 * @date 2019/10/31
 */
public class GenericTest {

    public void safeAdd(List<Object> list, Object o) {
        list.add(o);
    }

    public void safeAdd1(List<String> list, String s) {
        list.add(s);
    }

    public void unsafeAdd(List list, Object o) {
        list.add(o);
    }

    public void untypeAdd(List<?> list) {
        list.add(null);
        //list.add(5); 编译出错
        Object o = list.get(0);
    }

    public static void main(String[] args) {
        List<Object> objectList = new ArrayList<>();
        List<String> stringList = new ArrayList<>();

        GenericTest test = new GenericTest();
        // test.safeAdd(stringList, ""); //编译出错
        // test.safeAdd1(objectList, "s"); //编译出错
        test.unsafeAdd(stringList, 5);
        test.unsafeAdd(objectList, "");
        System.out.println(stringList.get(0));

        Object[] objects = new Integer[2];
        objects[0] = "hello";
    }
}
