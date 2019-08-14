package function;

import org.apache.flink.api.java.tuple.Tuple;
import org.apache.flink.api.java.tuple.Tuple1;
import org.apache.flink.streaming.api.functions.windowing.WindowFunction;
import org.apache.flink.streaming.api.windowing.windows.TimeWindow;
import org.apache.flink.util.Collector;
import pojo.ItemViewCount;
import pojo.UserBehavior;

/**
 * @author: yang
 * @date: 2019/7/24
 */
public class WindowResultFunction implements WindowFunction<Long, ItemViewCount, Tuple, TimeWindow> {
    @Override
    public void apply(Tuple key, TimeWindow window, Iterable<Long> input, Collector<ItemViewCount> out) throws Exception {
        out.collect(ItemViewCount.of(((Tuple1<Long>)key).f0, window.getEnd(),input.iterator().next()));
    }

}
