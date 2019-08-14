package window.function;

import org.apache.flink.api.common.functions.MapFunction;

/**
 * @author: Mr.Yang
 * @create: 2019-07-02
 */
public class RejectElementMapFunction implements MapFunction<String, String> {

    @Override
    public String map(String value) throws Exception {
        return "rejected: " + value;
    }
}
