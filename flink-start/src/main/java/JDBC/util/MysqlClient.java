package JDBC.util;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author: Mr.Yang
 * @create: 2019-08-22
 */
public class MysqlClient {

	private String username;
	private String password;
	private Connection connection;

	public MysqlClient(String username, String password) throws ClassNotFoundException {
		this.username = username;
		this.password = password;
		Class.forName(JDBCInfo.DRIVER_NAME);
	}

	/**
	 * 根据传入的url获取connection
	 * @param url
	 * @return
	 */
	public Connection getConnection(String url) {
		try {
			connection = DriverManager.getConnection(url, username, password);
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return connection;
	}

	/**获取已有的connection*/
	public Connection getConnection() {
		if (connection == null) {
			throw new RuntimeException("Connection is not initialization");
		}
		return connection;
	}

	public void close() {
		if (connection != null) {
			try {
				connection.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

}
