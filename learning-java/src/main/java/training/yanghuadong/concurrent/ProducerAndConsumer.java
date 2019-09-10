package training.yanghuadong.concurrent;

/**
 * 生产者消费者
 *  使用synchronized，wait，notify三个方法实现
 *
 * @author: Mr.Yang
 * @create: 2019-09-05
 */
public class ProducerAndConsumer {

	/**
	 * 一个资源类
	 */
	public static class Mall {
		private int breadNumber = 0;

		public synchronized void increment() throws InterruptedException {
			// 不能使用if进行条件判断
			// if会存在虚假唤醒
			while (breadNumber != 0) {
				this.wait();
			}

			++breadNumber;
			Thread.sleep(1000);
			System.out.println(Thread.currentThread().getName() + ":生产了一个面包，还剩" + breadNumber + "个面包");
			this.notifyAll();
		}

		public synchronized void descrement() throws InterruptedException {
			while (breadNumber == 0) {
				this.wait();
			}

			--breadNumber;
			Thread.sleep(1000);
			System.out.println(Thread.currentThread().getName() + ":吃掉了一个面包，还剩" + breadNumber + "个面包");
			this.notifyAll();

		}
	}

	public static void main(String[] args) {
		Mall mall = new Mall();

		new Thread(new PorducerWorker(mall)).start();
		new Thread(new PorducerWorker(mall)).start();

		new Thread(new ConsumerWorker(mall)).start();
		new Thread(new ConsumerWorker(mall)).start();
	}

	//================================
	//  WORK CLASS
	//================================

	public static class PorducerWorker implements Runnable {
		private Mall mall;

		public PorducerWorker(Mall mall) {
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
		private Mall mall;

		public ConsumerWorker(Mall mall) {
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
