/**
 * 
 */
package org.kroz.activerecord;

import java.sql.Timestamp;
import java.util.List;

import org.kroz.activerecord.ActiveRecordBase;
import org.kroz.activerecord.test.fixtures.TestConst;
import org.kroz.activerecord.test.fixtures.User;
import org.kroz.activerecord.test.fixtures.UserData;

import android.content.Context;
import android.test.AndroidTestCase;

/**
 * Validates basic Create/Update/Delete operations on single DB entity
 */
public class TimestampTest extends AndroidTestCase {

	// ----------------- Fixture START --------------------//
	int counter;
	String _dbName;
	Context _ctx;
	DatabaseBuilder _builder;

	// ----------------- Fixture END--------------------//

	protected void setUp() throws Exception {
		super.setUp();
		_dbName = TestConst.DB_NAME;
		_ctx = getContext();
		_builder = new DatabaseBuilder(TestConst.DB_NAME);
		_builder.addClass(User.class);
		_builder.addClass(UserData.class);
		Database.setBuilder(_builder);
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}

	/**
	 * Test method for {@link org.kroz.activerecord.ActiveRecordBase#test()}.
	 */
	public void testCreateEntity() {
		 // Drop database before test run, to start with clean schema. New DB
		 // will be created on first ActiveRecordBase.open() call
		DatabaseHelper.dropDatabase(_ctx, _dbName);

		try {
			ActiveRecordBase conn = ActiveRecordBase.open(_ctx, _dbName, 1);

			User usr1 = conn.newEntity(User.class);
			usr1.firstName = "John";
			usr1.lastName = "Smith";
			usr1.registrationDate = new Timestamp(System.currentTimeMillis());
			usr1.ssn = 1234567890;
			usr1.save();

			User usr2 = conn.newEntity(User.class);
			usr2.firstName = "Bill";
			usr2.lastName = "Gates";
			usr2.registrationDate = new Timestamp(1967, 4, 8, 5, 1, 2, 0);
			usr2.ssn = 1234567890;
			usr2.save();

			conn.close();
		} catch (ActiveRecordException e) {
			fail(e.getLocalizedMessage());
		}

	}

	/**
	 * Test method for {@link org.kroz.activerecord.ActiveRecordBase#test()}.
	 */
	public void testFindEntity() {
		try {

			ActiveRecordBase conn = ActiveRecordBase.open(_ctx, _dbName, 1);
			List<User> ul = conn.findAll(User.class);
			assertEquals(2, ul.size());

			User u = ul.get(1);
			assertEquals(new Timestamp(1967, 4, 8, 5, 1, 2, 0),
					u.registrationDate);
			conn.close();
		} catch (ActiveRecordException e) {
			fail(e.getLocalizedMessage());
		}

	}

}
