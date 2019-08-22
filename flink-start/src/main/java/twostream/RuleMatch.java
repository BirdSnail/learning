package twostream;

import com.cloudwise.sdg.dic.DicInitializer;
import com.cloudwise.sdg.template.TemplateAnalyzer;
import lombok.Data;
import lombok.ToString;
import org.apache.flink.api.common.state.MapState;
import org.apache.flink.api.common.state.MapStateDescriptor;
import org.apache.flink.api.common.typeinfo.BasicTypeInfo;
import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.api.java.typeutils.ListTypeInfo;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.datastream.BroadcastStream;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.co.KeyedBroadcastProcessFunction;
import org.apache.flink.streaming.api.functions.source.RichSourceFunction;
import org.apache.flink.util.Collector;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 一个数据流，一个规则流。
 * 将规则流进行广播，数据流从广播变量中获取规则进行匹配.
 *
 * 找出连续两个满足特定形状的球，如一个三角形后面跟着一个矩形。规则流负责定义shape的顺序
 * @author: Mr.Yang
 * @create: 2019-08-22
 */
public class RuleMatch {

	private final static MapStateDescriptor<String, Rule> ruleMapStateDescriptor = new MapStateDescriptor<>(
			"ruleMapStateDescriptor",
			BasicTypeInfo.STRING_TYPE_INFO,
			TypeInformation.of(Rule.class));

	// 存放符合规则的元素
	private final static MapStateDescriptor<String, List<Ball>> resultMapStateDescriptor = new MapStateDescriptor<>(
			"resultMapStateDescriptor",
			BasicTypeInfo.STRING_TYPE_INFO,
			new ListTypeInfo<>(Ball.class));

	public static void main(String[] args) throws Exception {
		final StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

		DataStream<Rule> ruleSource = env.fromElements(new Rule("one", "triangle", "rectangle"),
				new Rule("two","rectangle","circle"));
		DataStreamSource<Ball> ball = env.addSource(new BallGenerator());

		// broad rule
		final BroadcastStream<Rule> ruleBroadcastStream = ruleSource.broadcast(ruleMapStateDescriptor);

		ball.keyBy("color")
				.connect(ruleBroadcastStream)
				.process(new ProcessOfMatchRule())
				.print();

		env.execute();
	}

	// ====================================================================
	// DATA CLASS
	// ====================================================================

	/**需要广播的规则流*/
	@Data
	private static class Rule{
		private String ruleName;
		private String[] rule = new String[2];

		public Rule(String ruleName, String firstShape, String secondShape) {
			this.ruleName = ruleName;
			this.rule[0] = firstShape;
			this.rule[1] = secondShape;
		}
	}

	/**需要进行匹配的元素*/
	@Data
	@ToString
	public static class Ball{
		private int id;
		private String color;
		private String shape;

		public Ball() {}

		public Ball(int id, String color, String shape) {
			this.id = id;
			this.color = color;
			this.shape = shape;
		}
	}

	// ====================================================================
	// USER FUNCTION
	// ====================================================================

	/**flink source*/
	private static class BallGenerator extends RichSourceFunction<Ball> {
		private TemplateAnalyzer tplAnalyzer;
		private int id = 0;
		private boolean isRunning = true;
		private Ball ball;

		@Override
		public void open(Configuration parameters) throws Exception {
			DicInitializer.init();
			//编辑模版
			String tplName = "abc.tpl";
			String tpl = "$Dic{color}\t$Dic{shape}";
			tplAnalyzer = new TemplateAnalyzer(tplName, tpl);
		}

		@Override
		public void run(SourceContext<Ball> ctx) throws Exception {
			while (isRunning) {
				Thread.sleep(2000L);

				String[] values = tplAnalyzer.analyse().split("\t");
				ball = new Ball(id++, values[0], values[1]);
				System.out.println(ball);

				ctx.collect(ball);
			}
		}

		@Override
		public void cancel() {
			isRunning = false;
		}
	}

	/**对元素按照规则进行匹配，输出符合规则的元素*/
	private static class ProcessOfMatchRule extends KeyedBroadcastProcessFunction<String, Ball, Rule, String> {

		@Override
		public void processElement(Ball value, ReadOnlyContext ctx, Collector<String> out) throws Exception {
			String shape = value.getShape();
			MapState<String, List<Ball>> resultState = getRuntimeContext().getMapState(resultMapStateDescriptor);

			for (Map.Entry<String, Rule> entry :ctx.getBroadcastState(ruleMapStateDescriptor).immutableEntries()) {
				final Rule rule = entry.getValue();
				final String ruleName = entry.getKey();

				List<Ball> result = resultState.get(ruleName);
				if (result == null) {
					result = new ArrayList<>();
				}

				if (shape.equals(rule.getRule()[1]) && !result.isEmpty()) {
					result.forEach(ball -> out.collect("Match " + ruleName + ": " + ball + "-->" + value));
				}
				result.clear();

				if (shape.equals(rule.getRule()[0])) {
					result.add(value);
				}

				// 更新状态
				if (result.isEmpty()) {
					resultState.remove(ruleName);
				} else {
					resultState.put(ruleName, result);
				}
			}
		}

		@Override
		public void processBroadcastElement(Rule value, Context ctx, Collector<String> out) throws Exception {
			if (value != null) {
				ctx.getBroadcastState(ruleMapStateDescriptor).put(value.getRuleName(), value);
			}
		}
	}
}
