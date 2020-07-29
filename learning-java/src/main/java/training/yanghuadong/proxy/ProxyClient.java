package training.yanghuadong.proxy;

import java.lang.reflect.Proxy;
import java.util.Map;

/**
 * @author BirdSnail
 * @date 2020/1/3
 */
public class ProxyClient {

	public static void main(String[] args) {
		// 快速为顶层接口创建一个代理类
		// 直接在内存中生成代理类的字节码
		Map proxyInstance = (Map) Proxy.newProxyInstance(
				DynamicProxyTest.class.getClassLoader(),
				new Class[]{Map.class},
				new DynamicProxyTest());

		System.out.println(proxyInstance.put("hello", "value"));

		SaleFactoryInvocationHandler invocationHandler = new SaleFactoryInvocationHandler(new FoodFactory());
		SaleFactory proxySale = (SaleFactory) Proxy.newProxyInstance(
				ProxyClient.class.getClassLoader(),
				new Class[]{SaleFactory.class},
				invocationHandler
		);

		// 代理类所有的方法调用会被转移到统一的invoke方法
		proxySale.saleSomething("blak");
		proxySale.another();

		invocationHandler.setSaleFactory(new PhoneFactory());
		proxySale.saleSomething("iphone");
		proxySale.another();
	}

}
