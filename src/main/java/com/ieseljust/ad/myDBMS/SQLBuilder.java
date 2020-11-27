package com.ieseljust.ad.myDBMS;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class SQLBuilder {
	private Connection conn;
	private String sql;
	private int concurrency = ResultSet.CONCUR_READ_ONLY;
	private int scroll_sensitivity = ResultSet.TYPE_SCROLL_INSENSITIVE;
	
	public SQLBuilder setConcurrency(int concurrency) {
		this.concurrency = concurrency;
		return this;
	}
	
	public SQLBuilder setScroll_Sensitivity(int scroll_sensitivity) {
		this.scroll_sensitivity = scroll_sensitivity;
		return this;
	}
	public SQLBuilder setSQL(String sql) {
		this.sql = sql;
		return this;
	}
	public SQLBuilder setConnection(Connection conn) {
		this.conn = conn;
		return this;
	}
	public SQLBuilder() {}
	
	public SQLBuilder(Connection conn, String sql) {
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
		Statement st = conn.createStatement(scroll_sensitivity,concurrency);
		return st.executeQuery(sql);
	}
	
	public int executeUpdate(String sql) throws SQLException {
		Statement st = conn.createStatement();
		return st.executeUpdate(sql);
	}
	
}
