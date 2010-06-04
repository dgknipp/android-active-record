package org.kroz.activerecord;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

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

	DatabaseBuilder mBuilder;
	
	DatabaseOpenHelper(Context ctx, String dbName, DatabaseBuilder builder) {
		super(ctx, dbName, null, currentPackageVersion(ctx));
		mBuilder = builder;
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		for(String table: mBuilder.getTables()) {
			String sqlStr = mBuilder.getSQLCreate(table);
			db.execSQL(sqlStr);
		}
		// db.execSQL(ShowplacesTable.SQL_CREATE);
		// db.execSQL(FileInfoTable.SQL_CREATE);
		// db.execSQL(CacheStatusTable.SQL_CREATE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		for(String table: mBuilder.getTables()) {
			String sqlStr = mBuilder.getSQLDrop(table);
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
