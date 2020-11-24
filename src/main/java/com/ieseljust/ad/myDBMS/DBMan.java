package com.ieseljust.ad.myDBMS;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
// Imports per a entrada de dades
import java.util.Scanner;

import com.mysql.cj.jdbc.JdbcConnection;

public class DBMan {
    /*
    Esta és la classe llançadora de l'aplicació
    Conté el mètode main que recull la informació del servidor
    i inicia una instància de connectionManager per 
    gestionar les connexions
    */
    
    public static void main(String[] args) throws SQLException{
    	test();
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
		prop.put("generateSimpleParameterMetadata", "true");
    	Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/employees",prop);
    	ResultSet rs = new SQLBuilder(conn, "select * from employees.employees e \r\n" + 
    			"right join employees.dept_emp de on de.emp_no = e.emp_no \r\n" + 
    			"left join employees.departments d on d.dept_no = de.dept_no  \nlimit 1").executeQuery();

    	DatabaseUtils.printResultSetMetadata(conn, rs);
  
		ResultSet keys = conn.getMetaData().getPrimaryKeys("employees", null , "employees");
		while(keys.next()) {
			System.out.println("   adsasd "+keys.getString("PK_NAME"));
		}
        /*Statement stmt = conn.createStatement();
        //Query to create a table with all the supported data types
        String query = "CREATE table testdata.testtypes("
           + "varchar_column VARCHAR( 20 ), "
           + "tinyint_column TINYINT, "
           + "text_column TEXT, "
           + "date_column DATE, "
           + "smallint_column SMALLINT, "
           + "mediumint_column MEDIUMINT, "
           + "int_column INT, "
           + "bigint_column BIGINT, "
           + "float_column FLOAT( 10, 2 ), "
           + "double_column DOUBLE, "
           + "decimal_column DECIMAL( 10, 2 ), "
           + "datetime_column DATETIME, "
           + "timestamp_column TIMESTAMP, "
           + "time_column TIME, "
           + "year_column YEAR, "
           + "char_column CHAR( 10 ), "
           + "tinyblob_column TINYBLOB, "
           + "tinytext_column TINYTEXT, "
           + "blob_column BLOB, "
           + "mediumblob_column MEDIUMBLOB, "
           + "mediumtext_column MEDIUMTEXT, "
           + "longblob_column LONGBLOB, "
           + "longtext_column LONGTEXT, "
           + "enum_column ENUM( '1', '2', '3' ), "
           + "set_column SET( '1', '2', '3' ), "
           + "bool_column BOOL, "
           + "binary_column BINARY( 20 ), "
           + "varbinary_column VARBINARY( 20 )"
        + ")";
        //Executing the query
        stmt.execute(query);
        System.out.println("Table created ........");*/
    	
DatabaseUtils.printResultSetMetadata(conn,    	new SQLBuilder(conn, "select * from testdata.testtypes t ;").executeQuery());
    	//System.out.println(new ResultSetMetadata(rs).getInsertStatement());
    	/*ResultSet rs = conn.getMetaData().getTables(null, "employees", null, null);
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
		System.out.println(table);*/
		//return table;

    }
}
