package com.ieseljust.ad.myDBMS;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class ConnectionManager implements Shell {

	private String server;
	private String port;
	private String user;
	private String pass;
	private Connection connection;

	public ConnectionManager() {
		this("locahost", "3306", "root", "root");
	}

	public ConnectionManager(String server, String port, String user, String pass) {
		this.server = server;
		this.port = port;
		this.user = user;
		this.pass = pass;
	}

	public Connection connectDBMS() {
		try {
			System.out.println("Connecting...");
			Properties prop = new Properties();
			prop.put("user", user);
			prop.put("password", pass);
			connection = DriverManager.getConnection(String.format("jdbc:mysql://%s:%s", server, port), prop);
			return connection;
		} catch (SQLException e) {
			if (e.toString().contains("romance'")) {
				try {
					connection = tryAgainConnectDBMS();
					return connection;
				} catch (SQLException e1) {
					System.out.println(e.getCause());
				}
			}
			ConsoleColors.staticPrintColoredString(e.toString(), ConsoleColors.RED);
			return null;
		}
	}

	/**
	 * Esta funcion es para salvaguardar un error que me da cuando intenta
	 * conectarse a la BDO de MI CASA ni siquiera pasa en el portatil, para poder
	 * hacer testeos. Este es el error que me sale:
	 * <p>
	 * java.sql.SQLException: The server time zone value 'Hora est?ndar romance' is
	 * unrecognized or represents more than one time zone. You must configure either
	 * the server or JDBC driver (via the serverTimezone configuration property) to
	 * use a more specifc time zone value if you want to utilize time zone support.
	 * 
	 * @return
	 * @throws SQLException
	 */
	private Connection tryAgainConnectDBMS() throws SQLException {
		Properties prop = new Properties();
		prop.put("user", user);
		prop.put("password", pass);
		prop.put("serverTimezone", "UTC");
		connection = DriverManager.getConnection(String.format("jdbc:mysql://%s:%s", server, port), prop);
		return connection;
	}

	public void showInfo() {
		try {
			DatabaseMetaData metadata = connection.getMetaData();
			System.out.println(String.format("SGBD           : %s", metadata.getDatabaseProductName()));
			System.out.println(String.format("Driver         : %s", metadata.getDriverName()));
			System.out.println(String.format("URL de conexion: %s", metadata.getURL()));
			System.out.println(String.format("Usuari         : %s", metadata.getUserName()));
		} catch (SQLException e) {
			ConsoleColors.staticPrintColoredString("Error al mostrar la informacion relativa a la conexion.\n\t" + e, ConsoleColors.RED);
		}
	}

	public void showDatabases() {
		try {
			ResultSet rs = connection.getMetaData().getCatalogs();
			DatabaseUtils.printResultSet(rs);
		} catch (SQLException e) {
			ConsoleColors.staticPrintColoredString("Error al mostrar las bases de datos.\n\t" + e, ConsoleColors.RED);
		}

	}
	
	public boolean startShell(String command) {
		switch (command) {
		case "sh db":
		case "show databases":
			this.showDatabases();
			break;
		case "info":
			this.showInfo();
			break;
		case "quit":
			break;
		default:
			String[] subcommand = command.split(" ");
			if(subcommand.length > 2) {
				ConsoleColors.staticPrintColoredString(String.format("Los argumentos a partir de %s son ignorados.",subcommand[1]), ConsoleColors.RED);
			}
			switch (subcommand[0]) {
			case "use":
				try {
					DatabaseManager dbman = new DatabaseManager(server,port,user,pass,subcommand[1]);
					dbman.connectDatabase();
					dbman.startShell();
				} catch (SQLException e) {
					ConsoleColors.staticPrintColoredString(String.format("Error al conectar con la base de datos %s: \n\t"+e,subcommand[1]), ConsoleColors.RED);
				}
				break;
			case "import":
				importScript(subcommand[1]);
				break;
			default:
				System.out.println(ConsoleColors.RED + "Unknown option" + ConsoleColors.RESET);
				return false;
			}
		}
		return true;
	}
	
	private boolean importScript(String file) {
		File f = new File(file);
		if(f.exists()) {
			List<String> statementsInFile = getStringsFromFile(f);
			int i = 0;
			String orden = "";
			if(!statementsInFile.isEmpty()) {
				try {
					connection.setAutoCommit(false);
					for (i = 0 ; i < statementsInFile.size(); i++) {
						orden = statementsInFile.get(i);
						System.out.println("Orden N"+i+": "+orden);
						new SQLExecutor(this.connection, orden).execute();
					}
					connection.commit();
					connection.setAutoCommit(true);
					return true;
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					ConsoleColors.staticPrintColoredString(String.format("Error en orden N%d %s\n%s",i,orden,e.toString()), ConsoleColors.RED);
				}
			}
		}
		else {
			ConsoleColors.staticPrintColoredString(String.format("El archivo %s no existe",file), ConsoleColors.RED);
		}
		return false;
	}
	
	private List<String> getStringsFromFile(File f) {
		List<String> list = new ArrayList<>();
		StringBuilder sb = new StringBuilder();
		try {
			FileReader fr = new FileReader(f);
		
			int currentChar;
			while((currentChar = fr.read()) !=-1) {
				if(((char) currentChar) == '\n' || ((char) currentChar) == '\r' || ((char) currentChar) == '\t') {
					continue;
				}
				sb.append((char)currentChar);
				if(((char) currentChar) == ';') {
					list.add(sb.toString());
					sb = new StringBuilder();
				}
			}
			fr.close();
			if(sb.length() > 2) {
				list.add(sb.toString());
			}
		} catch (IOException e) {
			ConsoleColors.staticPrintColoredString("Error leyendo el archivo : "+f.getName() , ConsoleColors.RED);
		}
		return list;
	}

	@Override
	public String getShellString() {
		return ConsoleColors.GREEN_BOLD_BRIGHT + "# (" + this.user + ") on " + this.server + ":"
				+ this.port + "> " + ConsoleColors.RESET;
	}

}