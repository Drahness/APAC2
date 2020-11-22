package com.ieseljust.ad.myDBMS;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class SQLExecutor {
	private Connection conn;
	private String sql;
	public SQLExecutor(Connection conn, String sql) {
		this.conn = conn;
		this.sql = sql;
	}
	
	public boolean execute() throws SQLException {
		return this.execute(sql);
	}
	
	public ResultSet executeQuery() throws SQLException {
		return this.executeQuery(sql);
	}
	
	public int executeUpdate() throws SQLException {
		return this.executeUpdate(sql);
	}
	
	public boolean execute(String sql) throws SQLException {
		Statement st = conn.createStatement();
		return st.execute(sql);
	}
	
	public ResultSet executeQuery(String sql) throws SQLException {
		Statement st = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
		return st.executeQuery(sql);
	}
	
	public int executeUpdate(String sql) throws SQLException {
		Statement st = conn.createStatement();
		return st.executeUpdate(sql);
	}
}
