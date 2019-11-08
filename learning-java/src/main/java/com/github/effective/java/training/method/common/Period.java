package com.github.effective.java.training.method.common;

import java.util.Date;

/**
 * @author BirdSnail
 * @date 2019/11/7
 */
public class Period {

    private final Date start;
    private final Date end;

    public Period(Date start, Date end) {
        if (start.compareTo(end) > 0) {
            throw new IllegalArgumentException();
        }
        this.start = start;
        this.end = end;
    }

    public Date start() {
        return start;
    }

    public Date end() {
        return end;
    }

    public static void main(String[] args) {
        Date start = new Date();
        Date end = new Date();
        Period p = new Period(start, end);
        end.setYear(55); // 导致了start 大于了 end
        System.out.println(p.start().compareTo(p.end()));
    }
}
