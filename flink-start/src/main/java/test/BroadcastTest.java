package test;

import org.apache.flink.api.common.functions.RichMapFunction;
import org.apache.flink.api.java.ExecutionEnvironment;
import org.apache.flink.api.java.operators.DataSource;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.configuration.Configuration;

import java.util.Collection;

/**
 * 使用广播变量时，若广播名相同，后一个会覆盖前一个
 * @author: Mr.Yang
 * @create: 2019-07-15
 */
public class BroadcastTest {

	public static void main(String[] args) throws Exception {
		final ExecutionEnvironment env = ExecutionEnvironment.getExecutionEnvironment();

		DataSource<String> source = env.fromElements("a", "b");
		DataSource<Integer> broadcast1 = env.fromElements(1, 1, 1);
		DataSource<Integer> broadcast2 = env.fromElements(2, 2, 2);

		source.map(new ContactTwoData())
				.withBroadcastSet(broadcast2,"test")
				.withBroadcastSet(broadcast1,"test")
				.print();
	}

	//********************************USER FUNCTION****************************************

	/**
	 * 使用广播变量
	 */
	private static class ContactTwoData extends RichMapFunction<String, Tuple2<String, Integer>> {

		private Collection<Integer> broadcast;

		@Override
		public void open(Configuration parameters) throws Exception {
			super.open(parameters);
			broadcast = getRuntimeContext().getBroadcastVariable("test");
		}

		@Override
		public Tuple2<String, Integer> map(String value) throws Exception {
			int count = 0;
			for (Integer num : broadcast) {
				count += num;
			}

			return Tuple2.of(value, count);
		}
	}
}
