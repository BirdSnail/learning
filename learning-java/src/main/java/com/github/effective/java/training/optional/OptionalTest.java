package com.github.effective.java.training.optional;

import lombok.ToString;

import java.util.Optional;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author BirdSnail
 * @date 2019/11/18
 */
public class OptionalTest {

    private static  User per = new User();

    public static void main(String[] args) {
        per.setName("timo");
        Optional<User> user = Optional.ofNullable(per);
        System.out.println(user.map(user1 -> user1.setName("nihao"))
                // 这个里面的如果的方法一定为执行
                // 返回结果根据Optional里面是否null判断
                .orElse(new User().setName("hello")));
    }

    @ToString
    static class User{
        String name;
        int age;

        public String getName() {
            return name;
        }

        User setName(String name) {
            System.out.println("我被执行了");
            this.name = name;
            return this;
        }
    }
}
