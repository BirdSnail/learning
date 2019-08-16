package yanghuadong.singleton;

/**
 * 静态内部类：
 *  1. 延迟加载，调用getInstance()之前可以读取文件，修改参数来改变类变量。
 *  2. 没有性能损耗
 *
 * @author: Mr.Yang
 * @create: 2019-08-16
 */
public class SingleObjectOfInternalClass {

	private static class SingletonHonlder {
		private static final SingleObjectOfInternalClass INSTANCE = new SingleObjectOfInternalClass();
	}

	private SingleObjectOfInternalClass() {}

	public static final SingleObjectOfInternalClass getInstance() {
		return SingletonHonlder.INSTANCE;
	}
}
