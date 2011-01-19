/**
 * 
 */
package org.kroz.activerecord;

import java.lang.reflect.Field;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.kroz.activerecord.test.fixtures.TestConst;
import org.kroz.activerecord.test.fixtures.User;
import org.kroz.activerecord.test.fixtures.UserData;

import android.content.Context;
import android.test.AndroidTestCase;

/**
 * @author VKROZ
 * 
 */
public class EntityTest extends AndroidTestCase {

	// ----------------- Fixture START --------------------//
	int counter;
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
		
		_builder = new DatabaseBuilder(TestConst.DB_NAME);
		_builder.addClass(User.class);
		_builder.addClass(UserData.class);
		Database.setBuilder(_builder);
	}

	protected void tearDown() throws Exception {
		super.tearDown();
		// -- We want a clean run in between each test -- //
		DatabaseHelper.dropDatabase(_ctx, _dbName);
	}

	/**
	 * Test method for {@link org.kroz.activerecord.ActiveRecordBase#test()}.
	 */
	public void testCreateEntity() {
		ActiveRecordBase conn = null;

		// Open DB
		try {
			conn = ActiveRecordBase.open(_ctx, _dbName, _dbVersion);
		} catch (ActiveRecordException e) {
			fail(e.getLocalizedMessage());
		}

		// Insert records
		try {
			User usr1 = conn.newEntity(User.class);
			usr1.firstName = "John";
			usr1.lastName = "Smith";
			usr1.registrationDate = new Timestamp(System.currentTimeMillis());
			usr1.ssn = 1234567890;
			usr1.save();

			User usr2 = conn.newEntity(User.class);
			usr2.firstName = "John2";
			usr2.save();

			usr2.firstName = "Some3";
			usr2.save();

		} catch (ActiveRecordException e) {
			fail(e.getLocalizedMessage());
		}

		// Find all records
		try {
			List<User> u = conn.findAll(User.class);
			assertNotNull(u);
			assertEquals(2, u.size());
		} catch (ActiveRecordException e) {
			fail(e.getLocalizedMessage());
		}

		// Close DB
		conn.close();

	}

	public void testCreateEntityUsingMap() throws Exception {
		 Map<String, Object> createEntityValues = new HashMap<String, Object>();
		 createEntityValues.put("firstName", "John");
		 createEntityValues.put("lastName", "Smith");
		 createEntityValues.put("registrationDate", new Timestamp(123456));
		 createEntityValues.put("ssn", new Long(123456789));

		 User johnSmith = new User(createEntityValues);

		 assertEquals("John", johnSmith.firstName);
		 assertEquals("Smith", johnSmith.lastName);
		 assertEquals(new Timestamp(123456), johnSmith.registrationDate);
		 assertEquals(123456789, johnSmith.ssn);
	}

	/**
	 * Test method for {@link org.kroz.activerecord.ActiveRecordBase#test()}.
	 */
	public void testFindEntity() {
		// Open DB
		try {

			ActiveRecordBase conn = ActiveRecordBase.open(_ctx, _dbName, _dbVersion);
			List<User> u = conn.findAll(User.class);
			assertNotNull(u);
			conn.close();
		} catch (ActiveRecordException e) {
			fail(e.getLocalizedMessage());
		}

	}

}
