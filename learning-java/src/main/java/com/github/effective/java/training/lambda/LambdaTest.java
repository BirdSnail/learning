package com.github.effective.java.training.lambda;

import java.util.HashMap;
import java.util.Map;

/**
 * @author BirdSnail
 * @date 2019/11/7
 */
public class LambdaTest {

    public static void main(String[] args) {
        Map<String, Integer> map = new HashMap<>();
        //map.put("", 0);
        map.merge("key", 1, Integer::sum);
        System.out.println(map.get("key"));
        map.merge("key", 2, Integer::sum);
        System.out.println(map.get("key"));
    }
}
