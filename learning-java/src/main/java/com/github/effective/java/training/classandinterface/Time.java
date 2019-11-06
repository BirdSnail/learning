package com.github.effective.java.training.classandinterface;

/**
 * @author BirdSnail
 * @date 2019/10/25
 */
public class Time {

    // 虽然使用public暴露了类的字段，但是这个字段定义为不可变的，产生的危害较小
    public final int hour;
    public final int min;

    public Time(int hour, int min) {
        this.hour = hour;
        this.min = min;
    }

}
