package JDBC;

import org.apache.flink.api.common.typeinfo.BasicTypeInfo;
import org.apache.flink.api.java.DataSet;
import org.apache.flink.api.java.ExecutionEnvironment;
import org.apache.flink.api.java.io.jdbc.JDBCInputFormat;
import org.apache.flink.api.java.io.jdbc.split.GenericParameterValuesProvider;
import org.apache.flink.api.java.typeutils.RowTypeInfo;
import org.apache.flink.types.Row;

import java.io.Serializable;

/**
 * @author: Mr.Yang
 * @create: 2019-07-17
 */
public class MysqlReaderOfFlink {

	private static final String USER_NAME = "root";
	private static final String PASS_WORD = "yhd19941108";
	private static final String DRIVER_NAME = "com.mysql.jdbc.Driver";
	private static final String URL = "jdbc:mysql://bear:3306/testdata?useUnicode=true&characterEncoding=utf8";
	private static final String SELECT_ALL_OF_PARAMETER = "select * from testdata.goods t where t.goodsName = ?";
	private static final RowTypeInfo TYPES =
										new RowTypeInfo(BasicTypeInfo.INT_TYPE_INFO,
												BasicTypeInfo.STRING_TYPE_INFO,
												BasicTypeInfo.BIG_DEC_TYPE_INFO,
												BasicTypeInfo.INT_TYPE_INFO,
												BasicTypeInfo.INT_TYPE_INFO);

	public static void main(String[] args) throws Exception {
		final ExecutionEnvironment env = ExecutionEnvironment.getExecutionEnvironment();

		DataSet<Row> input = createDataSourceFromMysql(env);
		input.print();

	}

	/**
	 * 创建mysql的数据源
	 * @param env
	 * @return
	 */
	private static DataSet<Row> createDataSourceFromMysql(ExecutionEnvironment env){
		// 预定义值
		Serializable[][] parameterValues = new Serializable[2][1];
		// 第一次查询传递的值
		parameterValues[0] = new String[]{"apple"};
		// 第二次传递的值
		parameterValues[1] = new String[]{"小米"};

		// JDBCInputFormat的创建
		JDBCInputFormat jdbcInputFormat = JDBCInputFormat.buildJDBCInputFormat()
				.setUsername(USER_NAME)
				.setPassword(PASS_WORD)
				.setDrivername(DRIVER_NAME)
				.setDBUrl(URL)
				.setQuery(SELECT_ALL_OF_PARAMETER)
				.setParametersProvider(new GenericParameterValuesProvider(parameterValues))
				.setRowTypeInfo(TYPES)
				.finish();

		return env.createInput(jdbcInputFormat);
	}
}
