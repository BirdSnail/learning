package training.yanghuadong.proxy;

/**
 * @author BirdSnail
 * @date 2020/1/3
 */
public class PhoneFactory implements SaleFactory{
    @Override
    public void another() {
        System.out.println("hihihi");
    }

    @Override
    public void saleSomething(String name) {
        System.out.println("My phone " + name + " is very good!");
    }
}
