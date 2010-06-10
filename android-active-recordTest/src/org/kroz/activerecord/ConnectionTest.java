package org.kroz.activerecord;

import java.sql.Timestamp;
import java.util.List;

import org.kroz.activerecord.test.fixtures.Showplace;
import org.kroz.activerecord.test.fixtures.ShowplaceDetail;
import org.kroz.activerecord.test.fixtures.TestConst;
import org.kroz.activerecord.test.fixtures.User;
import org.kroz.activerecord.test.fixtures.UserData;

import android.content.Context;
import android.test.AndroidTestCase;

public class ConnectionTest extends AndroidTestCase {

	// ----------------- Fixture START --------------------//
	String _dbName;
	Context _ctx;
	DatabaseBuilder _builder;

	// ----------------- Fixture END--------------------//
	protected void setUp() throws Exception {
		super.setUp();
		_dbName = TestConst.DB_NAME;
		_ctx = getContext();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}

	public void testPreconditions() {
		assertNotNull(_dbName);
		assertNotNull(_ctx);
	}


	public void test1Insert() {
		// ----- Prepare -------
		try {
			DatabaseHelper.dropDatabase(_ctx, _dbName);
			_builder = new DatabaseBuilder(TestConst.DB_NAME, 1);
			_builder.addClass(User.class);
			_builder.addClass(UserData.class);
			_builder.addClass(Showplace.class);
			_builder.addClass(ShowplaceDetail.class);
			Database.setBuilder(_dbName, _builder);
		} catch (ActiveRecordException e1) {
			fail("Can't prepare testing DB");
		}

		// Open DB
		try {
			ActiveRecordBase con = ActiveRecordBase.open(_ctx, _dbName);

			User usr1 = con.newEntity(User.class);
			usr1.firstName = "John";
			usr1.lastName = "Smith";
			usr1.registrationDate = new Timestamp(System.currentTimeMillis());
			usr1.ssn = 1234567890;
			usr1.save();
			
			List<User> u = con.findAll(User.class);
			assertNotNull(u);
			assertEquals(1, u.size());

			// Close DB
			con.close();
		} catch (ActiveRecordException e) {
			fail(e.getLocalizedMessage());
		}


	}

	public void test2DbConnect2() {
		try {
			DatabaseHelper.dropDatabase(_ctx, _dbName);
			_builder = new DatabaseBuilder(TestConst.DB_NAME, 2);
			_builder.addClass(User.class);
			_builder.addClass(UserData.class);
			_builder.addClass(Showplace.class);
			_builder.addClass(ShowplaceDetail.class);
			Database.setBuilder(_dbName, _builder);
		} catch (ActiveRecordException e1) {
			fail("Can't prepare testing DB");
		}

		try {
			
			// Open DB #1
			Database db = Database.open(_ctx, _dbName);
			ActiveRecordBase con1 = ActiveRecordBase.createInstance(db);
			con1.open();

			// Open DB #2
			ActiveRecordBase con2 = ActiveRecordBase.open(_ctx, _dbName);

			// Find record - record not found
			User usr1 = con1.findByID(User.class, 1);
			// Record not found - create new object, set fields and save in DB
			assertNull(usr1);

			usr1 = con1.newEntity(User.class);
			usr1.firstName = "John";
			usr1.lastName = "Smith";
			usr1.registrationDate = new Timestamp(System.currentTimeMillis());
			usr1.ssn = 1234567890;
			usr1.save();

			// Now find record - record found
			User usr2 = con2.findByID(User.class, 1);
			// Compare - must be identical
			assertTrue(usr2.equals(usr1));

			// Modify fields, copy object
			usr1.ssn = 22222222;
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
		} catch (ActiveRecordException e) {
			fail(e.getLocalizedMessage());
		}
	}

	public void testDbInsert() {
	}

}
