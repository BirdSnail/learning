package yanghuadong.singleton;

/**
 * 饿汉模式：
 *  1. 不存在性能的缺陷
 *  2. 没有延迟创建对象。该模式在类加载时就会创建对象，当对象需要先读取配置文件或参数进行赋值时该模式不可用
 * @author: Mr.Yang
 * @create: 2019-08-16
 */
public class SingleObjectOfHunger {
	private static final SingleObjectOfHunger singleton = new SingleObjectOfHunger();

	private SingleObjectOfHunger() {

	}

	public static SingleObjectOfHunger getInstance() {
		return singleton;
	}
}
