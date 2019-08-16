package yanghuadong.singleton;

/**
 * 懒汉模式
 *  1. 延迟创建对象
 *  2. 存在性能的缺陷
 * @author: Mr.Yang
 * @create: 2019-08-16
 */
public class SingleObjectOfLazy{
	private static SingleObjectOfLazy singleton;

	private SingleObjectOfLazy() {
		
	}

	public synchronized static SingleObjectOfLazy getInstance() {
		if (singleton == null) {
			singleton = new SingleObjectOfLazy();
		}
		return singleton;
	}
}
