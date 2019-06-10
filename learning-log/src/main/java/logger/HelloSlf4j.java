package logger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * Slf4j这是一个接口，不提供任何的实现。需要与其他的日志体系进行桥接使用。
 * 使用需要三层的依赖。
 *  1. 门面器 slf4j
 *  2. 桥接器 slf4j-log4j12
 *  3. 具体实现 log4j （桥接器会自己引入该依赖，一般不需要添加）
 *
 * 本例演示Slf4j与log4j桥接使用,
 *
 * @Author yanghuadong
 * @DATE 2019/6/10 21:50
 */
public class HelloSlf4j {

    private static Logger logger = LoggerFactory.getLogger(HelloLog4j.class);

    public static void main(String[] args) {
        logger.error("abc");
        logger.info("cd");
    }
}
