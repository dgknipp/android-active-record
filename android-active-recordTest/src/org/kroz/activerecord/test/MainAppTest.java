package org.kroz.activerecord.test;

import org.kroz.activerecord.ActiveRecordBase;
import org.kroz.activerecord.Database;
import org.kroz.activerecord.DatabaseBuilder;
import org.kroz.activerecord.test.fixture.User;
import org.kroz.activerecord.test.fixture.UserData;

import android.content.Context;
import android.database.sqlite.SQLiteException;
import android.test.AndroidTestCase;

public class MainAppTest extends AndroidTestCase {

	// ----------------- Fixture START --------------------//
	String mDbName = "test.db";
	Context mCtx = getContext();

	// ----------------- Fixture END--------------------//
	protected void setUp() throws Exception {
		super.setUp();
		mDbName = "test.db";
		mCtx = getContext();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}

	public void testPreconditions() {
		assertNotNull(1);
	}

	public void testAll() {
		try {
			DatabaseBuilder builder = new DatabaseBuilder(1);
			builder.addClass(User.class).addClass(UserData.class);
			
			// Create database object instance
			Database db = Database.create(mCtx, mDbName, builder);
			
			// Connect to BD
			ActiveRecordBase.connect(db);
			
			// Find record - record not found
			User usr1 = ActiveRecordBase.findByID(User.class, 1);

			// Record not found - create new object, set fields
			assertNull(usr1);
			usr1 = new User();
			usr1.set("firstName", "John");
			usr1.set("lastName", "Smith");
			usr1.set("registrationDate", System.currentTimeMillis());
			usr1.set("ssn", 1234567890);

			// Save in DB
			usr1.save();

			// Now find record - record found
			User usr2 = ActiveRecordBase.findByID(User.class, 1);
			// Compare - must be identical
			assertTrue(usr2.equals(usr1));

			// Modify fields, copy object
			usr2.set("ssn", 9999999);
			// Compare - must be identical
			assertTrue(usr2.equals(usr1));

			// Save record
			usr1.save();

			// Save record - do nothing
			usr2.save();

			// Open DB
			ActiveRecordBase.connect(db);

			// Find record, compare data with copy in memory (must be equal)
			// Now find record - record found
			usr2 = ActiveRecordBase.findByID(User.class, 1);

			// Delete record
			usr2.delete();

			// Find record - not found

			assert (true);
		} catch (InstantiationException e) {
			fail(e.getMessage());
		} catch (IllegalAccessException e) {
			fail(e.getMessage());
		} catch (SQLiteException e) {
			fail(e.getMessage());
		}
	}

}
