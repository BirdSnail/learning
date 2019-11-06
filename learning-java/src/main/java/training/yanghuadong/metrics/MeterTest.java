package training.yanghuadong.metrics;

import com.codahale.metrics.ConsoleReporter;
import com.codahale.metrics.Meter;
import com.codahale.metrics.MetricRegistry;

import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * 统计TPS,meter会自动统计最近一分钟，5分钟，15分钟事件发生的速度
 * @author: Mr.Yang
 * @create: 2019-08-16
 */
public class MeterTest {

	private static Random random = new Random();
	private static Meter meter;
	private static int number = 1;

	private static void request(String message) {
		System.out.println(message + " " + number++);
		meter.mark();
	}

	/**请求指定次数*/
	private static void request(String message, int n) {
		while (n > 0) {
			request(message);
			n--;
		}
	}

	public static void main(String[] args) throws InterruptedException {
		MetricRegistry registry = new MetricRegistry();
		final ConsoleReporter reporter = ConsoleReporter.forRegistry(registry).build();
		meter = registry.meter(MetricRegistry.name(MeterTest.class, "request", "tps"));

		reporter.start(3L, TimeUnit.SECONDS);

		while (true) {
			request("hello", random.nextInt(5));
			Thread.sleep(1000L);
		}
	}

}
