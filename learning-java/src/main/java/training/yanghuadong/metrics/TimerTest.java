package training.yanghuadong.metrics;

import com.codahale.metrics.ConsoleReporter;
import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.Timer;

import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * timer是histogram与meter的结合，histogram统计每次调用的耗时形成直方图的数据，
 * meter统计调用次数形成tps、最近1、5、15分钟调用速度等数据。
 * @author: Mr.Yang
 * @create: 2019-08-16
 */
public class TimerTest {

	private static Random random = new Random();

	public static void main(String[] args) throws InterruptedException {
		MetricRegistry registry = new MetricRegistry();
		final ConsoleReporter reporter = ConsoleReporter.forRegistry(registry).build();

		Timer timer = registry.timer(MetricRegistry.name(TimerTest.class, "get-requests"));
		Timer.Context ctx;

		reporter.start(3L, TimeUnit.SECONDS);

		// 从timer.time()到ctx.stop()算作一次请求 ---> meter
		// 中间的时间差值是请求耗时  -----------------> histogram
		while (true) {
			ctx = timer.time();
			Thread.sleep(random.nextInt(5000));
			ctx.stop();
		}
	}
}
