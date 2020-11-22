package com.ieseljust.ad.myDBMS;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
// Imports per a entrada de dades
import java.util.Scanner;

public class DBMan {
    /*
    Esta és la classe llançadora de l'aplicació
    Conté el mètode main que recull la informació del servidor
    i inicia una instància de connectionManager per 
    gestionar les connexions
    */
    
    public static void main(String[] args) throws SQLException{
    	//test();
        ConnectionManager cm;
        Scanner keyboard = new Scanner(System.in);
        String user, pass, ip, port;
        do {
            System.out.print(ConsoleColors.GREEN_BOLD_BRIGHT+"# Server address: "+ConsoleColors.RESET);
            ip = keyboard.nextLine();
            System.out.print(ConsoleColors.GREEN_BOLD_BRIGHT+"# Port: "+ConsoleColors.RESET);
            port = keyboard.nextLine();
            System.out.print(ConsoleColors.GREEN_BOLD_BRIGHT+"# Username: "+ConsoleColors.RESET);
            user = keyboard.nextLine();
            System.out.print(ConsoleColors.GREEN_BOLD_BRIGHT+"# Password: "+ConsoleColors.BLACK);
            pass = keyboard.nextLine();
            System.out.print(ConsoleColors.RESET);
            cm = new ConnectionManager(ip, port, user, pass);
        } while(cm.connectDBMS()==null);
        cm.startShell();
        keyboard.close();
    }

    public static void test() throws SQLException {
		Properties prop = new Properties();
		prop.put("user", "root");
		prop.put("password", "1234");
		prop.put("serverTimezone", "UTC");
    	Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/employees",prop);
    	ResultSet rs = conn.getMetaData().getTables(null, "employees", null, null);
    	ResultSetMetaData rsm = rs.getMetaData();
    	String[] columnNames = new String[rsm.getColumnCount()];
    	for (int i = 1; i < 1+columnNames.length ; i++) {
    		System.out.println(rsm.getColumnName(i));
    		columnNames[i-1] = rsm.getColumnName(i);
			
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
		System.out.println(table);
		//return table;
    }
}
