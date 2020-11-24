package com.ieseljust.ad.myDBMS;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.HashMap;
import java.util.Map;

public class CUDHelper {
	public static Map<Integer,String> types;
	public static Map<Integer,String> nullability;
	public static Map<String,Integer> columnsMetadataIDs;
	private Connection conn;
	private String catalog;
	private String tablename;
	private TableMetadata metadata;
	
	public int executeInsertUser() throws SQLException {
		PreparedStatement ps = conn.prepareStatement(getInsertStatement(),Statement.RETURN_GENERATED_KEYS);
		int parameterIndex = 1;
		for (int i = 0; i < metadata.getColumnCount(); i++) {
			if(fieldIsInsertable(i)) {
				this.setValue(ps, metadata.getDataType(i), parameterIndex, i);
				parameterIndex++;
			}
		}
		int rows = ps.executeUpdate();
		if(metadata.isShowPK()) {
			ResultSet ids = ps.getGeneratedKeys();
			while (ids.next()) {
				ConsoleColors.staticPrintColoredString("ID: "+ids.getInt(1),ConsoleColors.GREEN_BOLD_BRIGHT);
			}
		}
		return rows;
	}
	
	public int executeInsert(int[] dataTypes, Object...values) {
		if(values.length != dataTypes.length) {
			throw new RuntimeException("You forgot something? Array sizes are not equal");
		}
		return 0;
	}
	
	private String getInsertStatement() {
		StringBuilder sb = new StringBuilder();
		sb.append("INSERT INTO ").append(catalog).append(".").append(tablename);
		sb.append(getParametrizedText());
		return sb.toString();
	}
	
	private String getParametrizedText() {
		StringBuilder interrogants = new StringBuilder();
		StringBuilder col_names = new StringBuilder();
		for (int i = 0 ; i < metadata.getColumnCount(); i++) {
			if(fieldIsInsertable(i)) {
				if(interrogants.length()==0) {
					col_names.append(' ').append('(').append(metadata.getColumnName(i));
					interrogants.append(' ').append('(').append('?');
				} else {
					col_names.append(',').append(' ').append(metadata.getColumnName(i));
					interrogants.append(',').append(' ').append('?');
				}
			}
		}
		col_names.append(')');
		interrogants.append(')');
		return col_names.toString() +" \nVALUES " +interrogants.toString();
	}
	
	
	
	public boolean fieldIsInsertable(int i) {
		return !metadata.isAutoIncremental(i);
	}
	
	public CUDHelper(Connection conn ,String catalog, String table) throws SQLException {
		this.catalog = catalog;
		this.metadata = new TableMetadata(conn.getMetaData(), catalog, table);
		this.tablename = table;
		this.conn = conn;
	}

	
	static {
		types = new HashMap<>();
		types.put(Types.BIT,"BIT");
		types.put(Types.TINYINT, "TINYINT");
		types.put(Types.SMALLINT, "SMALLINT");
		types.put(Types.INTEGER, "INTEGER");
		types.put(Types.BIGINT, "BIGINT");
		types.put(Types.FLOAT, "FLOAT");
		types.put(Types.REAL, "REAL");
		types.put(Types.DOUBLE, "DOUBLE");
		types.put(Types.NUMERIC, "NUMERIC");
		types.put(Types.DECIMAL, "DECIMAL");
		types.put(Types.CHAR, "CHAR");
		types.put(Types.VARCHAR, "VARCHAR");
		types.put(Types.LONGVARCHAR, "LONGVARCHAR");
		types.put(Types.DATE, "DATE");
		types.put(Types.TIME, "TIME");
		types.put(Types.TIMESTAMP, "TIMESTAMP");
		types.put(Types.BINARY, "BINARY");
		types.put(Types.VARBINARY, "VARBINARY");
		types.put(Types.LONGVARBINARY, "LONGVARBINARY");
		types.put(Types.NULL, "NULL");
		types.put(Types.OTHER, "OTHER");
		types.put(Types.JAVA_OBJECT, "JAVA_OBJECT");
		types.put(Types.DISTINCT, "DISTINCT");
		types.put(Types.STRUCT, "STRUCT");
		types.put(Types.ARRAY, "ARRAY");
		types.put(Types.BLOB, "BLOB");
		types.put(Types.CLOB, "CLOB");
		types.put(Types.REF, "REF");
		types.put(Types.DATALINK, "DATALINK");
		types.put(Types.BOOLEAN, "BOOLEAN");
		types.put(Types.ROWID, "ROWID");
		types.put(Types.NCHAR, "NCHAR");
		types.put(Types.NVARCHAR, "NVARCHAR");
		types.put(Types.LONGNVARCHAR, "LONGNVARCHAR");
		types.put(Types.NCLOB, "NCLOB");
		types.put(Types.SQLXML, "SQLXML");
		types.put(Types.REF_CURSOR, "REF_CURSOR");
		types.put(Types.TIME_WITH_TIMEZONE, "TIME_WITH_TIMEZONE");
		types.put(Types.TIMESTAMP_WITH_TIMEZONE, "TIMESTAMP_WITH_TIMEZONE");
		nullability = new HashMap<Integer,String>();
		nullability.put(DatabaseMetaData.columnNoNulls, "No Nulls");
		nullability.put(DatabaseMetaData.columnNullable, "Nullable");
		nullability.put(DatabaseMetaData.columnNullableUnknown, "NullableUnknown");
		
	}
	
