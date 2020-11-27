package com.ieseljust.ad.myDBMS;

import java.sql.*;
import java.util.Properties;

class DatabaseManager implements Shell {

	private String server;
	private String port;
	private String user;
	private String pass;
	private String dbname;
	private Connection connection;

	public DatabaseManager() throws SQLException {
		this("locahost", "3306", "root", "root", "sys");
	}

	DatabaseManager(String server, String port, String user, String pass, String dbname) throws SQLException {
		// TO-DO: Inicialització dels atributs de la classe
		// amb els valors indicats

		this.server = server;
		this.port = port;
		this.user = user;
		this.pass = pass;
		this.dbname = dbname;
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
		connection = DriverManager.getConnection(String.format("jdbc:mysql://%s:%s/%s", server, port, dbname), prop);
		return connection;
	}

	public Connection connectDatabase() throws SQLException {
		SQLException expected = null;
		try {
			Properties prop = new Properties();
			prop.put("user", user);
			prop.put("password", pass);
			connection = DriverManager.getConnection(String.format("jdbc:mysql://%s:%s/%s", server, port, dbname), prop);
			return connection;
		} catch (SQLException e) {
			expected = e;
		}
		if (expected.toString().contains("romance'")) {
			connection = tryAgainConnectDBMS();
			return connection;
		}
		else {
			throw expected;
		}
	}

	public void showTables() {
		try {
			DatabaseUtils.printResultSet(this.connection.getMetaData().getTables(this.dbname, null, null, null));
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void insertIntoTable(String table) {
		// TO-DO: Afig informació a la taula indicada

		// Passos
		// 1. Estableix la connexió amb la BD
		// 2. Obtenim les columnes que formen la taula (ens interessa el nom de la
		// columna i el tipus de dada)
		// 3. Demanem a l'usuari el valor per a cada columna de la taula
		// 4. Construim la sentència d'inserció a partir de les dades obtingudes
		// i els valors proporcionats per l'usuari

		// Caldrà tenir en compte:
		// - Els tipus de dada de cada camp
		// - Si es tracta de columnes generades automàticament per la BD
		// (Autoincrement)
		// i no demanar-les
		// - Gestionar els diferents errors
		// - Si la clau primària de la taula és autoincremental, que ens mostre el
		// valor d'aquesta quan acabe.
		try {
			CUDHelper cud = new CUDHelper(connection,dbname,table);
			cud.executeInsertUser();
		} catch(SQLException e) {
			ConsoleColors.printError("Error SQL, no se ha podido insertar\n\t"+ e);
		}
	}

	public void showDescTable(String table) {
		// TO-DO: Mostra la descripció de la taula indicada,
		// mostrant: nom, tipus de dada i si pot tindre valor no nul
		// Informeu també de les Claus Primàries i externes
		this.showDescTable(table, false);
	}
	private void showDescTable(String table, boolean seeAll) {
		try {
			DatabaseUtils.printTableMetadata(this.connection, this.dbname, table, seeAll);
		} catch (SQLException e) {
			ConsoleColors.staticPrintColoredString(String.format("Error al mostrar la descripcio de la taula %s.\n\t",table) + e, ConsoleColors.RED);
		}	
	}

	@Override
	public void printHelp() {
		System.out.println("Commands: ");
		System.out.println("	[sh tables | show tables] -> Show the tables");
		System.out.println("	[describe | desc] tablename -> Describe the fields of a table");
		System.out.println("	[insert] tablename -> Start the inserting client");
		System.out.println("	[select] ... -> Executes the statement.");
		System.out.println("	Quit ->");
		
	}
	public boolean startShell(String command) {
		switch (command) {
		case "sh tables":
		case "show tables":
			this.showTables();
		case "quit":
			break;
		default:
			String[] subcommand = command.split(" ");
			switch (subcommand[0]) {
			case "describe": case "desc":
				boolean seeAll = false;
				if(subcommand.length > 2 && subcommand[2].equals("-A")) {
					seeAll = true;
					this.showDescTable(subcommand[1],true);
				} else {
					this.showDescTable(subcommand[1]);
				}
				break;
			case "insert":
				this.insertIntoTable(subcommand[1]);
				break;
			case "select":
				this.executeSelect(command);
				break;
			default:
				System.out.println(ConsoleColors.RED + "Unknown db option" + ConsoleColors.RESET);
				return false;
			}
		}
		return true;
	}
	@Override
	public String getShellString() {
		return ConsoleColors.GREEN_BOLD_BRIGHT + "# (" + this.user + ") on " + this.server + ":" + this.port + "["
				+ this.dbname + "] > " + ConsoleColors.RESET;
	}
	
	public void executeSelect(String query) {
		try {
			DatabaseUtils.printResultSet(new SQLBuilder(this.connection, query).executeQuery());
		} catch (SQLException e) {
			ConsoleColors.staticPrintColoredString("Error en la consulta: \n\t"+e, ConsoleColors.RED);
		}
	}
}