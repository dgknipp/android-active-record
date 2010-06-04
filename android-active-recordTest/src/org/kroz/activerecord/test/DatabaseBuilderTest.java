package org.kroz.activerecord.test;

import org.kroz.activerecord.DatabaseBuilder;
import org.kroz.activerecord.test.fixture.User;
import org.kroz.activerecord.test.fixture.UserData;

import android.test.AndroidTestCase;

public class DatabaseBuilderTest extends AndroidTestCase {

	protected void setUp() throws Exception {
		super.setUp();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}

	public void testAddClass() {
//		fail("Not yet implemented");
	}

	public void testGetTables() {
		DatabaseBuilder dbb = new DatabaseBuilder(1);
		dbb.addClass(User.class).addClass(UserData.class);
		String[] arr = dbb.getTables();
		assertEquals("Expected USER_DATA but got "+arr[0], "USER_DATA", arr[0]);
		assertEquals("Expected USER but got "+arr[1], "USER", arr[1]);
	}

	public void testGetSQLCreate() {
//		fail("Not yet implemented");
	}

	public void testGetSQLDrop() {
//		fail("Not yet implemented");
	}

}
