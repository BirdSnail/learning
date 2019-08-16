package yanghuadong.metrics;

import com.codahale.metrics.ConsoleReporter;
import com.codahale.metrics.Histogram;
import com.codahale.metrics.MetricRegistry;

import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * 统计一个指标的直方图信息
 * @author: Mr.Yang
 * @create: 2019-08-16
 */
public class HistorgramTest {
	private static Random random = new Random();

	public static void main(String[] args) throws InterruptedException {
		MetricRegistry registry = new MetricRegistry();
		final ConsoleReporter reporter = ConsoleReporter.forRegistry(registry).build();
		Histogram histogram = registry.histogram(MetricRegistry.name(HistorgramTest.class, "request", "histogram"));

		reporter.start(3L, TimeUnit.SECONDS);

		while (true) {
			histogram.update(random.nextInt(100));
			Thread.sleep(1000L);
		}
	}
}
