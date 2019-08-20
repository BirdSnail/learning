package project;


import org.apache.flink.api.common.functions.AggregateFunction;
import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.api.java.tuple.Tuple;
import org.apache.flink.api.java.tuple.Tuple3;
import org.apache.flink.streaming.api.TimeCharacteristic;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.timestamps.AscendingTimestampExtractor;
import org.apache.flink.streaming.api.functions.timestamps.BoundedOutOfOrdernessTimestampExtractor;
import org.apache.flink.streaming.api.functions.windowing.ProcessWindowFunction;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.streaming.api.windowing.windows.TimeWindow;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer;
import org.apache.flink.util.Collector;
import project.schema.LogDeserializtionSchema;

import java.util.Iterator;
import java.util.Properties;

/**
 * 功能：
 * 最近一分钟每个域名产生的流量统计
 *
 * @author: Mr.Yang
 * @create: 2019-08-19
 */
public class LogAnalysis {

	private static final long WINDOW_SIZE = 10L;

	public static void main(String[] args) throws Exception {
		final StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
		env.setStreamTimeCharacteristic(TimeCharacteristic.EventTime);

		// three	CN	M	1566206559573	175.148.211.190 v2.go2yd.com	7608
		DataStream<RequestLog> log = getKafkaDataSource(env);
		log.assignTimestampsAndWatermarks(
				new AscendingTimestampExtractor<RequestLog>() {
					@Override
					public long extractAscendingTimestamp(RequestLog log) {
						return log.getTime();
					}
				})
				.keyBy("domain")
				.timeWindow(Time.seconds(WINDOW_SIZE))
				.aggregate(new AggregateFunction<RequestLog, Integer, Integer>() {
					           @Override
					           public Integer createAccumulator() {
						           return 0;
					           }

					           @Override
					           public Integer add(RequestLog value, Integer accumulator) {
						           accumulator += value.getTraffic();
									return accumulator;
					           }

					           @Override
					           public Integer getResult(Integer accumulator) {
									return accumulator;
					           }

					           @Override
					           public Integer merge(Integer a, Integer b) {
						           return a + b;
					           }
							},
						new ProcessWindowFunction<Integer, Tuple3<String, Long, Integer>, Tuple, TimeWindow>() {
							@Override
							public void process(Tuple key, Context context, Iterable<Integer> elements, Collector<Tuple3<String, Long, Integer>> out)
									throws Exception {
								Iterator<Integer> sumOfTriffac = elements.iterator();
								if (sumOfTriffac.hasNext()) {
									out.collect(Tuple3.of(key.toString(), context.window().getEnd(), sumOfTriffac.next()));
								}
							}
						})
		.print();


		env.execute("LogAnalysisJob");
	}

	/**
	 * 以kafka为数据源
	 */
	private static DataStream<RequestLog> getKafkaDataSource(StreamExecutionEnvironment env) {
		Properties properties = new Properties();
		properties.setProperty("bootstrap.servers", "bear:9092");
		properties.setProperty("group.id", "test");

		TypeInformation<RequestLog> info = TypeInformation.of(RequestLog.class);

		DataStream<RequestLog> stream = env
				.addSource(new FlinkKafkaConsumer<>(Simulator.TOPIC, new LogDeserializtionSchema(info), properties));
		return stream;
	}

	// =======================================================
	//  USER FUNCTION
	// =======================================================

	private static class LogEventtime extends BoundedOutOfOrdernessTimestampExtractor<RequestLog> {

		public LogEventtime(Time maxOutOfOrderness) {
			super(maxOutOfOrderness);
		}

		@Override
		public long extractTimestamp(RequestLog log) {
			return log.getTime();
		}
	}

}
