package iterator;

import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.api.java.tuple.Tuple3;
import org.apache.flink.api.java.tuple.Tuple5;
import org.apache.flink.streaming.api.collector.selector.OutputSelector;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.IterativeStream;
import org.apache.flink.streaming.api.datastream.SplitStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.source.SourceFunction;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * @author: Mr.Yang
 * @create: 2019-07-04
 */
public class IteratorExample {

	private static final int BOUND = 100;

	public static void main(String[] args) throws Exception {
		final StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
		env.setBufferTimeout(1L);

		DataStreamSource<Tuple2<Integer, Integer>> fibonacci = env.addSource(new RandomFibonacciSource());

		// 产生一个迭代流
		IterativeStream<Tuple5<Integer, Integer, Integer, Integer, Integer>> iterativeStream = fibonacci.map(new FibonacciMapFunction())
				.iterate(5000L);

		// 需要迭代的操作
		SplitStream<Tuple5<Integer, Integer, Integer, Integer, Integer>> step = iterativeStream.map(new Step())
				.split(new FibonacciOverSelector());

		// 指定迭代操作的结束点
		iterativeStream.closeWith(step.select("iterate"));

		// 操作跳出迭代的流
		step.select("output")
				.map(new MapFunction<Tuple5<Integer, Integer, Integer, Integer, Integer>, Tuple3<Integer, Integer, Integer>>() {
					@Override
					public Tuple3<Integer, Integer, Integer> map(Tuple5<Integer, Integer, Integer, Integer, Integer> value) throws Exception {
						return Tuple3.of(value.f0, value.f1, value.f4);
					}
				}).print();

		env.execute("IterateStreamOfFibonacci");

	}

	/**
	 * Generate BOUND number of random integer pairs from the range from 1 to BOUND/2.
	 */
	private static class RandomFibonacciSource implements SourceFunction<Tuple2<Integer, Integer>> {
		private static final long serialVersionUID = 1L;

		private Random rnd = new Random();

		private volatile boolean isRunning = true;
		private int counter = 0;

		@Override
		public void run(SourceContext<Tuple2<Integer, Integer>> ctx) throws Exception {

			while (isRunning && counter < BOUND) {
				int first = rnd.nextInt(BOUND / 2 - 1) + 1;
				int second = rnd.nextInt(BOUND / 2 - 1) + 1;

				Tuple2<Integer, Integer> record = new Tuple2<>(first, second);
				System.out.println(record);

				ctx.collect(record);
				counter++;
				Thread.sleep(50L);
			}
		}

		@Override
		public void cancel() {
			isRunning = false;
		}
	}

	/**
	 * tuple2转换为tuple5，保存原先的两元素，新增迭代后的结果和次数
	 */
	private static class FibonacciMapFunction implements MapFunction<Tuple2<Integer, Integer>, Tuple5<Integer, Integer, Integer, Integer, Integer>> {

		@Override
		public Tuple5<Integer, Integer, Integer, Integer, Integer> map(Tuple2<Integer, Integer> value) throws Exception {
			return Tuple5.of(value.f0, value.f1, value.f0, value.f1, 0);
		}
	}

	/**
	 * 迭代函数，获取一个斐波那契数
	 */
	private static class Step implements MapFunction<Tuple5<Integer, Integer, Integer, Integer, Integer>, Tuple5<Integer, Integer, Integer, Integer, Integer>> {

		@Override
		public Tuple5<Integer, Integer, Integer, Integer, Integer> map(Tuple5<Integer, Integer, Integer, Integer, Integer> value) throws Exception {
			return Tuple5.of(value.f0, value.f1, value.f3, value.f2 + value.f3, ++value.f4);
		}
	}

	/**
	 * 选择需要进行迭代的流，不满足条件的直接输出到下游，不进行迭代运算
	 */
	private static class FibonacciOverSelector implements OutputSelector<Tuple5<Integer, Integer, Integer, Integer, Integer>> {

		@Override
		public Iterable<String> select(Tuple5<Integer, Integer, Integer, Integer, Integer> value) {
			List<String> out = new ArrayList();
			if (value.f2 < BOUND && value.f3 < BOUND) {
				out.add("iterate");
			} else {
				out.add("output");
			}

			return out;
		}
	}

}
