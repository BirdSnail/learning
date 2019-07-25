package pojo;

import lombok.Data;
import lombok.ToString;

/**
 * window聚合后的结果POJO
 *
 * @author: yang
 * @date: 2019/7/24
 */
@Data
@ToString
public class ItemViewCount {
    /**
     * 商品ID
     */
    private long itemId;
    /**
     * 窗口结束时间戳
     */
    private long windowEnd;
    /**
     * 商品的点击量
     */
    private long viewCount;

    public ItemViewCount() {
    }

    public ItemViewCount(Long itemId, Long windowEnd, Long viewCount) {
        this.itemId = itemId;
        this.windowEnd = windowEnd;
        this.viewCount = viewCount;
    }

    public static ItemViewCount of(Long itemId, Long windowEnd, Long viewCount) {
        return new ItemViewCount(itemId, windowEnd, viewCount);
    }
}
