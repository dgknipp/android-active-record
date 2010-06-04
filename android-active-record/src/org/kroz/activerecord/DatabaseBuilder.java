package org.kroz.activerecord;

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
	 * Add or update a table for an AREntity that is stored in the current database.
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
		
//		if (!mUpdating)
//			throw new IllegalStateException("Cannot modify database before initializing update.");
//		boolean createTable = true;
//		T e = c.newInstance();
//		String name = e.getTableName();
//		if (mMigrating) {
//			String[] tables = mDatabase.getTables();
//			for (int i = 0; i < tables.length; i++) {
//				if (tables[i].equals(name)) {
//					createTable = false;
//					break;
//				}
//			}
//		}
//		if (createTable) {
//			StringBuilder sb = new StringBuilder("CREATE TABLE ").append(name).append(
//					" (_id integer primary key");
//			for (Field column : e.getColumnFieldsWithoutID()) {
//				String jname = column.getName();
//				Class<?> jtype = column.getType();
//				String qtype = Database.getSQLiteTypeString(jtype);
//				sb.append(", ").append(jname).append(" ").append(qtype);
//			}
//			sb.append(")");
//			mDatabase.execute(sb.toString());
//		} else {
//			String[] existingColumns = mDatabase.getColumnsForTable(name);
//			for (Field column : e.getColumnFieldsWithoutID()) {
//				boolean addColumn = true;
//				for (int j = 0; j < existingColumns.length; j++) {
//					if (existingColumns[j].equals(column.getName())) {
//						addColumn = false;
//						break;
//					}
//				}
//				if (addColumn) {
//					mDatabase.execute(String.format("ALTER TABLE %s ADD COLUMN %s %s", name,
//							column.getName(), Database.getSQLiteTypeString(column.getType())));
//				}
//			}
//		}
		return this;
	}
	
	@SuppressWarnings("unchecked")
	public String[] getTables() {
		String[] ret = new String[classes.size()]; 
		Class[] arr = new Class[classes.size()];
		arr = classes.values().toArray(arr);
		for(int i=0; i<arr.length; i++) {
			Class c = arr[i];
			ret[i] = CamelNotationHelper.toSQLName(c.getSimpleName());
		}
		return ret;
	}
//	public String[] getTables() {
//		return null;
//	}

	/**
	 * Returns SQL create statement for specified table
	 */
	public String getSQLCreate(String table) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * Returns SQL drop table statement for specified table
	 */
	public String getSQLDrop(String table) {
		// TODO Auto-generated method stub
		return null;
	}
}
