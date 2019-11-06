package twostream;

import lombok.Setter;
import lombok.ToString;
import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.api.common.state.*;
import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.api.common.typeinfo.Types;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.datastream.BroadcastStream;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.co.KeyedBroadcastProcessFunction;
import org.apache.flink.util.Collector;

import static twostream.UserBehaviorMatch.action.*;

/**
 * 使用广播变量进行动态模式匹配
 *
 * @author: Mr.Yang
 * @create: 2019-08-30
 */
public class UserBehaviorMatch {

	public static MapStateDescriptor<Void, Pattern> bcStateDescriptor = new MapStateDescriptor<>(
			"store pattern",
			Types.VOID,
			TypeInformation.of(Pattern.class)
	);

	public static void main(String[] args) throws Exception {
		final StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

		// create sources
		DataStream<UserAction> userAction = getUserActionDataStreamSource(env);
		DataStream<Pattern> pattern = env.fromElements(new Pattern(LOGIN, LOGOUT));

		BroadcastStream<Pattern> patternBroadcast = pattern.broadcast(bcStateDescriptor);

		// connect
		userAction
				.keyBy( user -> user.userId)
				.connect(patternBroadcast)
				.process(new PatternEvaluator())
				.print();

		env.execute();
	}

	/**用户行为记录*/
	@Setter
	public static class UserAction {
		public long userId;
		public action action;

		public UserAction(){

		}

		public UserAction(long userId, UserBehaviorMatch.action action) {
			this.userId = userId;
			this.action = action;
		}
	}

	/**行为模式*/
	@ToString
	public static class Pattern{
		public action firstAction;
		public action secondAction;

		public Pattern() {

		}

		public Pattern(action firstAction, action secondAction) {
			this.firstAction = firstAction;
			this.secondAction = secondAction;
		}
	}

	/**行为类型*/
	public enum action{
		LOGIN,
		LOGOUT,
		BUY,
		ADD_TO_CART
	}

	// ===============================================

	private static DataStream<UserAction> getUserActionDataStreamSource(StreamExecutionEnvironment env) {
		DataStream<UserAction> socket = env.socketTextStream("bear", 9021)
				.flatMap(new FlatMapFunction<String, UserAction>() {
					@Override
					public void flatMap(String value, Collector<UserAction> out) throws Exception {
						if (value != null) {
							String[] split = value.split("\\W+");
							if (split.length == 2) {
								long userId = Long.valueOf(split[0]);

								switch (split[1]) {
									case "1":
										out.collect(new UserAction(userId, LOGIN));
										break;
									case "2":
										out.collect(new UserAction(userId, LOGOUT));
										break;
									case "3":
										out.collect(new UserAction(userId, BUY));
										break;
									case "4":
										out.collect(new UserAction(userId, ADD_TO_CART));
										break;
									default:
										break;
								}
							}
						}
					}
				});
		return socket;
	}

	/**对用户行为进行模式评估*/
	public static class PatternEvaluator extends KeyedBroadcastProcessFunction<Long, UserAction, Pattern, Tuple2<Long, Pattern>> {
		private ValueState<action> preActionState;
		private Pattern pattern;

		@Override
		public void open(Configuration parameters) throws Exception {
			ValueStateDescriptor actionDesc = new ValueStateDescriptor("saved action", action.class);
			preActionState = getRuntimeContext().getState(actionDesc);
		}

		@Override
		public void processElement(UserAction lastAction, ReadOnlyContext ctx, Collector<Tuple2<Long, Pattern>> out) throws Exception {
			action preAction = preActionState.value();
			pattern = ctx.getBroadcastState(bcStateDescriptor).get(null);
			if (pattern == null) {
				return;
			}

			if (preAction != null && preAction == pattern.firstAction && lastAction.action == pattern.secondAction) {
				out.collect(Tuple2.of(lastAction.userId, pattern));
			}

			preActionState.update(lastAction.action);
		}

		@Override
		public void processBroadcastElement(Pattern value, Context ctx, Collector<Tuple2<Long, Pattern>> out) throws Exception {
			ctx.getBroadcastState(bcStateDescriptor).put(null, value);
		}
	}
}
