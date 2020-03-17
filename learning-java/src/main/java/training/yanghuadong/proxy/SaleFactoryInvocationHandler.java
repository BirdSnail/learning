package training.yanghuadong.proxy;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * {@link SaleFactory}的方法handler，所有{@link SaleFactory}的实例的方法调用会被转移到对{@code invoke()}方法的调用
 * 动态代理的思想：
 *  将新的真实对象作为参数，动态生成它的代理对象。
 *
 * @author BirdSnail
 * @date 2020/1/3
 */
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class SaleFactoryInvocationHandler implements InvocationHandler {

    private SaleFactory saleFactory;

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        method.invoke(saleFactory, args);
        System.out.println(method.getName());
        return "ok";
    }
}
