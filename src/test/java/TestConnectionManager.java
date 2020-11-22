import static org.junit.Assert.*;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

import org.junit.BeforeClass;
import org.junit.Test;

import com.ieseljust.ad.myDBMS.ConnectionManager;

public class TestConnectionManager {

	public static ConnectionManager cm;
	/*
	@BeforeClass
	public static void preinit() throws IOException {
		cm = new ConnectionManager("localhost","3306","root","1234");
		cm.connectDBMS() ;
		String createDatabase = "Create database if not exists testdata;";
		String createTable = "Create table if not exists testdata.testTable (col1 integer);";
		FileWriter fw = new FileWriter(new File("is.sql"));
		System.out.println("Writing...");
		fw.write(createDatabase);
		fw.write(createTable);
		fw.close();
		cm.startShell("import is.sql");
	}
	@Test
	public void testScriptRunner() {
		int[] tests = new int[1] {100,10000,100000}
				;
		boolean[] btests = new boolean[tests.length];
		
		Random r = new Random();
		StringBuilder sb = new StringBuilder();
		for (int arr = 0 ; arr < tests.length; arr++) {
			for (int i = 0; i < tests[arr]; i++) {
				sb.append("INSERT INTO testdata.testTable values (").append(r.nextInt()).append(");").append("\n");
			}
			try {
				FileWriter fw = new FileWriter(new File("is"+arr+".sql"));
				System.out.println("Writing...");
				fw.write(sb.toString());
				fw.close();
				btests[arr] = cm.startShell(String.format("import is%d.sql",arr));
				System.out.println("Done "+arr);
			} catch (IOException e) {
				btests[arr] = false;
				System.out.println("Error");
			}
		}
		assertTrue(allTrue(btests));
	}
	public static boolean allTrue(boolean[] b) {
		for (boolean c : b) {
			if(!c) {
				return c;
			}
		}
		return true;
		
	}
*/
}
