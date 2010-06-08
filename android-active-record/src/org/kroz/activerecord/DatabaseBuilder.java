package org.kroz.activerecord;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.util.Log;

/**
 * Helper class to create/upgrade/open DB. 
 * Can be extended by application specific builder to hide details of constructing application-specific DB 
 * @author Vladimir Kroz (vkroz)
 */
public class DatabaseBuilder {

	Map<String, Class> classes = new HashMap<String, Class>();
	int _dbVersion;
	Context _ctx;

	/**
	 * Create a new DatabaseBuilder for a database.
	 * 
	 * @param currentVersion
	 *            The version that an up to date database would have.
	 */
	public DatabaseBuilder() {
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
	String[] getTables() {
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
	<T extends ActiveRecordBase> String getSQLCreate(String table) throws IllegalAccessException, InstantiationException {
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
	String getSQLDrop(String table) {
		return "DROP TABLE IF EXISTS " + table;
	}

	@SuppressWarnings("unchecked")
	private Class getClassBySqlName(String table) {
		String jName = CamelNotationHelper.toJavaClassName(table);
		return classes.get(jName);
	}
	

}
