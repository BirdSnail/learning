package ontimer;

import lombok.Setter;
import lombok.ToString;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.common.state.ValueState;
import org.apache.flink.api.common.state.ValueStateDescriptor;
import org.apache.flink.api.java.tuple.Tuple;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.TimeCharacteristic;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.KeyedProcessFunction;
import org.apache.flink.streaming.api.functions.timestamps.AscendingTimestampExtractor;
import org.apache.flink.util.Collector;


/**
 * @author: Mr.Yang
 * @create: 2019-08-26
 */
public class OnTimerTest {

	public static void main(String[] args) throws Exception {
		final StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
		env.setStreamTimeCharacteristic(TimeCharacteristic.EventTime);
		env.setParallelism(1);

		DataStream<Tuple2<String, Integer>> source;
//		source= env.fromElements(Tuple2.of("a", 1), Tuple2.of("b", 8), Tuple2.of("c", 9),
//				Tuple2.of("a", 10));
				//, Tuple2.of("a", 2), Tuple2.of("a", 1));

		source = env.socketTextStream("bear", 9021)
				.map(new MapFunction<String, Tuple2<String, Integer>>() {
					@Override
					public Tuple2<String, Integer> map(String value) throws Exception {
						String[] split = value.split("\\W+");
						return Tuple2.of(split[0], Integer.valueOf(split[1]));
					}
				});

		source.assignTimestampsAndWatermarks(new AscendingTimestampExtractor<Tuple2<String, Integer>>() {
					@Override
					public long extractAscendingTimestamp(Tuple2<String, Integer> element) {
						return element.f1;
					}
				})
				.keyBy(0)
				.process(new KeyedProcessFunction<Tuple, Tuple2<String, Integer>, CountWithTimestamp>() {
					private ValueState<CountWithTimestamp> countOfKey;

					@Override
					public void open(Configuration parameters) throws Exception {
						countOfKey = getRuntimeContext().getState(new ValueStateDescriptor<>(
								"saved count of every key",
								CountWithTimestamp.class));
					}

					@Override
					public void processElement(Tuple2<String, Integer> value, Context ctx, Collector<CountWithTimestamp> out) throws Exception {
						CountWithTimestamp current = countOfKey.value();

						if (current == null) {
							current = new CountWithTimestamp();
							current.key = value.f0;
						}
						current.sum++;
						current.timestamp = ctx.timestamp();
						System.out.println("current element timestamp: " + ctx.timestamp());
						System.out.println("元素时间是：" + value.f1);

						countOfKey.update(current);

						long onTimer = current.timestamp + 5;
						ctx.timerService().registerEventTimeTimer(onTimer);
						System.out.println("注册的定时时间是：" + onTimer);

					}

					@Override
					public void onTimer(long timestamp, OnTimerContext ctx, Collector<CountWithTimestamp> out) throws Exception {
						System.out.println("计时器的触发时的时间戳：" + timestamp);
						long watermark = ctx.timerService().currentWatermark();
						CountWithTimestamp result = countOfKey.value();
						System.out.println("当前watermark是：" + watermark);

						// 每来一个元素就检除这个元素外，最近5s没有更新过的元素
						// 若相等就代表这个状态值在最近5s没有发生过更改
						// 因为timestamp在注册过后就不会发生变化，但是valueState是根据key对应的值在不断变化的
						if (timestamp == result.timestamp + 5) {
							out.collect(result);
							System.out.println("清除了状态：" + result);
							countOfKey.clear();
						}
					}

				})
				.print();

		//System.out.println(Long.MAX_VALUE);
		env.execute();

	}

	@Setter
	@ToString
	private static class CountWithTimestamp {
		public String key;
		public int sum;
		public long timestamp;
	}

	//====================================
	// USER FUNCTION
	//====================================

}
