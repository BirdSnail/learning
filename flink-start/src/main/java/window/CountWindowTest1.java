package window;

import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.common.functions.ReduceFunction;
import org.apache.flink.api.java.operators.DataSource;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

/**
 * @Author yanghuadong
 * @DATE 2019/6/18 20:40
 */
public class CountWindowTest1 {
    public static void main(String[] args) throws Exception {
        final StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        DataStreamSource<String> streamSource = env.socketTextStream("localhost", 10086);

        streamSource.map(new Tuple2MapFunction())
                .keyBy(0)
                // keyby分区过后是每个key里面元素的数量到达5个时才触发运算
                .countWindow(5)
                .reduce(new ReduceFunction<Tuple2<String, Long>>() {
                    @Override
                    public Tuple2<String, Long> reduce(Tuple2<String, Long> t1, Tuple2<String, Long> t2) throws Exception {
                        return Tuple2.of(t1.f0, t1.f1 + t2.f1);
                    }
                })
                .print();
        env.execute("countwindow-job");
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

