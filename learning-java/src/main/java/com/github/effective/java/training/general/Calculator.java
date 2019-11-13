package com.github.effective.java.training.general;

import java.math.BigDecimal;

/**
 * {@link BigDecimal}使用方式
 *
 * @author BirdSnail
 * @date 2019/11/11
 */
public class Calculator {

    public static void main(String[] args) {
        System.out.println(1.04 - 0.43);
        buy();
        buy2();
    }

    public static void buy() {
        double funds = 1.00;
        int count = 0;
        for (double price = 0.10; price <= funds; price += 0.10) {
            funds -= price;
            count++;
        }

        System.out.println("Change: " + funds);
        System.out.println("Count: " + count);
    }

    public static void buy2() {
        BigDecimal funds = new BigDecimal("1.00");
        final BigDecimal TEN_CENTS = new BigDecimal(".10");
        int count = 0;
        for (BigDecimal price = TEN_CENTS; funds.compareTo(price) >= 0; price = price.add(TEN_CENTS)) {
            funds = funds.subtract(price);
            count++;
        }

        System.out.println("Change: " + funds);
        System.out.println("Count: " + count);
    }
}
