package training.yanghuadong.flink.funtion;

import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.util.Collector;

/**
 * @author: Mr.Yang
 * @create: 2019-08-29
 */
public class MyFlatMapFunction implements FlatMapFunction<Integer, Integer> {

	@Override
	public void flatMap(Integer value, Collector<Integer> out) throws Exception {
		if (value > 5) {
			out.collect(value);
		}
	}
}
