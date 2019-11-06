package com.github.effective.java.training.generic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author BirdSnail
 * @date 2019/11/4
 */
public class Favorites {

    private Map<Class<?>, Object> favorites = new HashMap<>();

    public <T> void putFavorites(Class<T> type, T instance) {
        favorites.put(type, type.cast(instance));
    }

    public <T> T getFavorites(Class<T> type) {
        return type.cast(favorites.get(type));
    }

    public static void main(String[] args) {
        Favorites f = new Favorites();
        f.putFavorites(String.class, "string class");
        f.putFavorites(Integer.class, 11);
        //f.putFavorites(Class.class, Favorites.class);

        List<String> stringList = new ArrayList<>();
        List<Integer> integerList = new ArrayList<>();
        f.putFavorites(List.class, stringList);
        f.putFavorites(List.class, integerList);
    }

}
