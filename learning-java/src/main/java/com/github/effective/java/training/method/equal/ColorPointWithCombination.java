package com.github.effective.java.training.method.equal;

/**
 * @author BirdSnail
 * @date 2019/10/23
 */
public class ColorPointWithCombination {
    private Point point;
    private String color;

    public ColorPointWithCombination(int x, int y, String color) {
        point = new Point(x, y);
        this.color = color;
    }

    public String getColor() {
        return color;
    }

    /**
     * 使用组合解决在继承上面每个子类重写equals方法带来的不稳定性问题：
     *  不能在继承一个可实例化类时即遵守equals约定又添加一个值组件
     */
    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof ColorPointWithCombination)) {
            return false;
        }
        ColorPointWithCombination cp = (ColorPointWithCombination) obj;
        return cp.point.equals(point) && cp.getColor().equals(color);
    }
}
