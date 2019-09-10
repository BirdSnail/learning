package training.yanghuadong.tableAPI;

import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.table.api.EnvironmentSettings;
import org.apache.flink.table.api.Table;
import org.apache.flink.table.api.java.StreamTableEnvironment;
import org.apache.flink.table.sinks.CsvTableSink;
import org.apache.flink.table.sinks.TableSink;
import org.apache.flink.table.sources.CsvTableSource;

import static training.yanghuadong.tableAPI.basic.TableTestBasic.*;

/**
 * Flink Table 程序基本结构
 *  1. 创建TableEnvironment
 *  2. 注册表
 *  3. 执行相关query
 *  4. 编辑 tableResult，或者发送到 Table Sink
 *
 * @author: Mr.Yang
 * @create: 2019-08-30
 */
public class SimpleFlinkTableTest {

	public static void main(String[] args) {

		// Create a TableEnvironment
		EnvironmentSettings fsSettings = EnvironmentSettings.newInstance().useOldPlanner().inStreamingMode().build();
		StreamExecutionEnvironment fsEnv = StreamExecutionEnvironment.getExecutionEnvironment();
		StreamTableEnvironment fsTableEnv = StreamTableEnvironment.create(fsEnv, fsSettings);

		// Register a TableSource
		CsvTableSource csvSource = new CsvTableSource(CSV_INPUT_PATH, filedNames, fieldTypes);
		fsTableEnv.registerTableSource("CsvTable", csvSource);

		// table query
		// 计算每种商品的购买人数
		Table csvTable = fsTableEnv.scan("CsvTable");
		Table countOfCategory = csvTable.filter("behavior == buy")
				.groupBy("categoryId")
				.select("categoryId, count(*) as count");

		// 使用sql的方式
		fsTableEnv.sqlQuery("select categoryId, count(*) " +
				"from csvTable" +
				"where behavior == buy" +
				"group by categoryId"
		);

		// register a TableSink
		TableSink csvSink = new CsvTableSink(CSV_OUTPUT_PATH);
		fsTableEnv.registerTableSink("CsvSinkTable", csvSink);

	}
}
