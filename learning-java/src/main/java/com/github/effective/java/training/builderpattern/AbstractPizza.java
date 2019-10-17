package com.github.effective.java.training.builderpattern;

import java.util.EnumSet;
import java.util.Set;

/**
 * 抽象类的Builder模式
 * @author BirdSnail
 * @date 2019/10/17
 */
public abstract class AbstractPizza {
    public enum Topping {HAM, MUSHROOM, ONION, PEPPER, SAUSAGE}
    final Set<Topping> toppings;

    abstract static class Builder<T extends Builder<T>>{
        EnumSet<Topping> toppings = EnumSet.noneOf(Topping.class);

        public T addTopping(Topping topping) {
            toppings.add(topping);
            return self();
        }

        protected abstract T self();

        abstract AbstractPizza build();
    }

    public AbstractPizza(Builder builder) {
        toppings = builder.toppings.clone();
    }
}
