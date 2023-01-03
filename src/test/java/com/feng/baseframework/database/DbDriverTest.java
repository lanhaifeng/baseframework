package com.feng.baseframework.database;

import com.feng.baseframework.common.MockitoBaseTest;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.junit.Ignore;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;

/**
 * baseframework
 * 2019/8/29 18:47
 * 数据库驱动测试
 *
 * @author lanhaifeng
 * @since
 **/
public class DbDriverTest extends MockitoBaseTest {

	private static Logger logger = LoggerFactory.getLogger(DbDriverTest.class);
	private static final String url = "jdbc:mysql://192.168.230.206:3306/capaa";
	private static final String driverName = "com.mysql.jdbc.Driver";
	private static final String username = "asset";
	private static final String password = "hzmc321#";

	@Test
	@Ignore
	public void test() throws SQLException {
		try {
			Class.forName(driverName);
		} catch (Exception e) {
			logger.error(ExceptionUtils.getFullStackTrace(e));
		}
		String sql = "SELECT nextval('sq_my_sequence') as nextval";

		for (int i = 0; i < 100; i++) {
			Connection connection = DriverManager.getConnection(url, username, password);
			Statement preparedStatement = connection.createStatement();
			new SequenceThread(connection,preparedStatement,sql,i).start();
		}
	}

	public static void close(Connection connection, Statement preparedStatement){
		try{
			if(connection != null){
				connection.close();
			}
			if(preparedStatement != null){
				preparedStatement.close();
			}
		}catch (Exception e){
			logger.error(ExceptionUtils.getFullStackTrace(e));
		}
	}
}

class SequenceThread extends Thread{

	private Logger logger = LoggerFactory.getLogger(getClass());
	private Connection connection;
	private Statement preparedStatement;
	private String sql;
	private int index;

	public SequenceThread(Connection connection, Statement preparedStatement, String sql, int index){
		this.connection = connection;
		this.preparedStatement = preparedStatement;
		this.sql = sql;
		this.index = index;
	}

	@Override
	public void run() {
		try {
			System.out.println(index);
			Thread.sleep(1000l);
			ResultSet result = preparedStatement.executeQuery(sql);
			if(result.next()){
				logger.info(String.valueOf(result.getLong(1)));
			}
			DbDriverTest.close(connection, preparedStatement);
			System.out.println(index);
		} catch (Exception e) {
			logger.error(ExceptionUtils.getFullStackTrace(e));
		}
	}
}