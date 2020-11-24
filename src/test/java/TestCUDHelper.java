import static org.junit.Assert.*;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Timestamp;
import java.sql.Types;
import java.sql.Date;
import java.util.Properties;
import java.util.Random;

import org.junit.BeforeClass;
import org.junit.Test;

import com.ieseljust.ad.myDBMS.DatabaseUtils;
import com.ieseljust.ad.myDBMS.CUDHelper;
import com.ieseljust.ad.myDBMS.SQLExecutor;

public class TestCUDHelper {
	public static Connection conn;
	@BeforeClass
	public static void test() throws SQLException {
		Properties prop = new Properties();
		prop.put("user", "root");
		prop.put("password", "1234");
		prop.put("serverTimezone", "UTC");
		prop.put("generateSimpleParameterMetadata", "true");
    	conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/employees",prop);
	}
	@Test
	public void testSetValueMethod() throws Exception {
		
		CUDHelper rsm = new CUDHelper(conn ,new SQLExecutor(conn, "select * from employees.employees e;").executeQuery());
		System.out.println(rsm.getInsertStatement());
		DatabaseUtils.printFormattedTable(rsm.getMap());
		PreparedStatement ps = conn.prepareStatement(rsm.getInsertStatement());
		byte[] bytes = new byte[6541];
		new Random().nextBytes(bytes);
		
		// emp_no|birth_date|first_name |last_name     |gender|hire_date |
		rsm.setValue(ps,  Types.INTEGER, 1, 1);
		rsm.setValue(ps, Types.DATE, 2, 2);
		rsm.setValue(ps, Types.VARCHAR, 3, 3);
		rsm.setValue(ps,  Types.VARCHAR, 4, 4);
		rsm.setValue(ps,  Types.VARCHAR, 5, 5);
		rsm.setValue(ps,  Types.DATE, 6, 6);
		

		ps.executeUpdate();
	}
}
