/*import static org.junit.Assert.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.BeforeClass;
import org.junit.Test;

import com.ieseljust.ad.myDBMS.ConnectionManager;
import com.ieseljust.ad.myDBMS.DatabaseUtils;

public class TestDatabaseUtils {
	
	public static Connection conn;
	
	@BeforeClass
	public static void setConnection() {
		ConnectionManager cm = new ConnectionManager("localhost","3306","root","1234");
		conn = cm.connectDBMS();
	}
	
	@Test
	public void getMapFromResultSet() throws SQLException {
		System.out.println(conn);
		List<Map<String,String>> testTable = new ArrayList<>();
		Map<String,String> map = new HashMap<>();
		Map<String,String> map2 = new HashMap<>();
		
		map2.put("dept_no", "d005");
		map2.put("dept_name", "Development");
		// dept_no dept_name
		// d009	Customer Service
		map.put("dept_no", "d009");
		map.put("dept_name", "Customer Service");
		testTable.add(map);
		testTable.add(map2);
		Statement st = conn.createStatement();
		ResultSet rs = st.executeQuery("select * from employees.departments d limit 2");
		
		List<Map<String,String>> table = DatabaseUtils.getMapFromResultSet(rs, new String[] {"dept_no","dept_name"});
		
		assertEquals(testTable, table);
	}
	
	@Test
	public void getMapFromResultSetNull() throws SQLException {
		System.out.println(conn);
		List<Map<String,String>> testTable = new ArrayList<>();
		Map<String,String> map = new HashMap<>();
		Map<String,String> map2 = new HashMap<>();
		
		map2.put("dept_no", "d005");
		map2.put("dept_name", "Development");
		// dept_no dept_name
		// d009	Customer Service
		map.put("dept_no", "d009");
		map.put("dept_name", "Customer Service");
		testTable.add(map);
		testTable.add(map2);
		Statement st = conn.createStatement();
		ResultSet rs = st.executeQuery("select * from employees.departments d limit 2");
		
		List<Map<String,String>> table = DatabaseUtils.getMapFromResultSet(rs);
		
		assertEquals(testTable, table);
	}
	
	@Test
	public void printResultSet() throws SQLException {
		List<Map<String,String>> testTable = new ArrayList<>();
		Map<String,String> map = new HashMap<>();
		Map<String,String> map2 = new HashMap<>();
		
		map2.put("dept_no", "d005");
		map2.put("dept_name", "Development");
		// dept_no dept_name
		// d009	Customer Service
		map.put("dept_no", "d009");
		map.put("dept_name", "Customer Service");
		testTable.add(map);
		testTable.add(map2);
		Statement st = conn.createStatement();
		ResultSet rs = st.executeQuery("select * from employees.departments d limit 2");
		DatabaseUtils.printResultSet(rs);
	}
	
	@Test
	public void printBigResultSet() throws SQLException {
		List<Map<String,String>> testTable = new ArrayList<>();
		Map<String,String> map = new HashMap<>();
		Map<String,String> map2 = new HashMap<>();
		
		map2.put("dept_no", "d005");
		map2.put("dept_name", "Development");
		// dept_no dept_name
		// d009	Customer Service
		map.put("dept_no", "d009");
		map.put("dept_name", "Customer Service");
		testTable.add(map);
		testTable.add(map2);
		Statement st = conn.createStatement();
		ResultSet rs = st.executeQuery("select * from employees.employees d limit 1000");
		DatabaseUtils.printResultSet(rs);
	}

}
*/