package state;

import org.apache.flink.api.common.functions.RichMapFunction;
import org.apache.flink.api.common.state.MapState;
import org.apache.flink.api.common.state.MapStateDescriptor;
import org.apache.flink.api.common.typeinfo.BasicTypeInfo;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

/**
 * @author: Mr.Yang
 * @create: 2019-08-22
 */
public class MapStateTest {
	private static final MapStateDescriptor<String, Integer> MAP_STATE_DESCRIPTOR = new MapStateDescriptor<String, Integer>(
			"mapStateDescriptor",
			BasicTypeInfo.STRING_TYPE_INFO,
			BasicTypeInfo.INT_TYPE_INFO);

	public static void main(String[] args) throws Exception {
		final StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

		env.fromElements(1, 2, 3, 4, 5)
				.keyBy(ele -> 0)
				.map(new RichMapFunction<Integer, Integer>() {
					@Override
					public Integer map(Integer value) throws Exception {
						MapState<String, Integer> mapState = getRuntimeContext().getMapState(MAP_STATE_DESCRIPTOR);
						Integer intervalue = mapState.get("one");
						if (intervalue == null) {
							intervalue = 0;
						}
						mapState.put("one", value + intervalue);

						System.out.println(mapState.get("one"));
						return value;
					}
				});

		env.execute();
	}
}
