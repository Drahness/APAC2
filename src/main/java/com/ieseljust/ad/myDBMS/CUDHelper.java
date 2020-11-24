package com.ieseljust.ad.myDBMS;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CUDHelper {
	public static Map<Integer,String> types;
	public static Map<Integer,String> nullability;
	private int columnCount;
	private boolean isWithKeys = false;
	private boolean[] primary;
	private boolean[] autoincrement;
	private int[] type;
	private int[] precision;
	private int[] nullable;
	private int[] scale;
	private boolean[] read_only;
	private boolean[] case_sensitive;
	private boolean[] writable;
	private int[] display_size;
	private boolean[] signed;
	private boolean[] definitely_writable;
	private boolean[] searchable;
	private String[] table_name;
	private String[] name;
	private String[] catalog;
	private String[] classArr;
	
	public boolean metadataOfASingleTable() {
		String table = table_name[0];
		String catalog = table_name[0];
		for (int i = 0 ; i < columnCount ; i++) {
			String table2 = table_name[i];
			String catalog2 = table_name[i];
			if(!table.equals(table2) || !catalog.equals(catalog2) ) {
				return false;
			}
		}
		return true;
	}
	
	public int executeInsertUser(Connection conn) throws SQLException {
		PreparedStatement ps = conn.prepareStatement(getInsertStatement());
		for (int i = 0; i < columnCount; i++) {
			this.setValue(ps, type[i], i+1, i);
		}
		return ps.executeUpdate();
	}
	
	public String getInsertStatement() {
		StringBuilder sb = new StringBuilder();
		if(insertIsPermitted()) {
			sb.append("INSERT INTO ").append(catalog[0]).append(".").append(table_name[0]);
			sb.append(getParametrizedText());
		}
		else {
			throw new NotPermittedException("insert");
		}
		return sb.toString();
	}
	
	private String getParametrizedText() {
		StringBuilder interrogants = new StringBuilder();
		StringBuilder col_names = new StringBuilder();
		for (int i = 0 ; i < columnCount; i++) {
			if(fieldIsInsertable(i)) {
				if(interrogants.length()==0) {
					col_names.append(' ').append('(').append(name[i]);
					interrogants.append(' ').append('(').append('?');
				} else {
					col_names.append(',').append(' ').append(name[i]);
					interrogants.append(',').append(' ').append('?');
				}
			}
		}
		col_names.append(')');
		interrogants.append(')');
		return col_names.toString() +" \nVALUES " +interrogants.toString();
	}
	
	
	
	public boolean fieldIsInsertable(int i) {
		return !autoincrement[i] && writable[i] && !read_only[i];
	}
	public boolean insertIsPermitted() {
		return metadataOfASingleTable();
	}
	public boolean deleteIsPermitted() {
		return metadataOfASingleTable();
	}
	public boolean updateIsPermitted() {
		return metadataOfASingleTable();
	}
	
	public CUDHelper(Connection conn, ResultSet rs) throws SQLException {
		this(rs);
		isWithKeys = true;
		String lastTable = null;
		List<String> PKs = new ArrayList<>();
		//List<String> FKs = new ArrayList<>();
		for (int i = 0 ; i < columnCount; i++) {
			String tablename = table_name[i];
			String catalogg = catalog[i];
			if(!tablename.equals(lastTable)) {
				ResultSet keys = conn.getMetaData().getPrimaryKeys(catalogg, null , tablename);
				while(keys.next()) {
					PKs.add(keys.getString("COLUMN_NAME"));
				}
				lastTable = tablename;
			}
			if(!PKs.isEmpty() && PKs.contains(name[i])) {
				PKs.remove(name[i]);
				primary[i] = true;
			} else {
				primary[i] = false;
			}
			
		}
	}
	
	public CUDHelper(ResultSet rs) throws SQLException {
		this(rs.getMetaData());
	}

	private CUDHelper(ResultSetMetaData rsmd) throws SQLException {
		columnCount = rsmd.getColumnCount();
		autoincrement = new boolean[columnCount];
		type = new int[columnCount];
		precision = new int[columnCount];
		nullable = new int[columnCount];
		scale = new int[columnCount];
		read_only = new boolean[columnCount];
		case_sensitive = new boolean[columnCount];
		writable = new boolean[columnCount];
		display_size = new int[columnCount];
		signed = new boolean[columnCount];
		definitely_writable = new boolean[columnCount];
		primary = new boolean[columnCount];
		searchable = new boolean[columnCount];
		table_name = new String[columnCount];
		name = new String[columnCount];
		catalog = new String[columnCount];
		classArr = new String[columnCount];
		for (int i = 1; i < 1+rsmd.getColumnCount(); i++) {
			int iArray = i-1;
			autoincrement[iArray] =rsmd.isAutoIncrement(i);
			type[iArray] =rsmd.getColumnType(i);
			precision[iArray] =rsmd.getPrecision(i);
			nullable[iArray] = rsmd.isNullable(i);
			read_only[iArray] =rsmd.isReadOnly(i);
			scale[iArray] =rsmd.getScale(i);
			case_sensitive[iArray] =rsmd.isCaseSensitive(i);
			writable[iArray] =rsmd.isWritable(i);
			signed[iArray] =rsmd.isSigned(i);
			display_size[iArray] =rsmd.getColumnDisplaySize(i);
			definitely_writable[iArray] =rsmd.isDefinitelyWritable(i);
			searchable[iArray] =rsmd.isSearchable(i);
			table_name[iArray] =rsmd.getTableName(i);
			name[iArray] =rsmd.getColumnLabel(i);
			catalog[iArray] = rsmd.getCatalogName(i);
			classArr[iArray] = rsmd.getColumnClassName(i);
		}
	}
	public List<Map<String,String>> getMap() {
		List<Map<String,String>> mappedMetadata = new ArrayList<>();
		for (int i = 0; i < columnCount; i++) {
			Map<String,String> map = new HashMap<String,String>();
			map.put("autoincrement",String.valueOf(autoincrement[i]));
			map.put("type",types.get(type[i]));
			//map.put("precision",String.valueOf(precision[i]));
			map.put("nullable",nullability.get(nullable[i]));
			//map.put("read_only",String.valueOf(read_only[i]));
			//map.put("scale",String.valueOf(scale[i]));
			//map.put("case_sensitive",String.valueOf(case_sensitive[i]));
			//map.put("writable",String.valueOf(writable[i]));
			//map.put("signed",String.valueOf(signed[i]));
			////map.put("display_size",String.valueOf(display_size[i]));
			//map.put("definitely_writable",String.valueOf(definitely_writable[i]));
			//map.put("searchable",String.valueOf(searchable[i]));
			map.put("table_name",String.valueOf(table_name[i]));
			map.put("column-name",String.valueOf(name[i]));
			map.put("catalog",String.valueOf(catalog[i]));
			//map.put("java_class",String.valueOf(classArr[i]));
			if (isWithKeys) {
				map.put("primary_key",String.valueOf(primary[i]));
			}
			mappedMetadata.add(map);
		}
		return mappedMetadata;
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
		nullability.put(ResultSetMetaData.columnNoNulls, "No Nulls");
		nullability.put(ResultSetMetaData.columnNullable, "Nullable");
		nullability.put(ResultSetMetaData.columnNullableUnknown, "NullableUnknown");
		
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
		if(fieldIsInsertable(columnIndex-1)) {
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
	public class NotPermittedException extends RuntimeException {
		private static final long serialVersionUID = 5040948309038539896L;
		public NotPermittedException(String operation) {
			super(String.format("We cant create a statement to %s in this ResultSetMetadata.",operation));
		}
	}
}
