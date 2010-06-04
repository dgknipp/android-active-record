package org.kroz.activerecord;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Generates and updates ARDatabases.
 * 
 * @author Vladimir Kroz (AKA vkroz)
 * @author jeremyot
 * 
 *         This project based on and inspired by 'androidactiverecord' project
 *         written by JEREMYOT
 */
public class DatabaseBuilder {
	private static int mDatabaseVersion;

	Map<String, Class> classes = new HashMap<String, Class>();

	/**
	 * Create a new DatabaseBuilder for a database.
	 * 
	 * @param currentVersion
	 *            The version that an up to date database would have.
	 */
	public DatabaseBuilder(int currentVersion) {
		mDatabaseVersion = currentVersion;
	}

	/**
	 * Add or update a table for an AREntity that is stored in the current
	 * database.
	 * 
	 * @param <T>
	 *            Any AREntity type.
	 * @param c
	 *            The class to reference when updating or adding a table.
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 * @throws IllegalStateException
	 */
	public <T extends ActiveRecordBase> DatabaseBuilder addClass(Class<T> c) {
		classes.put(c.getName(), c);
		return this;
	}

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
	 * @throws InstantiationException 
	 * @throws IllegalAccessException 
	 */
	@SuppressWarnings("unchecked")
	public <T extends ActiveRecordBase> String getSQLCreate(String table) throws IllegalAccessException, InstantiationException {
		StringBuilder sb=null;
		Class<T> c = getClassBySqlName(table);
		T e = c.newInstance();
		if (null != c) {
			sb = new StringBuilder("CREATE TABLE ").append(table)
					.append(" (_id integer primary key");
			for (Field column : e.getColumnFieldsWithoutID()) {
				String jname = column.getName();
				Class<?> jtype = column.getType();
				String qtype = Database.getSQLiteTypeString(jtype);
				sb.append(", ").append(jname).append(" ").append(qtype);
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

	private Class getClassBySqlName(String table) {
		String jName = CamelNotationHelper.toJavaClassName(table);
		return classes.get(jName);
	}

}
