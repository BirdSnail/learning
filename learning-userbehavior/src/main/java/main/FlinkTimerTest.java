package main;

import Util.SourceUtil;
import org.apache.flink.api.common.JobExecutionResult;
import org.apache.flink.api.common.state.ListState;
import org.apache.flink.api.common.state.ListStateDescriptor;
import org.apache.flink.api.java.functions.KeySelector;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.TimeCharacteristic;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.KeyedProcessFunction;
import org.apache.flink.streaming.api.functions.timestamps.AscendingTimestampExtractor;
import org.apache.flink.util.Collector;
import pojo.UserBehavior;

import java.net.URISyntaxException;

/**
 *
 *
 * @author: yang
 * @date: 2019/7/24
 */
public class FlinkTimerTest {
    private static final String CSV_NAME = "UserBehavior.csv";

    public static void main(String[] args) throws Exception {
        final StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setStreamTimeCharacteristic(TimeCharacteristic.EventTime);
        env.setParallelism(4);

        DataStream<UserBehavior> source = SourceUtil.getUserBehaviorDataStream(env, CSV_NAME);
        source.assignTimestampsAndWatermarks(new AscendingTimestampExtractor<UserBehavior>() {
                    @Override
                    public long extractAscendingTimestamp(UserBehavior element) {
                        return element.getTimestamp() * 1000L;
                    }
                })
                .keyBy(new KeySelector<UserBehavior, Long>() {
                    @Override
                    public Long getKey(UserBehavior value) throws Exception {
                        return value.getItemId();
                    }
                })
                .process(new KeyedProcessFunction<Long, UserBehavior, String>() {

                    private ListState<UserBehavior> itemState;

                    @Override
                    public void open(Configuration parameters) throws Exception {
                        super.open(parameters);
                        ListStateDescriptor<UserBehavior> itemStateDesc = new ListStateDescriptor("list-state", UserBehavior.class);
                        itemState = getRuntimeContext().getListState(itemStateDesc);
                    }

                    @Override
                    public void processElement(UserBehavior value, Context ctx, Collector<String> out) throws Exception {
                        long watermark = ctx.timerService().currentWatermark();
                        System.out.println("watermark: " + watermark);
                        itemState.add(value);

                        ctx.timerService().registerEventTimeTimer(watermark-10L);
                        //out.collect(value.toString() + " : process");
                    }

                    @Override
                    public void onTimer(long timestamp, OnTimerContext ctx, Collector<String> out) throws Exception {
                        System.out.println("onTimer is invoking: " + timestamp);

                        Iterable<UserBehavior> userBehaviors = itemState.get();
                        itemState.clear();

                        for (UserBehavior userBehavior : userBehaviors) {
                            out.collect(userBehavior.toString() + " : onTimer");
                        }

                        Thread.sleep(5000L);
                    }
                }).print();

        System.out.println(env.getExecutionPlan());
        env.execute();

    }
}
