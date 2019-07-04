package window;

import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.streaming.api.TimeCharacteristic;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.windowing.assigners.TumblingEventTimeWindows;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.util.OutputTag;
import window.function.RejectElementMapFunction;
import window.function.Tokenizer;

/**
 * @author: Mr.Yang
 * @create: 2019-07-02
 */
public class SlideOutTest {

    public static OutputTag<String> rejectedWordsTag = new OutputTag<String>("rejected"){};

    public static void main(String[] args) throws Exception {
        final StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setStreamTimeCharacteristic(TimeCharacteristic.IngestionTime);
	    env.setParallelism(1);

	    SingleOutputStreamOperator<Tuple2<String, Integer>> tokened = env.fromElements(CountWindowTest2.WordCountData.WORDS)
			    .keyBy(word -> 0)
			    .process(new Tokenizer());

        tokened.keyBy(0)
                .window(TumblingEventTimeWindows.of(Time.seconds(3L)))
                .sum(1)
                .print();

        // 获取侧输出的元素
        tokened.getSideOutput(rejectedWordsTag)
                .map(new RejectElementMapFunction())
                .writeAsText("C:\\Users\\31472\\Desktop\\rejected");

        env.execute();
    }
}
