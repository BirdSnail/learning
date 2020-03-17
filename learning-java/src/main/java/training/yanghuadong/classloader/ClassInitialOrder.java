package training.yanghuadong.classloader;

import java.util.ArrayList;
import java.util.List;

/**
 * @author BirdSnail
 * @date 2020/2/20
 */
public class ClassInitialOrder {

    static {
        int a = 0;
    }

    static String staticMethod() {
        list.add("ok");
        return "ok";
    }
    private static final List<String> list = new ArrayList<>();

}
