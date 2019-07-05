package accumulator;

import org.apache.flink.api.common.JobExecutionResult;
import org.apache.flink.api.java.DataSet;
import org.apache.flink.api.java.ExecutionEnvironment;
import org.apache.flink.api.java.tuple.Tuple3;
import java.util.ArrayList;


/**
 * 自定义累加器使用示例
 *
 * @author: Mr.Yang
 * @create: 2019-07-05
 */
public class EmptyFieldsAccumulator {

	public static final String EMPTY_FIELD_ACCUMULATOR = "empty_field_accumulator";
	private static final Tuple3[] inputTuples = new Tuple3[]{Tuple3.of("hello", "ord", "age"),
			Tuple3.of("flink", null, "hadoop"),
			Tuple3.of("nihao", "value", " "),
			Tuple3.of("fire", "", "dnf")};


	public static void main(String[] args) throws Exception {
		final ExecutionEnvironment env = ExecutionEnvironment.getExecutionEnvironment();

		final DataSet<Tuple3<String, String, String>> source = env.fromElements(inputTuples);

		source.filter(new EmptyFieldFilterFunction())
				.print();

		// 作业执行结果
		// 调用print()方法会自动执行env.execute()。
		JobExecutionResult res = env.getLastJobExecutionResult();
		// 获取累计器的Instance
		ArrayList<Integer> count = res.getAccumulatorResult(EMPTY_FIELD_ACCUMULATOR);
		System.out.println("不合格的元素个数：" + count);
	}
}
