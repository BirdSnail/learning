package window;

import java.util.Random;

/**
 * @author: Mr.Yang
 * @create: 2019-06-20
 */
public class Test {

    public static void main(String[] args) {

//        char cr = (char)('A' + new Random().nextInt(5));
//        System.out.println(cr);

        System.out.println(getRandomChar());


    }

    private static String getRandomChar() {
        return String.valueOf((char) ('a' + new Random().nextInt(5)));
    }
}
