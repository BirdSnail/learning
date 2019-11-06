package com.github.effective.java.training.method.equal;

/**
 * @author BirdSnail
 * @date 2019/10/23
 */
public class Test {

    public static void main(String[] args) {
        Point p1 = new Point(1, 2);
        ColorPoint p2 = new ColorPoint(1, 2, "red");
        ColorPoint p3 = new ColorPoint(1, 2, "yellow");
        // 对称性
        System.out.println(p1.equals(p2));
        System.out.println(p2.equals(p1));

        // 传递性
        System.out.println(p2.equals(p1));
        System.out.println(p1.equals(p3));
        System.out.println(p2.equals(p3));

    }
}
