package training.yanghuadong.thread;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * 测试线程池本身是否可以被多个线程共享
 * 结论：是线程安全的
 *
 * @author BirdSnail
 * @date 2020/7/30
 */
public class ExecutorServiceSafeTest {

	static ExecutorService threadPool = Executors.newFixedThreadPool(4);

	public static void main(String[] args) throws InterruptedException {
		Thread thread1 = new Thread(() -> {
			Future<String> future = threadPool.submit(() -> "第一个线程提交的任务");
			try {
				Thread.sleep(2000);
				System.out.println("thread1 result: " + future.get());
			} catch (InterruptedException | ExecutionException e) {
				e.printStackTrace();
			}
		});

		Thread thread2 = new Thread(() -> {
			Future<String> future = threadPool.submit(() -> "第2个线程提交的任务");
			try {
				System.out.println("thread2 result: " + future.get());
			} catch (InterruptedException | ExecutionException e) {
				e.printStackTrace();
			}
		});

		thread1.start();
		thread2.start();
		Thread.sleep(3000);
		threadPool.shutdown();
	}

}
