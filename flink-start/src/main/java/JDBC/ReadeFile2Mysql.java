package JDBC;

import JDBC.util.JDBCInfo;
import org.apache.flink.api.common.JobExecutionResult;
import org.apache.flink.api.common.accumulators.LongCounter;
import org.apache.flink.api.common.functions.RichMapFunction;
import org.apache.flink.api.java.DataSet;
import org.apache.flink.api.java.ExecutionEnvironment;
import org.apache.flink.api.java.io.jdbc.JDBCAppendTableSink;
import org.apache.flink.api.java.operators.DataSource;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.types.Row;

import java.math.BigDecimal;
import java.sql.Types;

/**
 * 读取text文件写入数据库
 *
 * @author: Mr.Yang
 * @create: 2019-07-17
 */
public class ReadeFile2Mysql {

	private static final String PATH = "C:\\Users\\31472\\Desktop\\goods.txt";
	private static final int[] SQLTYPES = new int[]{Types.VARCHAR, Types.DECIMAL, Types.INTEGER, Types.INTEGER};
	private static final String ACCUMULATOR_NAME = "CountRows";

	public static void main(String[] args) throws Exception {
		final ExecutionEnvironment env = ExecutionEnvironment.getExecutionEnvironment();
		env.setParallelism(1);

		DataSource<String> file = env.readTextFile(PATH, "utf-8");

		// 转换成sink需要的格式 row
		DataSet<Row> rows = file.map(new GenerateRow());

		final JDBCAppendTableSink jdbcSink = createJDBCSink();
		jdbcSink.emitDataSet(rows);

		JobExecutionResult result = env.execute("reade_file_2_mysql");
		Long size = (Long) result.getAccumulatorResult(ACCUMULATOR_NAME);
		System.out.println("读取文件行数为：" + size);
	}

	/**
	 * 创建jdbc的sink
	 *
	 * @return
	 */
	private static JDBCAppendTableSink createJDBCSink() {
		JDBCAppendTableSink sink = JDBCAppendTableSink.builder()
				.setDrivername(JDBCInfo.DRIVER_NAME)
				.setUsername(JDBCInfo.USER_NAME)
				.setPassword(JDBCInfo.PASS_WORD)
				.setDBUrl(JDBCInfo.URL)
				.setQuery("insert into testdata.goods(goodsName, sellingPrice, goodsStock, appraiseNum) values(?,?,?,?)")
				.setParameterTypes(SQLTYPES)
				.build();

		return sink;
	}


	//************************************************************************************************************
	// USER FUNCTION
	//************************************************************************************************************

	/**
	 * 将text的一行转化为 {@link Row},并使用累加器统计读取到的行数
	 */
	private static class GenerateRow extends RichMapFunction<String, Row> {

		private LongCounter counter = new LongCounter();

		@Override
		public void open(Configuration parameters) throws Exception {
			super.open(parameters);
			getRuntimeContext().addAccumulator(ACCUMULATOR_NAME, counter);
		}

		@Override
		public Row map(String value) throws Exception {

			String[] info = value.split(",");
			counter.add(1L);

			return Row.of(info[0], BigDecimal.valueOf(Double.valueOf(info[1])), Integer.valueOf(info[2]),
					Integer.valueOf(info[3]));
		}
	}

}
