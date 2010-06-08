package org.kroz.activerecord;

import org.kroz.activerecord.ActiveRecordBase;
import org.kroz.activerecord.Database;
import org.kroz.activerecord.DatabaseBuilder;
import org.kroz.activerecord.test_fixture.Showplace;
import org.kroz.activerecord.test_fixture.ShowplaceDetail;
import org.kroz.activerecord.test_fixture.User;
import org.kroz.activerecord.test_fixture.UserData;

import android.content.Context;
import android.database.sqlite.SQLiteException;
import android.test.AndroidTestCase;

public class ConnectionTest extends AndroidTestCase {

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

	public void testDbConnect1() {
		try {
			DatabaseBuilder builder = new DatabaseBuilder();
			builder.addClass(User.class);
			builder.addClass(UserData.class);
			builder.addClass(Showplace.class);
			builder.addClass(ShowplaceDetail.class);
			Database.setDefaultBuilder(builder);
			
			// Open DB #1 
			Database db = Database.create(mCtx, mDbName);
			db.open();
			ActiveRecordBase con1 = ActiveRecordBase.connect(db); 

			// Open DB #2 
			ActiveRecordBase con2 = ActiveRecordBase.connect(mCtx, mDbName); 
						
			// Find record - record not found
			User usr1 = con1.findByID(User.class, 1);

			// Record not found - create new object, set fields and save in DB
			assertNull(usr1);
			usr1 = con1.newEntity(User.class);
			usr1.set("firstName", "John");
			usr1.set("lastName", "Smith");
			usr1.set("registrationDate", System.currentTimeMillis());
			usr1.set("ssn", 1234567890);
			usr1.save();

			// Now find record - record found
			User usr2 = con2.findByID(User.class, 1);
			// Compare - must be identical
			assertTrue(usr2.equals(usr1));

			// Modify fields, copy object
			usr2.set("ssn", 9999999);
			// Compare - must be identical
			assertTrue(usr2.equals(usr1));

			// Save record
			usr2.save();

			// Save record - do nothing
			usr2.save();

			// Close DB
			con1.close();
			con2.close();

			// Find record, compare data with copy in memory (must be equal)
			// Now find record - record found
			usr2 = con1.findByID(User.class, 1);

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

	public void testDbConnect2() {
	}

}
