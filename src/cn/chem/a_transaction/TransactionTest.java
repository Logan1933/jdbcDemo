package cn.chem.a_transaction;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Savepoint;

import org.junit.Test;

import cn.chem.utils.JDBCUtils;

public class TransactionTest {
	@Test
	public void demo5() {
		// 需求：向数据库中的emp表插入10000 条数据，(添加事务) 每隔500条设置一个回滚点。9876 制造异常，数据库保存多少条记录
		Connection conn = null;
		PreparedStatement stmt = null;

		Savepoint sp = null;
		try {
			// 获得连接
			conn = JDBCUtils.getConnection();
			// 开启事务
			conn.setAutoCommit(false);
			// 设置保存点
			sp = conn.setSavepoint();

			// 获得发送sql的对象
			String sql = "insert into emp(id,name,age) values(null, ?, ?)";
			stmt = conn.prepareStatement(sql);
			// 插入10000条
			for (int i = 1; i <= 10000; i++) {
				// 设置参数
				stmt.setString(1, "name" + i);
				stmt.setInt(2, i % 25 + 1);

				// 发送sql 执行
				stmt.executeUpdate();

				// 每隔500条设置一个回滚点
				if (i % 500 == 0) {
					sp = conn.setSavepoint();
				}

				// 在9876是制造异常
				if (i == 9876) {
					int j = 1 / 0;
				}

			}
			System.out.println("一切OK，事务提交");
			conn.commit();
		} catch (Exception e) {
			System.out.println("出现异常, 事务回滚");
			try {
				if (conn != null) {
					// 带保存点的事务回滚是把回滚点之后的所有操作全部撤销
					conn.rollback(sp);
				}
			} catch (SQLException e1) {
				e1.printStackTrace();
			}

			// 需要将回滚点以前的更新生效 所以需要在提交一次
			try {
				if (conn != null) {
					conn.commit();
				}
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
		} finally {
			JDBCUtils.release(conn, stmt);
		}
	}

	@Test
	public void demo4() {
		// 需求：向数据库中的emp表插入10000 条数据，(添加事务)
		Connection conn = null;
		PreparedStatement stmt = null;

		try {
			// 获得连接
			conn = JDBCUtils.getConnection();
			// 开启事务
			conn.setAutoCommit(false);

			// 获得发送sql的对象
			String sql = "insert into emp(id,name,age) values(null, ?, ?)";
			stmt = conn.prepareStatement(sql);
			// 插入10000条
			for (int i = 1; i <= 10000; i++) {
				// 设置参数
				stmt.setString(1, "name" + i);
				stmt.setInt(2, i % 25 + 1);

				// 发送sql 执行
				stmt.executeUpdate();
			}
			System.out.println("一切OK，事务提交");
			conn.commit();
		} catch (Exception e) {
			System.out.println("出现异常, 事务回滚");
			try {
				if (conn != null) {
					conn.rollback();
				}
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
		} finally {
			JDBCUtils.release(conn, stmt);
		}
	}

	@Test
	public void demo3() {
		// 需求：向数据库中的emp表插入10000 条数据，
		Connection conn = null;
		PreparedStatement stmt = null;

		try {
			// 获得连接
			conn = JDBCUtils.getConnection();
			// 获得发送sql的对象
			String sql = "insert into emp(id,name,age) values(null, ?, ?)";
			stmt = conn.prepareStatement(sql);
			// 插入10000条
			for (int i = 1; i <= 10; i++) {
				// 设置参数
				stmt.setString(1, "name" + i);
				stmt.setInt(2, i % 25 + 1);

				// 发送sql 执行
				stmt.executeUpdate();
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			JDBCUtils.release(conn, stmt);
		}
	}

	@Test
	public void demo2() {
		// 需求：a 向 b 转账 100元。(未添加事务：)
		Connection conn = null;
		PreparedStatement stmt = null;

		try {
			// 获得连接
			conn = JDBCUtils.getConnection();
			// 开启事务 要在获得连接之后
			conn.setAutoCommit(false);
			// 获得发送sql的对象
			String sql = "update account set money=money-100 where name='a'"; // a-100
			stmt = conn.prepareStatement(sql);
			// 发送sql
			stmt.executeUpdate();

			// int j = 1 / 0;

			// 获得发送sql的对象
			String sql2 = "update account set money=money+100 where name='b'"; // b+100
			stmt = conn.prepareStatement(sql2);
			// 发送sql
			stmt.executeUpdate();

			System.out.println("一切OK 事务提交");
			conn.commit();
		} catch (Exception e) {
			System.out.println("出现异常 撤销操作 事务回滚");
			try {
				if (conn != null) {
					conn.rollback();
				}
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
		} finally {
			JDBCUtils.release(conn, stmt);
		}
	}

	@Test
	public void demo1() {
		// 需求：a 向 b 转账 100元。(未添加事务：)
		Connection conn = null;
		PreparedStatement stmt = null;

		try {
			// 获得连接
			conn = JDBCUtils.getConnection();
			// 获得发送sql的对象
			String sql = "update account set money=money-100 where name='a'"; // a-100
			stmt = conn.prepareStatement(sql);
			// 发送sql
			stmt.executeUpdate();

			// 获得发送sql的对象
			String sql2 = "update account set money=money+100 where name='b'"; // b+100
			stmt = conn.prepareStatement(sql2);
			// 发送sql
			stmt.executeUpdate();

			System.out.println("一切OK");

		} catch (Exception e) {
			System.out.println("出现异常");
			e.printStackTrace();
		} finally {
			JDBCUtils.release(conn, stmt);
		}
	}
}
