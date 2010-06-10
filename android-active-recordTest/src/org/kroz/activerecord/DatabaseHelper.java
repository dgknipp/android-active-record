package org.kroz.activerecord;

import java.io.File;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper {
	public static void dropAllTables(Database db) throws ActiveRecordException {
		if (db.isOpen()) {
			String[] tables;
			try {
				tables = db.getTables();
				for (String table : tables) {
					if (table.equalsIgnoreCase("android_metadata"))
						continue;
					else
						db.execute("drop table if exists " + table);
				}
			} catch (ActiveRecordException e) {
				e.printStackTrace();
			}
		} else
			throw new ActiveRecordException(ErrMsg.ERR_DB_IS_NOT_OPEN);
	}

	public static boolean dropDatabase(Context ctx, String dbName)
			throws ActiveRecordException {
		SQLiteOpenHelper hlp = new DatabaseOpenHelper(ctx, dbName,
				new DatabaseBuilder(dbName, 1));
		try {
			SQLiteDatabase db = hlp.getReadableDatabase();
			String p = db.getPath();
			db.close();
			hlp.close();
			File f = new File(p);
			return f.delete();
		} catch (SQLiteException e) {
			e.printStackTrace();
		}
		return false;
	}

}
