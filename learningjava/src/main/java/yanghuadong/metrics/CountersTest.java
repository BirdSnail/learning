package yanghuadong.metrics;

import com.codahale.metrics.ConsoleReporter;
import com.codahale.metrics.Counter;
import com.codahale.metrics.MetricRegistry;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * 使用metric的计数器统计队列元素中剩余个数
 * @author: Mr.Yang
 * @create: 2019-08-16
 */
public class CountersTest {

	private static Queue<Integer> queue = new LinkedList<>();
	private static Random random = new Random();
	private static Counter counter;

	private static void addElement(Integer element) {
		queue.offer(element);
		counter.inc(1L);
		System.out.println("add element: " + element);
	}

	private static void takeElement() {
		Integer element = queue.poll();
		if (element != null) {
			counter.dec();
		}
		System.out.println("take element: " + element);
	}

	public static void main(String[] args) throws InterruptedException {
		MetricRegistry registry = new MetricRegistry();
		final ConsoleReporter reporter = ConsoleReporter.forRegistry(registry).build();
		counter = registry.counter(MetricRegistry.name(CountersTest.class, "queue", "size"));

		reporter.start(5L, TimeUnit.SECONDS);

		while (true) {
			Thread.sleep(1000L);
			if (random.nextDouble() > 0.3) {
				addElement(random.nextInt(100));
			} else {
				takeElement();
			}
		}
	}


}
