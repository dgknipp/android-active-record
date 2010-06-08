package org.kroz.activerecord;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Provides access to a database of notes. Each note has a title, the note
 * itself, a creation date and a modified data.
 * 
 * @author Vladimir Kroz (AKA vkroz)
 * @author jeremyot
 * 
 *         This project based on and inspired by 'androidactiverecord' project
 *         written by JEREMYOT
 */
public class DatabaseOpenHelper extends SQLiteOpenHelper {

	DatabaseBuilder _builder;

	DatabaseOpenHelper(Context ctx, String dbName, DatabaseBuilder builder) {
		super(ctx, dbName, null, currentPackageVersion(ctx));
		_builder = builder;
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		for (String table : _builder.getTables()) {
			String sqlStr = null;
			try {
				sqlStr = _builder.getSQLCreate(table);
			} catch (IllegalAccessException e) {
				Log.e(this.getClass().getName(), e.getMessage(), e);
			} catch (InstantiationException e) {
				Log.e(this.getClass().getName(), e.getMessage(), e);
			}
			if (sqlStr != null)
				db.execSQL(sqlStr);
		}
		// db.execSQL(ShowplacesTable.SQL_CREATE);
		// db.execSQL(FileInfoTable.SQL_CREATE);
		// db.execSQL(CacheStatusTable.SQL_CREATE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		for (String table : _builder.getTables()) {
			String sqlStr = _builder.getSQLDrop(table);
			db.execSQL(sqlStr);
		}
		// db.execSQL("DROP TABLE IF EXISTS " + ShowplacesTable.TABLE_NAME);
		// db.execSQL("DROP TABLE IF EXISTS " + FileInfoTable.TABLE_NAME);
		// db.execSQL("DROP TABLE IF EXISTS " + CacheStatusTable.TABLE_NAME);
		onCreate(db);
	}

	/**
	 * Returns version code of the Android application package
	 * 
	 * @param ctx
	 * @return application' version code
	 */
	static int currentPackageVersion(Context ctx) {
		String packageName = ctx.getPackageName();
		try {
			PackageInfo pi = ctx.getPackageManager().getPackageInfo(
					packageName, 0);
			return pi.versionCode;
		} catch (NameNotFoundException e) {
			return -1;
		}
	}

}
