package Util;

import main.HotItems;
import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.api.java.io.PojoCsvInputFormat;
import org.apache.flink.api.java.typeutils.PojoTypeInfo;
import org.apache.flink.api.java.typeutils.TypeExtractor;
import org.apache.flink.core.fs.Path;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import pojo.UserBehavior;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;

/**
 * @author: yang
 * @date: 2019/7/24
 */
public class SourceUtil {

    /**
     * 创建CSV格式的{@link DataStream}
     *
     * @param env
     * @return
     */
    public static DataStream<UserBehavior> getUserBehaviorDataStream(StreamExecutionEnvironment env, String CSV_NAME) throws URISyntaxException {
        PojoTypeInfo<UserBehavior> pojoType = (PojoTypeInfo<UserBehavior>) TypeExtractor.createTypeInfo(UserBehavior.class);

        String[] fieldsName = new String[]{"userId", "itemId", "categoryId", "behavior", "timestamp"};
        PojoCsvInputFormat<UserBehavior> csvInputFormat = new PojoCsvInputFormat(findCSVPath(CSV_NAME), pojoType, fieldsName);

        return env.createInput(csvInputFormat, TypeInformation.of(UserBehavior.class));
    }

    /**
     * 获取CSV路径
     *
     * @param CSVname
     * @return
     * @throws URISyntaxException
     */
    private static Path findCSVPath(String CSVname) throws URISyntaxException {
        URL url = HotItems.class.getClassLoader().getResource(CSVname);
        return Path.fromLocalFile(new File(url.toURI()));
    }
}
