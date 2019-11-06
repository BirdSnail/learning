package com.github.effective.java.training.classandinterface;

import java.time.Instant;

/**
 * @author BirdSnail
 * @date 2019/10/29
 */
public class Sub extends Super {

    private final Instant ins;

    Sub() {
        this.ins = Instant.now();
    }

    @Override
    public void overrideMe() {
        System.out.println(ins);;
    }

    public static void main(String[] args) {
        Sub sub = new Sub();
        sub.overrideMe();
    }
}
