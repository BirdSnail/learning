package window.util;

import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.util.Collector;

/**
 * @author: Mr.Yang
 * @create: 2019-06-28
 */
public class SplitWordsFunction implements FlatMapFunction<String, Tuple2<String, Integer>> {
    @Override
    public void flatMap(String value, Collector<Tuple2<String, Integer>> out) throws Exception {
        // 拆分单词
        String[] tokens = value.toLowerCase().split("\\W+");

        for (String word : tokens) {
            if (word.length() > 0) {
                System.out.println("===" + word + "===");
                out.collect(Tuple2.of(word, 1));
            }
        }
    }
}
