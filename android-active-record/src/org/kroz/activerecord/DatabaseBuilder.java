package org.kroz.activerecord;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/**
 * Helper class to create/upgrade/open DB.
 * 
 * @author Vladimir Kroz (vkroz)
 */
public class DatabaseBuilder {

	Map<String, Class> classes = new HashMap<String, Class>();
	String _dbName;
	int _version;

	/**
	 * Create a new DatabaseBuilder for a database.
	 * 
	 * @param currentVersion
	 *            The version that an up to date database would have.
	 */
	public DatabaseBuilder(String dbName, int version) {
		this._dbName = dbName;
		this._version = version;
	}

	/**
	 * Add or update a table for an AREntity that is stored in the current
	 * database.
	 * 
	 * @param <T>
	 *            Any ActiveRecordBase type.
	 * @param c
	 *            The class to reference when updating or adding a table.
	 */
	public <T extends ActiveRecordBase> void addClass(Class<T> c) {
		classes.put(c.getSimpleName(), c);
	}

	/**
	 * Returns list of DB tables according to classes added to a schema map
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public String[] getTables() {
		String[] ret = new String[classes.size()];
		Class[] arr = new Class[classes.size()];
		arr = classes.values().toArray(arr);
		for (int i = 0; i < arr.length; i++) {
			Class c = arr[i];
			ret[i] = CamelNotationHelper.toSQLName(c.getSimpleName());
		}
		return ret;
	}

	/**
	 * Returns SQL create statement for specified table
	 * 
	 * @param table
	 *            name in SQL notation
	 * @throws ActiveRecordException
	 */
	@SuppressWarnings("unchecked")
	public <T extends ActiveRecordBase> String getSQLCreate(String table)
			throws ActiveRecordException {
		StringBuilder sb = null;
		Class<T> c = getClassBySqlName(table);
		T e = null;
		try {
			e = c.newInstance();
		} catch (IllegalAccessException e1) {
			throw new ActiveRecordException(e1.getLocalizedMessage());
		} catch (InstantiationException e1) {
			throw new ActiveRecordException(e1.getLocalizedMessage());
		}
		if (null != c) {
			sb = new StringBuilder("CREATE TABLE ").append(table).append(
					" (_id integer primary key");
			for (Field column : e.getColumnFieldsWithoutID()) {
				String jname = column.getName();
				String qname = CamelNotationHelper.toSQLName(jname);
				Class<?> jtype = column.getType();
				String qtype = Database.getSQLiteTypeString(jtype);
				sb.append(", ").append(qname).append(" ").append(qtype);
			}
			sb.append(")");

		}
		return sb.toString();
	}

	/**
	 * Returns SQL drop table statement for specified table
	 * 
	 * @param table
	 *            name in SQL notation
	 */
	public String getSQLDrop(String table) {
		return "DROP TABLE IF EXISTS " + table;
	}

	public String getDatabaseName() {
		return _dbName;
	}

	@SuppressWarnings("unchecked")
	private Class getClassBySqlName(String table) {
		String jName = CamelNotationHelper.toJavaClassName(table);
		return classes.get(jName);
	}

	public int getVersion() {
		return _version;
	}

}
