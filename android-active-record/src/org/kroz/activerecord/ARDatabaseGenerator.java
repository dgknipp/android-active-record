package org.kroz.activerecord;

import java.lang.reflect.Field;

/**
 * Generates and updates ARDatabases.
 * 
 * @author Vladimir Kroz (AKA vkroz)
 * @author jeremyot
 * 
 *         This project based on and inspired by 'androidactiverecord' project
 *         written by JEREMYOT
 */
public class ARDatabaseGenerator {
	private Database mDatabase;
	private boolean mMigrating = false;
	private boolean mUpdating = false;
	private static int mDatabaseVersion;

	
	
	/**
	 * Create a new ARDatabaseGenerator for a database.
	 * 
	 * @param database
	 *            The database to generate or upgrade.
	 * @param currentVersion
	 *            The version that an up to date database would have.
	 */
	public ARDatabaseGenerator(Database database, int currentVersion) {
		mDatabase = database;
		mDatabaseVersion = currentVersion;
	}

	public boolean needsUpdate() throws Exception {
		return mDatabase.getVersion() != mDatabaseVersion;
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
	public <T extends ActiveRecordBase> void addClass(Class<T> c) throws IllegalAccessException,
			InstantiationException, IllegalStateException {
		if (!mUpdating)
			throw new IllegalStateException("Cannot modify database before initializing update.");
		boolean createTable = true;
		T e = c.newInstance();
		String name = e.getTableName();
		if (mMigrating) {
			String[] tables = mDatabase.getTables();
			for (int i = 0; i < tables.length; i++) {
				if (tables[i].equals(name)) {
					createTable = false;
					break;
				}
			}
		}
		if (createTable) {
			StringBuilder sb = new StringBuilder("CREATE TABLE ").append(name).append(
					" (_id integer primary key");
			for (Field column : e.getColumnFieldsWithoutID()) {
				String jname = column.getName();
				Class<?> jtype = column.getType();
				String qtype = Database.getSQLiteTypeString(jtype);
				sb.append(", ").append(jname).append(" ").append(qtype);
			}
			sb.append(")");
			mDatabase.execute(sb.toString());
		} else {
			String[] existingColumns = mDatabase.getColumnsForTable(name);
			for (Field column : e.getColumnFieldsWithoutID()) {
				boolean addColumn = true;
				for (int j = 0; j < existingColumns.length; j++) {
					if (existingColumns[j].equals(column.getName())) {
						addColumn = false;
						break;
					}
				}
				if (addColumn) {
					mDatabase.execute(String.format("ALTER TABLE %s ADD COLUMN %s %s", name,
							column.getName(), Database.getSQLiteTypeString(column.getType())));
				}
			}
		}
	}

	/**
	 * Call this before calling addClass(), will wipe the existing database.
	 */
	public void beginUpdate() {
		beginUpdate(true);
	}

	/**
	 * Call this before calling addClass().
	 * 
	 * @param clear
	 *            Whether or not to drop all existing data. Use this if breaking changes have been made.
	 */
	public void beginUpdate(boolean clear) {
		String[] tables = mDatabase.getTables();
		if (clear) {
			for (String table : tables)
				mDatabase.execute(String.format("drop table %s", table));
		}
		if (!clear && tables.length > 0)
			mMigrating = true;
		mUpdating = true;
	}

	/**
	 * Call this after all updates have been completed, when there will be no more calls to addClass().
	 * 
	 * @throws Exception
	 */
	public void endUpdate() throws Exception {
		mUpdating = false;
		mMigrating = false;
		mDatabase.setVersion(mDatabaseVersion);
	}
}
