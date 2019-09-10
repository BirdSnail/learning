package training.yanghuadong.concurrent;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 使用{@link Lock}替换synchronized，完成锁的功能
 * 使用{@link Condition} 替换wait，notify，完成线程等待唤醒功能
 *
 * @author: Mr.Yang
 * @create: 2019-09-05
 */
public class ProducerAndConsumerWithLock {

	public static class Bread {
		private int number = 0;

		private Lock lock = new ReentrantLock();
		private Condition create = lock.newCondition();
		private Condition eat = lock.newCondition();

		public void increment() throws InterruptedException {
			lock.lock();
			try {
				while (number != 0) {
					create.await();
				}
				++number;
				System.out.println(Thread.currentThread().getName() + ":生产了一个面包，还剩" + number + "个面包");
				eat.signal();
			}finally {
				lock.unlock();
			}
		}

		public void descrement() throws InterruptedException {
			lock.lock();
			try {
				while (number == 0) {
					eat.await();
				}
				--number;
				System.out.println(Thread.currentThread().getName() + ":吃掉了一个面包，还剩" + number + "个面包");
				create.signal();
			}finally {
				lock.unlock();
			}
		}
	}

	public static void main(String[] args) {
		Bread bread = new Bread();

		new Thread(new PorducerWorker(bread)).start();
		new Thread(new PorducerWorker(bread)).start();

		new Thread(new ConsumerWorker(bread)).start();
		new Thread(new ConsumerWorker(bread)).start();
	}


	//================================
	//  WORK CLASS
	//================================

	public static class PorducerWorker implements Runnable {
		private Bread mall;

		public PorducerWorker(Bread mall) {
			this.mall = mall;
		}

		@Override
		public void run() {
			for (int i = 0; i < 10; i++) {
				try {
					mall.increment();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public static class ConsumerWorker implements Runnable {
		private Bread mall;

		public ConsumerWorker(Bread mall) {
			this.mall = mall;
		}

		@Override
		public void run() {
			for (int i = 0; i < 10; i++) {
				try {
					mall.descrement();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
