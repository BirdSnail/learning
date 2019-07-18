package JDBC.util;

import org.apache.flink.api.common.typeinfo.BasicTypeInfo;
import org.apache.flink.api.java.typeutils.RowTypeInfo;

/**
 * @author: Mr.Yang
 * @create: 2019-07-17
 */
public class JDBCInfo {
	public static final String USER_NAME = "root";
	public static final String PASS_WORD = "yhd19941108";
	public static final String DRIVER_NAME = "com.mysql.jdbc.Driver";
	public static final String URL = "jdbc:mysql://bear:3306/testdata?useUnicode=true&characterEncoding=utf8";
	public static final String SELECT_ALL_OF_PARAMETER = "select * from testdata.goods t where t.goodsName = ?";
	public static final RowTypeInfo TYPES =
			new RowTypeInfo(BasicTypeInfo.INT_TYPE_INFO,
					BasicTypeInfo.STRING_TYPE_INFO,
					BasicTypeInfo.BIG_DEC_TYPE_INFO,
					BasicTypeInfo.INT_TYPE_INFO,
					BasicTypeInfo.INT_TYPE_INFO);
}
