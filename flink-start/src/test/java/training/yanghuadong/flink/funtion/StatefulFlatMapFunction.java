package training.yanghuadong.flink.funtion;

import org.apache.flink.api.common.functions.RichFlatMapFunction;
import org.apache.flink.api.common.state.ValueState;
import org.apache.flink.api.common.state.ValueStateDescriptor;
import org.apache.flink.api.common.typeinfo.BasicTypeInfo;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.util.Collector;

/**
 * @author: Mr.Yang
 * @create: 2019-08-29
 */
public class StatefulFlatMapFunction extends RichFlatMapFunction<Integer, Integer> {

	private ValueState<Integer> countState;

	@Override
	public void open(Configuration parameters) throws Exception {
		countState = getRuntimeContext().getState(new ValueStateDescriptor<Integer>(
				"saved count",
				BasicTypeInfo.INT_TYPE_INFO
		));
		countState.update(0);

		System.out.println("open()被调用.....");
	}

	@Override
	public void flatMap(Integer value, Collector<Integer> out) throws Exception {
		Integer count = countState.value();

		if (value > 2) {
			out.collect(value);
			countState.update(++count);
		}
	}
}
