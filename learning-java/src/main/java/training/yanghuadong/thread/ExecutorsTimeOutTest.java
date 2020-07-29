package training.yanghuadong.thread;

import java.util.concurrent.*;

/**
 * {@link Future#get()}设置了超时时间时，当超过了时间get()会马上返回，但是task并没有结束
 *
 * @author BirdSnail
 * @date 2020/7/9
 */
public class ExecutorsTimeOutTest {

	public static void main(String[] args)  {
		ExecutorService executorService = Executors.newFixedThreadPool(5);

		Future<String> future = executorService.submit(() -> {
			try {
				Thread.sleep(1500L);
				System.out.println("task is execute");
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			return "haha";
		});
		try {
			future.get(1L, TimeUnit.SECONDS);
		} catch (InterruptedException | ExecutionException | TimeoutException e) {
			e.printStackTrace();
		}
		System.out.println("主线程执行完毕");
		executorService.shutdown();
	}
}
