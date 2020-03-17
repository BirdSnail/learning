package training.yanghuadong.proxy;

/**
 * 动态代理顶层抽象接口
 *
 * 商店
 *
 * @author BirdSnail
 */
public interface SaleFactory {

    void another();

    /**
     * 出售东西
     * @param name 货物名称
     */
    void saleSomething(String name);

}
