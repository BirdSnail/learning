package watermark;

import org.apache.flink.streaming.api.functions.timestamps.BoundedOutOfOrdernessTimestampExtractor;
import org.apache.flink.streaming.api.windowing.time.Time;


/**
 * @author: Mr.Yang
 * @create: 2019-06-20
 */
public class MyTimestampsAndWatermarks extends BoundedOutOfOrdernessTimestampExtractor<String> {


    public MyTimestampsAndWatermarks(Time maxOutOfOrderness) {
        super(maxOutOfOrderness);
    }

    @Override
    public long extractTimestamp(String element) {
        return Long.parseLong(element.split(" ")[0]);
    }
}
