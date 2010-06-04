package org.kroz.activerecord;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;

/**
 * Represents a database to be used by Android Active Record entities.
 * 
 * @author jeremyot
 */
public class Database {

	private SQLiteDatabase mDatabase;
	private String mPath;
	private Context mContext;

	/**
	 * Creates a new ARDatabase object
	 * 
	 * @param fileName
	 *            The file name to use for the SQLite database.
	 * @param context
	 *            The context used for database creation, its package name will
	 *            be used to place the database on external storage if any is
	 *            present, otherwise the context's application data directory.
	 */
	public Database(String fileName, Context context) {
		mPath = fileName;
		mContext = context;
	}

	/**
	 * Opens or creates the database file. Uses external storage if present,
	 * otherwise uses local storage.
	 */
	public void open() {
		if (mDatabase != null && mDatabase.isOpen()) {
			mDatabase.close();
			mDatabase = null;
		}
		String dbPath = (Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED) ? appendFilePath(Environment
				.getExternalStorageDirectory().getAbsolutePath(), String
				.format("android%1$sdata%1$s%2$s%1$s", File.separator, mContext
						.getPackageName())) : mContext.getDir(
				mContext.getPackageName(), 0).getAbsolutePath());
		new File(dbPath).mkdirs();
		mDatabase = SQLiteDatabase.openDatabase(appendFilePath(dbPath, mPath),
				null, SQLiteDatabase.CREATE_IF_NECESSARY
						| SQLiteDatabase.NO_LOCALIZED_COLLATORS);
	}

	public void close() {
		if (mDatabase != null)
			mDatabase.close();
		mDatabase = null;
	}

	/**
	 * Execute some raw SQL.
	 * 
	 * @param sql
	 *            Standard SQLite compatible SQL.
	 */
	public void execute(String sql) {
		mDatabase.execSQL(sql);
	}

	/**
	 * Insert into a table in the database.
	 * 
	 * @param table
	 *            The table to insert into.
	 * @param parameters
	 *            The data.
	 * @return The ID of the new row.
	 */
	public long insert(String table, ContentValues parameters) {
		return mDatabase.insert(table, null, parameters);
	}

	/**
	 * Update a table in the database.
	 * 
	 * @param table
	 *            The table to update.
	 * @param values
	 *            The new values.
	 * @param whereClause
	 *            The condition to match (Don't include "where").
	 * @param whereArgs
	 *            The arguments to replace "?" with.
	 * @return The number of rows affected.
	 */
	public int update(String table, ContentValues values, String whereClause,
			String[] whereArgs) {
		return mDatabase.update(table, values, whereClause, whereArgs);
	}

	/**
	 * Delete from a table in the database
	 * 
	 * @param table
	 *            The table to delete from.
	 * @param whereClause
	 *            The condition to match (Don't include WHERE).
	 * @param whereArgs
	 *            The arguments to replace "?" with.
	 * @return The number of rows affected.
	 */
	public int delete(String table, String whereClause, String[] whereArgs) {
		return mDatabase.delete(table, whereClause, whereArgs);
	}

	/**
	 * Execute a raw SQL query.
	 * 
	 * @param sql
	 *            Standard SQLite compatible SQL.
	 * @return A cursor over the data returned.
	 */
	public Cursor rawQuery(String sql) {
		return rawQuery(sql, null);
	}

	/**
	 * Execute a raw SQL query.
	 * 
	 * @param sql
	 *            Standard SQLite compatible SQL.
	 * @param params
	 *            The values to replace "?" with.
	 * @return A cursor over the data returned.
	 */
	public Cursor rawQuery(String sql, String[] params) {
		return mDatabase.rawQuery(sql, params);
	}

	/**
	 * Execute a query.
	 * 
	 * @param table
	 *            The table to query.
	 * @param selectColumns
	 *            The columns to select.
	 * @param where
	 *            The condition to match (Don't include "where").
	 * @param whereArgs
	 *            The arguments to replace "?" with.
	 * @return A cursor over the data returned.
	 */
	public Cursor query(String table, String[] selectColumns, String where,
			String[] whereArgs) {
		return query(false, table, selectColumns, where, whereArgs, null, null,
				null, null);
	}

	/**
	 * Execute a query.
	 * 
	 * @param distinct
	 * @param table
	 *            The table to query.
	 * @param selectColumns
	 *            The columns to select.
	 * @param where
	 *            The condition to match (Don't include "where").
	 * @param whereArgs
	 *            The arguments to replace "?" with.
	 * @param groupBy
	 * @param having
	 * @param orderBy
	 * @param limit
	 * @return A cursor over the data returned.
	 */
	public Cursor query(boolean distinct, String table, String[] selectColumns,
			String where, String[] whereArgs, String groupBy, String having,
			String orderBy, String limit) {
		return mDatabase.query(distinct, table, selectColumns, where,
				whereArgs, groupBy, having, orderBy, limit);
	}

	public String[] getTables() {
		Cursor c = query("sqlite_master", new String[] { "name" }, "type = ?",
				new String[] { "table" });
		List<String> tables = new ArrayList<String>();
		try {
			while (c.moveToNext()) {
				tables.add(c.getString(0));
			}
		} finally {
			c.close();
		}
		return tables.toArray(new String[0]);
	}

	public String[] getColumnsForTable(String table) {
		Cursor c = rawQuery(String.format("PRAGMA table_info(%s)", table));
		List<String> columns = new ArrayList<String>();
		try {
			while (c.moveToNext()) {
				columns.add(c.getString(c.getColumnIndex("name")));
			}
		} finally {
			c.close();
		}
		return columns.toArray(new String[0]);
	}

	public int getVersion() throws Exception {
		if (!mDatabase.isOpen())
			throw new Exception("Database closed.");
		return mDatabase.getVersion();
	}

	public void setVersion(int version) throws Exception {
		if (!mDatabase.isOpen())
			throw new Exception("Database closed.");
		mDatabase.setVersion(version);
	}

	public void beginTransaction() {
		mDatabase.beginTransaction();
	}

	public void endTransaction() {
		mDatabase.endTransaction();
	}

	/**
	 * Get the SQLite type for an input class.
	 * 
	 * @param c
	 *            The class to convert.
	 * @return A string representing the SQLite type that would be used to store
	 *         that class.
	 */
	protected static String getSQLiteTypeString(Class<?> c) {
		String name = c.getName();
		if (name.equals("java.lang.String"))
			return "text";
		if (name.equals("short"))
			return "int";
		if (name.equals("int"))
			return "int";
		if (name.equals("long"))
			return "int";
		if (name.equals("long"))
			return "int";
		if (name.equals("java.sql.Timestamp"))
			return "int";
		if (name.equals("double"))
			return "real";
		if (name.equals("float"))
			return "real";
		if (name.equals("[B"))
			return "blob";
		if (name.equals("boolean"))
			return "bool";
		if (c.getSuperclass() == AREntity.class)
			return "int";
		throw new IllegalArgumentException(
				"Class cannot be stored in Sqlite3 database.");
	}

	/**
	 * Append to a file path, takes extra or missing separator characters into
	 * account.
	 * 
	 * @param path
	 *            The root path.
	 * @param append
	 *            What to add.
	 * @return The new path.
	 */
	private static String appendFilePath(String path, String append) {
		return path.concat(path.endsWith(File.separator) ? (append
				.startsWith(File.separator) ? append.substring(1) : append)
				: File.separator
						.concat((append.startsWith(File.separator) ? append
								.substring(1) : append)));
	}
}
