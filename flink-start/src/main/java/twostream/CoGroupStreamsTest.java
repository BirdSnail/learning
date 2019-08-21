package twostream;

import org.apache.flink.api.common.functions.CoGroupFunction;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.windowing.assigners.TumblingProcessingTimeWindows;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.util.Collector;

import java.util.Iterator;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author: Mr.Yang
 * @create: 2019-08-21
 */
public class CoGroupStreamsTest {

	public static void main(String[] args) throws Exception {

		final StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

		// create source
		DataStream<Tuple2<Integer, Integer>> number = env.fromElements(Tuple2.of(1, 1), Tuple2.of(1, 2),Tuple2.of(2, 2),
				Tuple2.of(3, 3), Tuple2.of(4, 4), Tuple2.of(5, 5), Tuple2.of(6, 6));
		DataStream<Tuple2<Integer, String>> letter = env.fromElements(Tuple2.of(1, "A"),Tuple2.of(1, "A"), Tuple2.of(2, "B"),
				Tuple2.of(3, "C"), Tuple2.of(4, "D"), Tuple2.of(5, "E"), Tuple2.of(6, "F"));

		// connected stream with CoGroupFunction
		number.coGroup(letter)
				.where(first -> first.f0)
				.equalTo(second -> second.f0)
				.window(TumblingProcessingTimeWindows.of(Time.seconds(10L)))
				.apply(new CoGroupFunction<Tuple2<Integer, Integer>, Tuple2<Integer, String>, Tuple2<Integer, String>>() {

					private StringBuilder sb = new StringBuilder();

					@Override
					public void coGroup(
							Iterable<Tuple2<Integer, Integer>> first, Iterable<Tuple2<Integer, String>> second,
							Collector<Tuple2<Integer, String>> out) throws Exception {
						int sum = 0;
						sb.setLength(0);

						Iterator<Tuple2<Integer, Integer>> firstIte = first.iterator();
						Tuple2<Integer, Integer> firstElement = firstIte.next();
						sum += firstElement.f1;

						while (firstIte.hasNext()) {
							sum += firstIte.next().f1;
						}
						sb.append(sum);
						sb.append("->");
						Iterator<Tuple2<Integer, String>> secondIte = second.iterator();
						while (secondIte.hasNext()) {
							sb.append(secondIte.next().f1);
						}

						out.collect(Tuple2.of(firstElement.f0, sb.toString()));
					}
				}).print();

		env.execute("CoGroupTestJob");
	}
}
