package logger;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @Author yanghuadong
 * @DATE 2019/6/10 21:27
 */
public class HelloLog4j {

    public static void main(String[] args) {
        Log log = LogFactory.getLog(HelloLog4j.class);
        log.error("erro log");
    }
}
