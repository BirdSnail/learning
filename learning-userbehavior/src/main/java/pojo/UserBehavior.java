package pojo;

import lombok.Data;

/**
 * 用户行为pojo
 * @author: yang
 * @date: 2019/7/23
 */
@Data
public class UserBehavior {
    /**
     * 用户ID
     */
    private long userId;
    /**
     * 商品ID
     */
    private long itemId;
    /**
     * 商品类目ID
     */
    private int categoryId;
    /**
     * 用户行为, 包括("pv", "buy", "cart", "fav")
     */
    private String behavior;
    /**
     * 行为发生的时间戳，单位秒
     */
    private long timestamp;
}
