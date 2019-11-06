package com.github.effective.java.training.builderpattern;

/**
 * Builder模式
 * 使用场景：
 *  有很多构造函数参数，其中一些时必选的另一些为可选的时候
 *
 * 使用方法:
 *  1. 使用Build类来处理参数
 *  2. 要生成的类从Build中获取参数，而不是同public 构造函数直接赋值
 *
 * @author BirdSnail
 * @date 2019/10/17
 */
public class NutritionFactsWhitBuilderPattern {
    private final int servingSize;
    private final int servings;
    private final int calories;
    private final int fat;
    private final int sodium;
    private final int carbohydrate;


    public static class Builder {
        // required parameters
        private final int servingSize;
        private final int servings;

        // optional parameters
        private  int calories = 0;
        private  int fat = 0;
        private  int sodium = 0;
        private  int carbohydrate = 0;

        public Builder(int servingSize, int servings) {
            this.servingSize = servingSize;
            this.servings = servings;
        }

        /**
         * 返回对象本身，可以链式调用
         */
        public Builder setCalories(int calories) {
            this.calories = calories;
            return this;
        }

        public Builder setFat(int fat) {
            this.fat = fat;
            return this;
        }

        public Builder setSodium(int sodium) {
            this.sodium = sodium;
            return this;
        }

        public Builder setCarbohydrate(int carbohydrate) {
            this.carbohydrate = carbohydrate;
            return this;
        }

        /**
         *需要提供一个build方法返回要创建的对象
         */
        public NutritionFactsWhitBuilderPattern build() {
            return new NutritionFactsWhitBuilderPattern(this);
        }

    }

    private NutritionFactsWhitBuilderPattern(Builder builder) {
        servings = builder.servings;
        servingSize = builder.servingSize;
        calories = builder.calories;
        fat = builder.fat;
        sodium = builder.sodium;
        carbohydrate = builder.carbohydrate;
    }

    public static void main(String[] args) {
        NutritionFactsWhitBuilderPattern fact = new Builder(20, 30)
                .setCalories(1)
                .setFat(2)
                .setCarbohydrate(3)
                .setSodium(4)
                .build();
    }

}
