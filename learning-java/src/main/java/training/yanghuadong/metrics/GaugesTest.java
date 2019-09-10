package training.yanghuadong.metrics;

import com.codahale.metrics.ConsoleReporter;
import com.codahale.metrics.Gauge;
import com.codahale.metrics.MetricRegistry;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;
import java.util.concurrent.TimeUnit;


/**
 * 统计队列种的元素个数
 * @author: Mr.Yang
 * @create: 2019-08-16
 */
public class GaugesTest {

	private static Queue<Integer> queue = new LinkedList<>();
	private static Random random = new Random();

	public static void main(String[] args) throws InterruptedException {
		MetricRegistry registry = new MetricRegistry();
		ConsoleReporter reporter  = ConsoleReporter.forRegistry(registry).build();

		registry.register(MetricRegistry.name(GaugesTest.class, "queue", "size"), new QueneSize());

		reporter.start(1, TimeUnit.SECONDS);

		while (true) {
			Thread.sleep(2000L);
			queue.add(random.nextInt(100));
		}
	}

	/**实现一个Gauge计算队列元素个数*/
	private static class QueneSize implements Gauge<Integer>{
		@Override
		public Integer getValue() {
			return queue.size();
		}
	}

}
