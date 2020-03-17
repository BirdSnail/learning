package training.yanghuadong.proxy;

/**
 * @author BirdSnail
 * @date 2020/1/3
 */
public class FoodFactory  implements SaleFactory{
    @Override
    public void another() {
        System.out.println("hahaha");
    }

    @Override
    public void saleSomething(String name) {
        System.out.println("sale " + name + " food, thank you!");
    }
}
