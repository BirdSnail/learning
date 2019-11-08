package com.github.effective.java.training.method.common;

import java.util.Date;

/**
 * @author BirdSnail
 * @date 2019/11/7
 */
public class PeriodWithSafe {
    private final Date start;
    private final Date end;

    /**
     * 进行了防御性的拷贝，外界修改了参数值也不会对构造方法的{@code Date}产生影响
     * @param start 开始时间
     * @param end 结束时间
     */
    public PeriodWithSafe(Date start, Date end) {
        this.start = new Date(start.getTime());
        this.end = new Date(end.getTime());
        if (this.start.compareTo(this.end) > 0) {
            throw new IllegalArgumentException();
        }
    }

    public Date start() {
        return new Date(start.getTime());
    }

    /**
     * 对于 <code>p.end().setTime(78) </code> 这种攻击也可以被防御下来
     *
     * @return date
     */
    public Date end() {
        return new Date(end.getTime());
    }

    public static void main(String[] args) throws InterruptedException {
        Date start = new Date();
        Thread.sleep(2000L);
        Date end = new Date();
        System.out.println(start.getTime()  + ":" + end.getTime());

        PeriodWithSafe p = new PeriodWithSafe(start, end);
        end.setYear(55); // 第一次攻击被防御了下来
        System.out.println(p.start().compareTo(p.end()));
        p.end().setTime(78); // 第二次攻击
        System.out.println(p.start().compareTo(p.end()));

    }
}
