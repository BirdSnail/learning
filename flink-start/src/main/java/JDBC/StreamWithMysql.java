package JDBC;

import JDBC.util.JDBCInfo;
import JDBC.util.MysqlClient;
import JDBC.util.TemplateAnalyzerUtil;
import lombok.Data;
import lombok.ToString;
import org.apache.flink.api.common.functions.RichMapFunction;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.source.SourceFunction;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Random;

import static JDBC.util.JDBCInfo.*;

/**
 * @author: Mr.Yang
 * @create: 2019-08-22
 */
public class StreamWithMysql {

	private static String[] names = {"apple", "小米", "华为", "applex", "apple1", "apple2", "apple3", "apple4"};
	private static Random random = new Random();

	public static void main(String[] args) throws Exception {
		final StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

		env.addSource(new SourceFunction<Phone>() {
			private boolean isRunning = true;
			private String template = "$Dic{area}\t$Dic{color}";

			@Override
			public void run(SourceContext<Phone> ctx) throws Exception {
				while (isRunning) {
					Thread.sleep(2000L);

					String[] profile = TemplateAnalyzerUtil.getTplAnalyzer("phone", template).analyse().split("\t");
					Phone phone = new Phone(names[random.nextInt(names.length)], profile[0], profile[1]);
					System.out.println(phone);

					ctx.collect(phone);
				}
			}

			@Override
			public void cancel() {
				isRunning = false;
			}
		})
				.map(new RichMapFunction<Phone, Phone>() {
					private Connection connection;
					private MysqlClient mysql;
					private PreparedStatement preStatement;

					@Override
					public void open(Configuration parameters) throws Exception {
						super.open(parameters);
						mysql = new MysqlClient(USER_NAME, PASS_WORD);
						connection = mysql.getConnection(URL);
						preStatement = connection.prepareStatement(SELECT_PRICE);
					}

					@Override
					public Phone map(Phone value) throws Exception {
						preStatement.setString(1,value.getName());
						double price = 0.0;
						try(ResultSet result = preStatement.executeQuery()){
							if (result.next()) {
								price = result.getDouble(1);
							}
						}
						value.setPrice(price);
						return value;
					}

					@Override
					public void close()  {
						if (preStatement != null) {
							try {
								preStatement.close();
							} catch (SQLException e) {
								e.printStackTrace();
							}
						}
						mysql.close();
					}
				})
				.print();

		env.execute();
	}

	// ==============================================================
	// SOURCE DATA
	// ==============================================================

	@Data
	@ToString
	private static class Phone{
		private String name;
		private String area;
		private String color;
		private double price;

		public Phone() {}

		public Phone(String name, String area, String color) {
			this.name = name;
			this.area = area;
			this.color = color;
		}
	}

}
