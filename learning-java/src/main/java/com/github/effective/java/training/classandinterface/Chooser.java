package com.github.effective.java.training.classandinterface;

import java.util.Collection;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

/**
 * @author BirdSnail
 * @date 2019/10/31
 */
public class Chooser<T> {

    private T[] choiceArray;

    public Chooser(Collection<? extends T> collection) {
        this.choiceArray = (T[]) collection.toArray();
    }

    public T choose() {
        Random random = ThreadLocalRandom.current();
        return choiceArray[random.nextInt(choiceArray.length)];
    }

}
