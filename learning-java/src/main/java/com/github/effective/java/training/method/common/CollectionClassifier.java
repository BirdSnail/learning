package com.github.effective.java.training.method.common;

import java.util.*;

/**
 * @author BirdSnail
 * @date 2019/11/8
 */
public class CollectionClassifier {

    public static String classify(Set<?> set) {
        return "set";
    }

    public static String classify(List<?> list) {
        return "list";
    }

    private static String classify(Collection<?> collection) {
        return "Unknown Collection";
    }

    public static void main(String[] args) {
        Collection<?>[] colls = {new HashSet<String>(),
                new ArrayList<String>(),
                new HashMap<String, String>().values()
        };

        // 不会打印 set， list， unknown collection
        // 问题在于循环的迭代时参数类型相同的 Collection<?>
        for (Collection<?> coll : colls) {
            System.out.println(classify(coll));
        }
    }
}
