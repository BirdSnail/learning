package twostream;

import org.apache.flink.streaming.api.TimeCharacteristic;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.ProcessFunction;
import org.apache.flink.streaming.api.functions.co.CoMapFunction;
import org.apache.flink.streaming.api.windowing.assigners.TumblingProcessingTimeWindows;
import org.apache.flink.util.Collector;


/**
 * @author: Mr.Yang
 * @create: 2019-08-21
 */
public class ConnectedStreamTest {

	public static void main(String[] args) throws Exception {
		final StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
		env.setStreamTimeCharacteristic(TimeCharacteristic.EventTime);

		DataStream<String> input1 = env.fromElements("a", "b", "c", "d");
		DataStream<String> input2 = env.socketTextStream("bear",9021);

		input1.connect(input2)
				.map(new CoMapFunction<String, String, String>() {
					@Override
					public String map1(String value) throws Exception {
						return value + "--one";
					}

					@Override
					public String map2(String value) throws Exception {
						return value + "--two";
					}
				})
				.print();

		env.execute();
	}
}
