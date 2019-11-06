package training.yanghuadong.singleton;

/**
 * 双重检验锁：
 *  1. 不再方法上上锁，降低了性能的损耗
 *  2. 要使用 volatile 关键字。因为 new Object()实际上会分三个步骤
 *      1. 再推内存上开辟空间
 *      2. 执行构造函数为变量赋值
 *      3. 将对象引用指向创建的对象（这步过后引用不为null）
 *     但是JVM内部优化过后2，3步骤不确定。假如第一个线程执行过程为 1->3->2，执行到3时第二线程调用了getInstance(),会返回一个引用不为null，
 *     但是没有初始化完成的对象。加上volatile可以解决上述问题。保证sinleton这个变量的写操作一定先于读操作完成。
 *
 * @author: Mr.Yang
 * @create: 2019-08-16
 */
public class SingleObjectOfDoubleCheck {
	private static volatile SingleObjectOfDoubleCheck singleton;

	private SingleObjectOfDoubleCheck() {}

	public static SingleObjectOfDoubleCheck getInstance() {
		if (singleton == null) {
			synchronized (SingleObjectOfDoubleCheck.class) {
				if (singleton == null) {
					singleton = new SingleObjectOfDoubleCheck();
				}
			}
		}
		return singleton;
	}
}
