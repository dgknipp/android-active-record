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
	int _dbVersion;
	Context _ctx;
	DatabaseBuilder _builder;

	// ----------------- Fixture END--------------------//
	protected void setUp() throws Exception {
		super.setUp();
		_dbName = TestConst.DB_NAME;
		_dbVersion = TestConst.DB_VERSION1;
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
		// Drop DB at the beginning of the test set
		DatabaseHelper.dropDatabase(_ctx, _dbName);

		// ----- Prepare -------
		_builder = new DatabaseBuilder(TestConst.DB_NAME);
		_builder.addClass(User.class);
		_builder.addClass(UserData.class);
		Database.setBuilder(_builder);

		// Open DB
		try {
			ActiveRecordBase con = ActiveRecordBase.open(_ctx, _dbName, _dbVersion);

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
		_builder = new DatabaseBuilder(TestConst.DB_NAME);
		_builder.addClass(User.class);
		_builder.addClass(UserData.class);
		_builder.addClass(Showplace.class);
		_builder.addClass(ShowplaceDetail.class);
		Database.setBuilder(_builder);

		try {

			// Open DB #1
			Database db = Database.open(_ctx, _dbName, _dbVersion);
			ActiveRecordBase con1 = ActiveRecordBase.createInstance(db);
			con1.open();

			// Open DB #2
			ActiveRecordBase con2 = ActiveRecordBase.open(_ctx, _dbName, _dbVersion);

			// Find record - record not found
			User usr1 = con1.findByID(User.class, 1);
			// Record not found - create new object, set fields and save in DB
			assertNotNull(usr1);
			assertEquals(1234567890, usr1.ssn);

			usr1 = con1.newEntity(User.class);
			usr1.firstName = "Vova";
			usr1.lastName = "Kroz";
			usr1.registrationDate = new Timestamp(System.currentTimeMillis());
			usr1.ssn = 222333444;
			usr1.save();

			// Now find record - record found
			User usr2 = con2.findByID(User.class, usr1._id);
			// Compare - must be identical
			assertEquals(usr2._id, usr1._id);

			// Modify fields, copy object
			usr1.ssn = 22222222;
			// Save record
			usr2.save();

			// Save record - do nothing
			usr2.save();

			// Close DB
			con1.close();
			con2.close();

		} catch (ActiveRecordException e) {
			fail(e.getLocalizedMessage());
		}
	}

}
