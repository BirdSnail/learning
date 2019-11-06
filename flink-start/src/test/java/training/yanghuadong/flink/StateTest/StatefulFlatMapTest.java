package training.yanghuadong.flink.StateTest;

import org.apache.flink.streaming.api.operators.StreamFlatMap;
import org.apache.flink.streaming.util.OneInputStreamOperatorTestHarness;
import org.apache.flink.streaming.util.TestHarnessUtil;
import org.junit.Before;
import org.junit.Test;
import training.yanghuadong.flink.funtion.StatefulFlatMapFunction;

import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * @author: Mr.Yang
 * @create: 2019-08-29
 */
public class StatefulFlatMapTest {
	private OneInputStreamOperatorTestHarness<Integer, Integer> testHarness;
	private StatefulFlatMapFunction statefulFlatMapFunction;

	@Before
	public void setupTestHarness() throws Exception {
		statefulFlatMapFunction = new StatefulFlatMapFunction();

		// 将用户自定义函数包装到运算符中
		testHarness = new OneInputStreamOperatorTestHarness<>(new StreamFlatMap<>(statefulFlatMapFunction));

		// 可选的配置执行环境
		testHarness.getExecutionConfig();

		testHarness.open();
	}

	@Test
	public void testStatefulFlatMapFunction() throws Exception {
		// 向operator发送带有时间戳的数据
		testHarness.processElement(2,1);

		//
		testHarness.processWatermark(1);

		ConcurrentLinkedQueue<Object> expectedOutput = new ConcurrentLinkedQueue<>();
		expectedOutput.add(3);
		TestHarnessUtil.assertOutputEquals("Output was not correct.", expectedOutput, testHarness.getOutput());
	}

}
