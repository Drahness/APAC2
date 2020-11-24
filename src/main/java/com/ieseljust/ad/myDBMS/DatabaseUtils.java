package com.ieseljust.ad.myDBMS;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DatabaseUtils {
	public static List<Map<String,String>> getMapFromResultSet(ResultSet rs, String[] columnNames) throws SQLException {
		if(columnNames == null) {
			ResultSetMetaData rsmt = rs.getMetaData();
			columnNames = new String[rsmt.getColumnCount()];
			for(int i = 1 ; i < columnNames.length ; i++) {
				columnNames[i-1] = rsmt.getColumnLabel(i);
			}
		}
		List<Map<String,String>> table = new ArrayList<>();
		while(rs.next()) {
			Map<String,String> map = new HashMap<>();
			for (String columnName : columnNames) {
				Object o = rs.getObject(columnName);
				if(o != null) {
					map.put(columnName, o.toString());
				}
			}
			table.add(map);
		}
		return table;
	}
	
	public static List<Map<String,String>> getMapFromResultSet(ResultSet rs) throws SQLException {
		ResultSetMetaData rsmt = rs.getMetaData();
		String[] columnNames = new String[rsmt.getColumnCount()];
		for(int i = 1 ; i < rsmt.getColumnCount()+1 ; i++) {
			columnNames[i-1] = rsmt.getColumnLabel(i);
		}
		return getMapFromResultSet(rs,columnNames);
	}
	
	public static void printFormattedTable(List<Map<String,String>> table) {
		String[] keys ;
		if(!table.isEmpty()) {
			keys = table.get(0).keySet().toArray(new String[0]);
		} else {
			System.out.println(ConsoleColors.getErrorColored("La consulta no ha arrojado ningun resultado."));
			return;
		}
		int[] sizes = new int[keys.length];
		for (int i = 0; i < keys.length; i++) {
			sizes[i] = getMaxLenghtOfColumn(table, keys[i]);
		}

		StringBuilder sb = new StringBuilder();
		sb.append("|");
		for (int i = 0; i < keys.length; i++) {
			sb.append(ConsoleColors.COLORS[i%12])
				.append(String.format("%-"+sizes[i]+"s", keys[i]))
				.append(ConsoleColors.RESET)
				.append("|");
		}
		System.out.println(sb);
		sb = new StringBuilder();
		sb.append("|");
		for (int i = 0; i < keys.length; i++) {
			sb.append(String.format("%-"+sizes[i]+"s", " ")).append("|");
		}
		
		System.out.println((sb.toString().replace(" ", "-")));
		for (Map<String, String> map : table) {
			printRow(map, keys, sizes);
		}
	}
	
	public static int getMaxLenghtOfColumn(List<Map<String,String>> table, String column) {
		int max = column.length();
		for (Map<String, String> map : table) {
			String obj = map.get(column);
			if(obj != null) {
				int size = map.get(column).length();
				if(map.get(column).length() > max) {
					max = size;
				}
			}
		}
		return max;
	}
	
	private static void printRow(Map<String,String> row,String[] keys, int[] sizes) {
		StringBuilder sb = new StringBuilder();
		sb.append("|");
		for (int i = 0; i < keys.length; i++) {
			sb.append(ConsoleColors.COLORS[i%12])
				.append(String.format("%-"+sizes[i]+"s", row.get(keys[i])))
				.append(ConsoleColors.RESET)
				.append("|");
		}
		System.out.println(sb);
	}
	
	public static void printResultSet(ResultSet rs) {
		try {
			List<Map<String,String>> table = getMapFromResultSet(rs);
			printFormattedTable(table);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public static void printTableMetadata(Connection conn, String catalog ,String table, boolean b) throws SQLException {
		List<Map<String,String>> listTable = new TableMetadata(conn.getMetaData(),catalog,table).getMetadata(b);
		printFormattedTable(listTable);
	}
}
