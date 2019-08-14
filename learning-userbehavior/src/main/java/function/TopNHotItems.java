package function;

import org.apache.flink.api.common.state.ListState;
import org.apache.flink.api.common.state.ListStateDescriptor;
import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.functions.KeyedProcessFunction;
import org.apache.flink.util.Collector;
import pojo.ItemViewCount;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: yang
 * @date: 2019/7/24
 */
public class TopNHotItems extends KeyedProcessFunction<Long, ItemViewCount, Tuple2<Long, Long>> {

    private ListState<ItemViewCount> itemViewCountListState;
    private int topSize;

    public TopNHotItems(int topSize) {
        this.topSize = topSize;
    }

    @Override
    public void open(Configuration parameters) throws Exception {
        super.open(parameters);
        itemViewCountListState = getRuntimeContext().getListState(
                new ListStateDescriptor<ItemViewCount>("item-state", TypeInformation.of(ItemViewCount.class)));
    }

    @Override
    public void processElement(ItemViewCount value, Context ctx, Collector<Tuple2<Long, Long>> out) throws Exception {
        itemViewCountListState.add(value);
        // 被触发市意味着收到了timestamp超过windowEnd+1的watermark，即收集齐了该windowEnd下所有窗口的值
        ctx.timerService().registerEventTimeTimer(value.getWindowEnd() + 1);
    }

    @Override
    public void onTimer(long timestamp, OnTimerContext ctx, Collector<Tuple2<Long, Long>> out) throws Exception {
        Iterable<ItemViewCount> itemViewCounts = itemViewCountListState.get();
        itemViewCountListState.clear();

        List<ItemViewCount> itemViewCountList = new ArrayList<>();
        for (ItemViewCount itemViewCount : itemViewCounts) {
            itemViewCountList.add(itemViewCount);
        }

        // sort
        itemViewCountList.sort((item1, item2) -> (int) (item2.getViewCount() - item1.getViewCount()));

        for (int i = 0; i < topSize && i < itemViewCountList.size(); i++) {
            out.collect(Tuple2.of(itemViewCountList.get(i).getItemId(), itemViewCountList.get(i).getViewCount()));
        }

        System.out.println("=======================================================================\n\n");
    }
}
