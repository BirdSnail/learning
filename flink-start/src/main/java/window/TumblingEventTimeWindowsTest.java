package window;

import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.common.functions.ReduceFunction;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.streaming.api.TimeCharacteristic;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.windowing.assigners.TumblingEventTimeWindows;
import org.apache.flink.streaming.api.windowing.time.Time;
import watermark.MyTimestampsAndWatermarks;

/**
 * @author: Mr.Yang
 * @create: 2019-06-20
 */
public class TumblingEventTimeWindowsTest {

    public static void main(String[] args) throws Exception {
        final StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setStreamTimeCharacteristic(TimeCharacteristic.EventTime);

        DataStreamSource<String> socketSource = env.socketTextStream("bear", 10086);
        // 抽取时间·event-time，生成wartmark
        socketSource.assignTimestampsAndWatermarks(new MyTimestampsAndWatermarks(Time.milliseconds(2000)))
                .map(new Tuple2MapFunction())
                .keyBy(0)
                .window(TumblingEventTimeWindows.of(Time.seconds(10L)))
                .reduce(new ReduceFunction<Tuple2<String, Long>>() {
                    @Override
                    public Tuple2<String, Long> reduce(Tuple2<String, Long> t1, Tuple2<String, Long> t2) throws Exception {
                        return Tuple2.of(t1.f0, t1.f1 + t2.f1);
                    }
                })
                .print();
        env.execute("timewindow-job");
    }

    // *****************************************************************************************************************
    //                            UTIL CLASS
    // *****************************************************************************************************************

    private static class Tuple2MapFunction implements MapFunction<String, Tuple2<String, Long>> {

        @Override
        public Tuple2<String, Long> map(String word) throws Exception {
            System.out.println(word.split(" ")[0]);
            return Tuple2.of(word.split(" ")[1], 1L);
        }
    }
}
