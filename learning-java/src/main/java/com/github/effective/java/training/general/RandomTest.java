package com.github.effective.java.training.general;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

/**
 * 需要随机数时请使用标准类库
 *  1. Random.nextInt()
 *  2. ThreadLocalRandom.  --Java7 之后使用这个，更快，生成的随机数质量更高
 *
 * @author BirdSnail
 * @date 2019/11/11
 */
public class RandomTest {

    public static void main(String[] args) {
        for (int i = 0; i < 5; i++) {
            System.out.println(ThreadLocalRandom.current().nextInt(10));
        }

        for (int i = 0; i < 5; i++) {
            System.out.println(new Random().nextInt(10));
        }
    }
}
