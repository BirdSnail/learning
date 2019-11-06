package training.yanghuadong.concurrent.synchronize;

import java.util.concurrent.TimeUnit;

/**
 * 1.普通同步方法synchronized使用的是当前的对象锁（this.lock），一个对象只有一把锁。如果一个类有多个synchronized方法，
 *   当一个线程进入其中一个同步方法时，其他线程无法进入其他的synchronized方法。
 *
 * 2.静态 synchronized 方法使用的Class锁，某一个时刻只能有一个线程进入静态synchronized方法。
 *
 * @author: Mr.Yang
 * @create: 2019-09-10
 */
public class TestSynchronized {

	public static void main(String[] args) {
		//synchronizedTest();

		Pad pad = new Pad();

		new Thread(() -> {
			try {
				pad.getIos();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}).start();

		new Thread(() -> //phone.getAndroid()
				pad.getAndroid()).start();


	}

	private static void synchronizedTest() {
		Phone phone = new Phone();
		Phone phone2 = new Phone();

		new Thread(() -> {
			try {
				phone.getIos();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}).start();

		new Thread(() -> //phone.getAndroid()
				phone2.getAndroid()).start();
	}
}

/**资源类*/
class Phone{

	public synchronized void getIos() throws InterruptedException {
		TimeUnit.SECONDS.sleep(3);
		System.out.println("getIos");
	}

	public synchronized void getAndroid() {
		System.out.println("getAndroid");
	}
}

class Pad{

	public static synchronized void getIos() throws InterruptedException {
		TimeUnit.SECONDS.sleep(3);
		System.out.println("getIos");
	}

	public static synchronized void getAndroid() {
		System.out.println("getAndroid");
	}
}