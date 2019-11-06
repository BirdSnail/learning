package com.github.effective.java.training.method.equal;

/**
 * @author BirdSnail
 * @date 2019/10/23
 */
public class ColorPoint extends Point {

    private String color;

    public ColorPoint(int x, int y, String color) {
        super(x, y);
        this.color = color;
    }

    public String getColor() {
        return color;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Point)) {
            return false;
        }

        if (!(obj instanceof ColorPoint)) {
            return obj.equals(this);
        }

        return super.equals(obj) && ((ColorPoint) obj).getColor().equals(color);
    }
}