	public void setValue(PreparedStatement ps ,Object value, int typeField, int parameterIndex, int columnIndex) throws SQLException {
		if(fieldIsInsertable(columnIndex-1)) {
			switch(typeField) {
			case Types.BOOLEAN: case Types.BIT:
				ps.setBoolean(parameterIndex, (Boolean) value);
				break;
			case Types.TINYINT: case Types.INTEGER: case Types.SMALLINT:
				ps.setInt(parameterIndex, (Integer) value);
				break;
			case Types.VARCHAR: case Types.NVARCHAR:case Types.LONGNVARCHAR: case Types.LONGVARCHAR: case Types.CHAR:
				ps.setString(parameterIndex, (String) value);
				break;
			/*case Types.TIME:
				ps.setTime(parameterIndex, (Time) value);
				break;
			case Types.TIMESTAMP:
				ps.setTimestamp(parameterIndex, ((Timestamp) value));
				break;*/
			case Types.DATE: 
				ps.setDate(parameterIndex, (Date) value);
				break;
			case Types.REAL:
				ps.setFloat(parameterIndex, (Float) value);
				break;
			case Types.DOUBLE:
				ps.setDouble(parameterIndex, (Double) value);
				break;
			case Types.BIGINT:
				ps.setLong(parameterIndex, (Long) value);
				break;
			case Types.DECIMAL:
				ps.setBigDecimal(parameterIndex, (BigDecimal) value);
				break;
			/*case Types.LONGVARBINARY:  case Types.VARBINARY: case Types.BINARY:
				ps.setBytes(parameterIndex, (byte[]) value);
				break;*/
			default:
				///throw new RuntimeException("Unexpected type "+ types.get(typeField)+":"+value);
				ps.setNull(parameterIndex, typeField);
				
			}
		}
	}
	public void setValue(PreparedStatement ps , int typeField, int parameterIndex, int columnIndex) throws SQLException {
		StringBuilder text = new StringBuilder();
		text.append(String.format("%s.%s: %s $: ",this.tablename,metadata.getColumnName(columnIndex),CUDHelper.types.get(typeField)));
		if(fieldIsInsertable(columnIndex)) {
			switch(typeField) {
			case Types.BOOLEAN: case Types.BIT:
				ps.setBoolean(parameterIndex, Leer.leerBoolean(text.toString()));
				break;
			case Types.TINYINT: case Types.INTEGER: case Types.SMALLINT:
				ps.setInt(parameterIndex, Leer.leerEntero(text.toString()));
				break;
			case Types.VARCHAR: case Types.NVARCHAR:case Types.LONGNVARCHAR: case Types.LONGVARCHAR: case Types.CHAR:
				ps.setString(parameterIndex, (String) Leer.leerTexto(text.toString()));
				break;
			/*case Types.TIME:
				ps.setTime(parameterIndex, (Time) value);
				break;
			case Types.TIMESTAMP:
				ps.setTimestamp(parameterIndex, ((Timestamp) value));
				break;*/
			case Types.DATE: 
				ps.setDate(parameterIndex,Leer.leerFechaSQL(text.toString()));
				break;
			case Types.REAL:
				ps.setFloat(parameterIndex,Leer.leerFloat(text.toString()));
				break;
			case Types.DOUBLE:
				ps.setDouble(parameterIndex,Leer.leerDouble(text.toString()));
				break;
			case Types.BIGINT:
				ps.setLong(parameterIndex, Leer.leerLong(text.toString()));
				break;
			case Types.DECIMAL:
				ps.setBigDecimal(parameterIndex, (BigDecimal) Leer.leerBigDecimal(text.toString()));
				break;
			/*case Types.LONGVARBINARY:  case Types.VARBINARY: case Types.BINARY:
				ps.setBytes(parameterIndex, (byte[]) value);
				break;*/
			default:
				///throw new RuntimeException("Unexpected type "+ types.get(typeField)+":"+value);
				ps.setNull(parameterIndex, typeField);
				
			}
		}
	}
	
	/**
	 * The operation done with this {@link CUDHelper} is not permitted
	 * see 
	 * <br>{@link CUDHelper#deleteIsPermitted()}
	 * <br> {@link CUDHelper#updateIsPermitted()}
	 * <br>{@link CUDHelper#insertIsPermitted()}
	 * @author Catalan Renegado
	 *
	 */
	@Deprecated
	public class NotPermittedException extends RuntimeException {
		private static final long serialVersionUID = 5040948309038539896L;
		public NotPermittedException(String operation) {
			super(String.format("We cant create a statement to %s in this ResultSetMetadata.",operation));
		}
	}
}
