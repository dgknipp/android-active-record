package org.kroz.activerecord.test;

import android.database.sqlite.SQLiteDatabase;
import android.test.AndroidTestCase;

public class DatabaseTest extends AndroidTestCase {

	protected void setUp() throws Exception {
		super.setUp();
	}
	
	protected void tearDown() throws Exception {
		super.tearDown();
	}

	public void testDatabase() {
//		fail("Not yet implemented");
	}

	public void testOpen() {
		String dbPath = "/";
//		Database db = new Database("", null);
		SQLiteDatabase mDatabase = SQLiteDatabase.openDatabase(dbPath,
				null, SQLiteDatabase.CREATE_IF_NECESSARY
						| SQLiteDatabase.NO_LOCALIZED_COLLATORS);
		assertNotNull(mDatabase);
	}

}
