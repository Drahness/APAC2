package com.ieseljust.ad.myDBMS;

import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
/**
 * Retrieves all the relative information from a table, this are the metas: <br>
1.TABLE_CAT String => table catalog (may be null)  <br>
2.TABLE_SCHEM String => table schema (may be null) <br>
3.TABLE_NAME String => table name  <br>
4.COLUMN_NAME String => column name  <br>
5.DATA_TYPE int => SQL type from java.sql.Types <br> 
6.TYPE_NAME String => Data source dependent type name,for a UDT the type name is fully qualified <br> 
7.COLUMN_SIZE int => column size.  <br>
8.BUFFER_LENGTH is not used.  <br>
9.DECIMAL_DIGITS int => the number of fractional digits. Null is returned for data types whereDECIMAL_DIGITS is not applicable. <br> 
10.NUM_PREC_RADIX int => Radix (typically either 10 or 2)  <br>
11.NULLABLE int => is NULL allowed.  columnNoNulls - might not allow NULL values  
 columnNullable - definitely allows NULL values  
columnNullableUnknown - nullability unknown 

12.REMARKS String => comment describing column (may be null)  <br>
13.COLUMN_DEF String => default value for the column, which should be interpreted as a string when the value is enclosed in single quotes (may be null) <br>
 
14.SQL_DATA_TYPE int => unused <br> 
15.SQL_DATETIME_SUB int => unused  <br>
16.CHAR_OCTET_LENGTH int => for char types themaximum number of bytes in the column <br> 
17.ORDINAL_POSITION int => index of column in table(starting at 1)  <br>
18.IS_NULLABLE String => ISO rules are used to determine the nullability for a column. "YES" | "NO" | "" <br> 

19.SCOPE_CATALOG String => catalog of table that is the scopeof a reference attribute (null if DATA_TYPE isn't REF) <br> 
20.SCOPE_SCHEMA String => schema of table that is the scopeof a reference attribute (null if the DATA_TYPE isn't REF)  <br>
21.SCOPE_TABLE String => table name that this the scopeof a reference attribute (null if the DATA_TYPE isn't REF)  <br>
22.SOURCE_DATA_TYPE short => source type of a distinct type or user-generatedRef type, SQL type from java.sql.Types (null if DATA_TYPEisn't DISTINCT or user-generated REF) <br> 
23.IS_AUTOINCREMENT String => "NO" | "YES" | "" <br>
24.IS_GENERATEDCOLUMN String => "NO" | "YES" | ""; <br>
 
 * 
 * @author Catalan Renegado
 *
 */
public class TableMetadata {
	private int columnCount;
	private List<Map<String,Object>> metadata = new ArrayList<>();
	private boolean showPK = false;
	
	public <T> T getMeta(Class<T> expected, String label, int index) {
		if(metadata.get(index).get(label).getClass().equals(expected)) {
			return getMeta(label,index);
		}
		throw new RuntimeException(String.format("Unexpected class %s, expected %s, key %s, index %d, value %s"
				,metadata.get(index).get(label).getClass().getSimpleName()
				,expected.getSimpleName()
				,label
				,index
				,metadata.get(index).get(label)));
	}
	
	@SuppressWarnings("unchecked")
	public <T> T getMeta(String label, int index) {
		return (T) metadata.get(index).get(label);
	}
	
	public TableMetadata(DatabaseMetaData dbmd,String catalog, String table) throws SQLException {
		ResultSet keys = dbmd.getPrimaryKeys(catalog, null , table);
		ResultSet columns = dbmd.getColumns(catalog, null, table, null);
		ResultSetMetaData columnsMeta = columns.getMetaData();
		while(columns.next()) {
			int columnsCount = columnsMeta.getColumnCount();
			Map<String,Object> mappedTable = new HashMap<>();
			for (int i = 1; i < columnsCount+1; i++) {
				mappedTable.put(columnsMeta.getColumnLabel(i), columns.getObject(i));		
			}
			mappedTable.put("PRIMARY_KEY",false);
			metadata.add(mappedTable);
		}
		while(keys.next()) {
			String columnName = keys.getString("COLUMN_NAME");
			for (Map<String,Object> b : metadata) {
				if(b.containsValue(columnName)) {
					b.put("PRIMARY_KEY", true);
					showPK = b.get("IS_AUTOINCREMENT").equals("YES");
				} else {
					b.put("PRIMARY_KEY", false);
				}
			}
		}
		columnCount = metadata.size();
	}
	
	public List<Map<String,String>> getMetadata(boolean seeAll) {
		List<Map<String,String>> mappedMetadata = new ArrayList<>();
		for (int i = 0; i < columnCount; i++) {
			Map<String,String> map = new HashMap<String,String>();
			Map<String, Object> currentColumn = metadata.get(i);
			for (String key : currentColumn.keySet()) {
				Object value = currentColumn.get(key);
				if(seeAll || displayable(key)) {
					if(value != null) {
						map.put(key, value.toString());
					}
					/*else {
						map.put(key, "");
					}*/
				}
			}
			mappedMetadata.add(map);
		}
		return mappedMetadata;
		
	}
	/**
	 * 
	 * @param i column index
	 * @return if it is autoIncremental, if false is not sure, can be auto 
	 */
	public boolean isAutoIncremental(int i) {
		return getMeta("IS_AUTOINCREMENT",i).equals("YES");
	}
	
	public boolean isPrimary(int i) {
		return getMeta("PRIMARY_KEY",i).equals(true);
	}
	
	public boolean isNullable(int i) {
		return getMeta("IS_NULLABLE",i).equals("YES");
	}
	
	private boolean displayable(String labelString) {
		boolean b = Objects.equals(labelString, "COLUMN_NAME") || Objects.equals(labelString, "IS_NULLABLE") || Objects.equals(labelString, "PRIMARY_KEY") || Objects.equals(labelString, "IS_AUTOINCREMENT") || Objects.equals(labelString, "TYPE_NAME") || Objects.equals(labelString, "COLUMN_SIZE");
		return b;
	}
	public int getColumnCount() {
		return columnCount;
	}
	
	public int getDataType(int i) {
		return getMeta("DATA_TYPE",i);
	}

	public String getTableName(int i) {
		return getMeta("TABLE_CAT",i);
	}

	public String getColumnName(int i) {
		return getMeta("COLUMN_NAME",i);
	}

	public boolean isShowPK() {
		return showPK;
	}
}
