package com.github.effective.java.training.builderpattern;

import java.util.Objects;

/**
 * @author BirdSnail
 * @date 2019/10/17
 */
public class MyPizza extends AbstractPizza {
    public enum Size {SMALL, MEDIUM, LARGE};
    private final Size size;

    public static class Builder extends AbstractPizza.Builder<Builder> {
        private final Size size;

        public Builder(Size size) {
            this.size = Objects.requireNonNull(size);
        }

        @Override
        protected Builder self() {
            return this;
        }

        @Override
        public MyPizza build() {
            return new MyPizza(this);
        }
    }

    private MyPizza(Builder builder) {
        super(builder);
        size = builder.size;
    }

    public static void main(String[] args) {
        MyPizza myPizza = new Builder(Size.MEDIUM)
                .addTopping(Topping.HAM)
                .addTopping(Topping.ONION)
                .build();
    }
}
