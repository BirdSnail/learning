package twostream;

import org.apache.flink.api.common.state.MapStateDescriptor;
import org.apache.flink.api.common.typeinfo.BasicTypeInfo;
import org.apache.flink.streaming.api.TimeCharacteristic;
import org.apache.flink.streaming.api.datastream.BroadcastStream;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.co.BroadcastProcessFunction;
import org.apache.flink.util.Collector;

/**
 * @author: Mr.Yang
 * @create: 2019-08-21
 */
public class BroadcastConnectedStreamTest {

	private final static MapStateDescriptor<String, String> broadDescriptor = new MapStateDescriptor<>("broadDescriptor",
			BasicTypeInfo.STRING_TYPE_INFO,BasicTypeInfo.STRING_TYPE_INFO);

	public static void main(String[] args) throws Exception {
		final StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
		env.setStreamTimeCharacteristic(TimeCharacteristic.EventTime);

		BroadcastStream<String> broadStream = env.fromElements("a", "b", "c", "d").broadcast(broadDescriptor);
		DataStream<String> input = env.socketTextStream("bear",9021);

		input.connect(broadStream)
				.process(new BroadcastProcessFunction<String, String, String>() {
					@Override
					public void processElement(String value, ReadOnlyContext ctx, Collector<String> out) throws Exception {
						String s = ctx.getBroadcastState(broadDescriptor).get(value);
						if (s != null) {
							out.collect("Origin:" + value + ",broad:" + s);
						}
					}

					@Override
					public void processBroadcastElement(String value, Context ctx, Collector<String> out) throws Exception {
						System.out.println("BroadcastStream element is: " + value);
						ctx.getBroadcastState(broadDescriptor).put(value,value);
					}
				})
				.print();

		env.execute();
	}
}
