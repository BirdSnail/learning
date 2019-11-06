package training.yanghuadong.flink.UnStateTest;

import org.apache.flink.util.Collector;
import org.junit.Test;
import org.mockito.Mockito;
import training.yanghuadong.flink.funtion.MyFlatMapFunction;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;

/**
 * 使用单元测试对我们自定义的flink functino进行测试
 * @author: Mr.Yang
 * @create: 2019-08-29
 */
public class MyMapFunctionTest {

	@Test
	public void testFlatMap() throws Exception {
		MyFlatMapFunction flatMap = new MyFlatMapFunction();

		Collector<Integer> collector = mock(Collector.class);

		flatMap.flatMap(6, collector);
		Mockito.verify(collector,times(1)).collect(6);
	}
}
