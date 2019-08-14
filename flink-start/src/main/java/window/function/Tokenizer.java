package window.function;

import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.streaming.api.functions.KeyedProcessFunction;
import org.apache.flink.util.Collector;
import window.SlideOutTest;

/**
 * @author: Mr.Yang
 * @create: 2019-07-02
 */
public class Tokenizer extends KeyedProcessFunction<Integer, String, Tuple2<String, Integer>> {
    @Override
    public void processElement(String value, Context ctx, Collector<Tuple2<String, Integer>> out) throws Exception {
        String[] tokens = value.toLowerCase().split("\\W+");

        // emit the pairs
        for (String token : tokens) {
            if (token.length() > 5) {
                ctx.output(SlideOutTest.rejectedWordsTag, token);
            } else if (token.length() > 0) {
                out.collect(new Tuple2<>(token, 1));
            }
        }
    }
}
