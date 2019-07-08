package batch;

import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.common.functions.ReduceFunction;
import org.apache.flink.api.java.DataSet;
import org.apache.flink.api.java.ExecutionEnvironment;


import java.util.List;

/**
 * 蒙特卡洛方法求pi
 * @author: Mr.Yang
 * @create: 2019-07-08
 */
public class Pi {

	public static void main(String[] args) throws Exception {
		final Long numSamples = 10000L;
		final ExecutionEnvironment env = ExecutionEnvironment.getExecutionEnvironment();

		DataSet<Long> count = env.generateSequence(1, numSamples)
				.map(new Sample())
				.reduce(new SumReducer());

		List<Long> list = count.collect();
		Double pi = 4.0 * list.get(0) / numSamples;
		System.out.println("圆周率是：" + pi);
	}

	//**************************************************************************************
	//UTIL FUNCTION
	//**************************************************************************************
	private static class Sample implements MapFunction<Long,Long>{

		@Override
		public Long map(Long value) throws Exception {
			double x = Math.random();
			double y = Math.random();
			return (x * x + y * y) < 1 ? 1L : 0L;
		}
	}

	private static class SumReducer implements ReduceFunction<Long>{

		@Override
		public Long reduce(Long value1, Long value2) throws Exception {
			return value1 + value2;
		}
	}

}
