package training.yanghuadong.tableAPI.basic;

import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.api.common.typeinfo.Types;

/**
 * @author: Mr.Yang
 * @create: 2019-08-30
 */
public class TableTestBasic {

	public static final String CSV_INPUT_PATH = "D:\\BigData\\testdata\\UserBehavior.csv";

	public static String[] filedNames = new String[]{"userId", "itemId", "categoryId", "behavior", "timeStamp"};

	public static TypeInformation[] fieldTypes = new TypeInformation[]{Types.LONG, Types.LONG, Types.INT, Types.STRING, Types.LONG};

	public static final String CSV_OUTPUT_PATH = "D:\\BigData\\testdata\\UserBehaviorResult.csv";
}
