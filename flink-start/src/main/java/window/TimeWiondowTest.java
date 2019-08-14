package window;

import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.common.functions.ReduceFunction;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.source.RichParallelSourceFunction;
import org.apache.flink.streaming.api.functions.source.SourceFunction;
import org.apache.flink.streaming.api.windowing.time.Time;

import java.util.Random;

/**
 * @author: Mr.Yang
 * @create: 2019-06-19
 */
public class TimeWiondowTest {

    public static void main(String[] args) throws Exception {
        final StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        // DataStreamSource<String> streamSource = env.socketTextStream("bear", 10086);
        DataStreamSource<String> streamSource = env.addSource(new DataSource());
        streamSource.map(new Tuple2MapFunction())
                .keyBy(0)
                .timeWindow(Time.seconds(10L),Time.seconds(2L))
//                .timeWindow(Time.seconds(5L))
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
    // UTIL CLASS
    // *****************************************************************************************************************

    private static class Tuple2MapFunction implements MapFunction<String, Tuple2<String, Long>> {

        @Override
        public Tuple2<String, Long> map(String word) throws Exception {
            return Tuple2.of(word, 1L);
        }
    }

    private static  class DataSource extends RichParallelSourceFunction<String>{

        private volatile boolean running = true;
        private Random random = new Random();
        long count = 0;

        @Override
        public void run(SourceContext<String> ctx) throws Exception {
            while (running && count < 100){
                ctx.collect(getRandomChar());
                count++;
                Thread.sleep(2000L);
            }
        }

        @Override
        public void cancel() {
            running = false;
        }
    }

    private static String getRandomChar() {
        String str = String.valueOf((char) ('a' + new Random().nextInt(5)));
        System.out.println(str);
        return str;
    }
}
