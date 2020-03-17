package training.yanghuadong.annotation;

/**
 * @author BirdSnail
 * @date 2020/3/16
 */
public class DoSomething {

    @MyRunTest
    public void addition() {
        System.out.println(1 + 1);
    }

    @MyRunTest
    public void subtraction() {
        System.out.println(5 - 1);
    }

    @MyRunTest
    public void multiplication() {
        System.out.println(3 * 5);
    }

    @MyRunTest
    public void division() {
        System.out.println(5 / 0);
    }
}
