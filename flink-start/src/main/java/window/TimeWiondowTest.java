package window;

import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.common.functions.ReduceFunction;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.windowing.time.Time;

/**
 * @author: Mr.Yang
 * @create: 2019-06-19
 */
public class TimeWiondowTest {

    public static void main(String[] args) throws Exception {
        final StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        DataStreamSource<String> streamSource = env.socketTextStream("bear", 10086);

        streamSource.map(new Tuple2MapFunction())
                .keyBy(0)
                // keyby分区过后是每个key里面元素的数量到达2个就触发运算
                .timeWindow(Time.seconds(10L),Time.seconds(2L))
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
            return Tuple2.of(word, 1L);
        }
    }
}
