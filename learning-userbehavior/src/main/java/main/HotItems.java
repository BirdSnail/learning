package main;

import Util.SourceUtil;
import function.TopNHotItems;
import function.WindowResultFunction;
import org.apache.flink.api.common.accumulators.Accumulator;
import org.apache.flink.api.common.accumulators.LongCounter;
import org.apache.flink.api.common.accumulators.SimpleAccumulator;
import org.apache.flink.api.common.functions.AggregateFunction;
import org.apache.flink.api.common.functions.FilterFunction;
import org.apache.flink.api.common.typeinfo.TypeHint;
import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.api.java.functions.KeySelector;
import org.apache.flink.api.java.io.PojoCsvInputFormat;
import org.apache.flink.api.java.tuple.Tuple;
import org.apache.flink.api.java.typeutils.PojoTypeInfo;
import org.apache.flink.api.java.typeutils.TypeExtractor;
import org.apache.flink.core.fs.Path;
import org.apache.flink.streaming.api.TimeCharacteristic;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.timestamps.BoundedOutOfOrdernessTimestampExtractor;
import org.apache.flink.streaming.api.functions.windowing.ProcessWindowFunction;
import org.apache.flink.streaming.api.functions.windowing.WindowFunction;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.streaming.api.windowing.windows.TimeWindow;
import org.apache.flink.util.Collector;
import pojo.ItemViewCount;
import pojo.UserBehavior;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;

/**
 * <p>每五分钟输出一次最近一个小时点击最多的商品
 * <p>
 * <p>思路：
 * 1. 按照商品id分组 --------------------------------------> keyBy()
 * 2. 创建滑动窗口window（size：1hour，slide：5min）--------> timeWindow()
 * 3. 同种商品进行累加，获得一个包含窗口标志的累加值 ----------> aggregate()
 * 4. 按照窗口标志进行分组 ---------------------------------> keyBy()
 * 5. 获取同一窗口内元素的topN -----------------------------> process()
 *
 * @author: yang
 * @date: 2019/7/23
 */
public class HotItems {

    private static final String CSV_NAME = "UserBehavior.csv";

    public static void main(String[] args) throws Exception {
        final StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(4);
        env.getConfig().disableForceKryo();
        env.setStreamTimeCharacteristic(TimeCharacteristic.EventTime);

        // userId, itemId,  categoryId,behavior,timestamp
        // 894923, 3076029, 1879194,   pv ,     1511658000
        DataStream<UserBehavior> userBehavior = SourceUtil.getUserBehaviorDataStream(env, CSV_NAME);
        userBehavior.assignTimestampsAndWatermarks(new GenerateWaterark(Time.seconds(5L)))
                .filter(new FilterFunction<UserBehavior>() {
                    @Override
                    public boolean filter(UserBehavior value) throws Exception {
                        return "pv".equals(value.getBehavior());
                    }
                })
                .keyBy("itemId")
                .timeWindow(Time.seconds(20000L), Time.seconds(5000L))
//                .timeWindow(Time.seconds(5L))
                .aggregate(new CountAGG(), new WindowResultFunction())
                .keyBy(new KeySelector<ItemViewCount, Long>() {
                    @Override
                    public Long getKey(ItemViewCount value) throws Exception {
                        return value.getWindowEnd();
                    }
                })
                .process(new TopNHotItems(3))
//                .apply(new WindowFunction<UserBehavior, UserBehavior, Tuple, TimeWindow>() {
//
//                    @Override
//                    public void apply(Tuple key, TimeWindow window, Iterable<UserBehavior> input, Collector<UserBehavior> out) throws Exception {
//                        for (UserBehavior user : input) {
//                            out.collect(user);
//                        }
//                    }
//                })
                .print();

        env.execute("hot top");
    }


    //********************************************************************************************************************
    // USER FUNCTION
    //********************************************************************************************************************

    private static class GenerateWaterark extends BoundedOutOfOrdernessTimestampExtractor<UserBehavior> {

        public GenerateWaterark(Time maxOutOfOrderness) {
            super(maxOutOfOrderness);
        }

        @Override
        public long extractTimestamp(UserBehavior element) {
            return element.getTimestamp() * 1000L;
        }
    }

    /**
     * 每来一条元素就加1
     */
    private static class CountAGG implements AggregateFunction<UserBehavior, SimpleAccumulator<Long>, Long> {

        @Override
        public SimpleAccumulator<Long> createAccumulator() {
            return new LongCounter();
        }

        @Override
        public SimpleAccumulator<Long> add(UserBehavior value, SimpleAccumulator<Long> accumulator) {
            accumulator.add(1L);
            return accumulator;
        }

        @Override
        public Long getResult(SimpleAccumulator<Long> accumulator) {
            return accumulator.getLocalValue();
        }

        @Override
        public SimpleAccumulator<Long> merge(SimpleAccumulator<Long> a, SimpleAccumulator<Long> b) {
            a.merge(b);
            return a;
        }
    }

}
