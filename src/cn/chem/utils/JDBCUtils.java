package cn.chem.utils;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

public class JDBCUtils {
	static String driverClass;
	static String url;
	static String user;
	static String password;

	static {
		// 通过properties对象来加载外部的配置文件
		// 创建properties对象，加载外部配置文件的核心对象
		Properties prop = new Properties();
		try {
			// 加载外部的配置文件
			InputStream in = JDBCUtils.class.getClassLoader().getResourceAsStream("jdbc.properties");
			prop.load(in);
		} catch (IOException e) {
			e.printStackTrace();
		}
		// 读取配置文件内容
		driverClass = prop.getProperty("driverClass");
		url = prop.getProperty("url");
		user = prop.getProperty("user");
		password = prop.getProperty("password");

		// 注册驱动
		try {
			Class.forName(driverClass);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	// 获得连接
	public static Connection getConnection() throws Exception {
		// 获得连接
		Connection conn = DriverManager.getConnection(url, user, password);
		return conn;
	}

	// 释放资源
	public static void release(Connection conn, Statement stmt, ResultSet rs) {
		try {
			if (rs != null) {
				rs.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		rs = null;

		release(conn, stmt);
	}

	// 释放资源
	public static void release(Connection conn, Statement stmt) {

		try {
			if (stmt != null) {
				stmt.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		stmt = null;

		try {
			if (conn != null) {
				conn.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		conn = null;
	}

	public static void main(String[] args) {
		// 通过properties对象来加载外部的配置文件
		// 创建properties对象，加载外部配置文件的核心对象
		Properties prop = new Properties();
		try {
			// 加载外部的配置文件
			InputStream in = JDBCUtils.class.getClassLoader().getResourceAsStream("jdbc.properties");
			prop.load(in);
		} catch (IOException e) {
			e.printStackTrace();
		}
		// 读取配置文件内容
		String driverClass = prop.getProperty("driverClass");
		String url = prop.getProperty("url");
		String user = prop.getProperty("user");
		String password = prop.getProperty("password");
		System.out.println(driverClass);
		System.out.println(url);
	}
}
