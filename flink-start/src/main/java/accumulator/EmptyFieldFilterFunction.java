package accumulator;

import org.apache.flink.api.common.accumulators.IntCounter;
import org.apache.flink.api.common.functions.RichFilterFunction;
import org.apache.flink.api.java.tuple.Tuple3;
import org.apache.flink.configuration.Configuration;

/**
 * 在filter函数里面使用自定义的累加器
 * 累加器使用说明：
 * 1. 创建一个累加器
 * 2. 通过context注册该累加器
 * 3. 更新累加器的值
 * 4. job结束后可获取累加器
 *
 * @author: Mr.Yang
 * @create: 2019-07-05
 */
public class EmptyFieldFilterFunction extends RichFilterFunction<Tuple3<String, String, String>> {

	// 创建累加器
	//private IntCounter counter = new IntCounter();
	private ListAccumulator emptyFieldCounter = new ListAccumulator();

	@Override
	public void open(Configuration parameters) throws Exception {
		super.open(parameters);

		// 向context中注册累加器
		getRuntimeContext().addAccumulator(EmptyFieldsAccumulator.EMPTY_FIELD_ACCUMULATOR, this.emptyFieldCounter);
	}

	@Override
	public boolean filter(Tuple3<String, String, String> value) throws Exception {
		boolean isEmptyField = false;

		if (checkEmptyFiled(value.f0)) {
			emptyFieldCounter.add(0);
			isEmptyField = true;
		}
		if (checkEmptyFiled(value.f1)) {
			emptyFieldCounter.add(1);
			isEmptyField = true;
		}

		if (checkEmptyFiled(value.f2)) {
			emptyFieldCounter.add(2);
			isEmptyField = true;
		}

		return !isEmptyField;
	}

	private boolean checkEmptyFiled(String value) {
		if (value == null || value.trim().isEmpty()) {
			return true;
		}
		return false;
	}
}
