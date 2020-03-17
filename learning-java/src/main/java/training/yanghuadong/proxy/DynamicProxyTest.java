package training.yanghuadong.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * 用于快速开始入门的{@link java.util.Map}代理类
 * @author BirdSnail
 * @date 2020/1/3
 */
public class DynamicProxyTest implements InvocationHandler {

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        System.out.println(method.getName());
        return 42;
    }
}
