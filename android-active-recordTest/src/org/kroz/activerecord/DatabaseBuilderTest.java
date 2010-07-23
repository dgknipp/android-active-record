package org.kroz.activerecord;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.kroz.activerecord.test.fixtures.Showplace;
import org.kroz.activerecord.test.fixtures.ShowplaceDetail;
import org.kroz.activerecord.test.fixtures.TestConst;
import org.kroz.activerecord.test.fixtures.User;
import org.kroz.activerecord.test.fixtures.UserData;

import android.test.AndroidTestCase;

public class DatabaseBuilderTest extends AndroidTestCase {

	List<String> _tables;
	int _version;

	protected void setUp() throws Exception {
		super.setUp();
		_tables = new ArrayList<String>();
		_tables.add("USER");
		_tables.add("USER_DATA");
		_tables.add("SHOWPLACE");
		_tables.add("SHOWPLACE_DETAIL");

		int _version = 1;
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}

	public void testGetTables() {
		DatabaseBuilder builder = new DatabaseBuilder(TestConst.DB_NAME);
		builder.addClass(User.class);
		builder.addClass(UserData.class);
		builder.addClass(Showplace.class);
		builder.addClass(ShowplaceDetail.class);

		String[] arr = builder.getTables();
		for (String t : arr) {
			assertTrue(t + " not found", _tables.contains(t));
		}
	}

	public void testOnUpdate() {
		DatabaseBuilder builder = new DatabaseBuilder(TestConst.DB_NAME);
		builder.addClass(User.class);
		builder.addClass(UserData.class);
		builder.addClass(Showplace.class);
		builder.addClass(ShowplaceDetail.class);

		String actual = builder.getSQLDrop("USER_DATA");
		String expected = "DROP TABLE IF EXISTS user_data";
		assertEquals("Got string: " + actual, expected.toLowerCase(), actual.toLowerCase());
	}

	public void testOnCreate() {
		DatabaseBuilder builder = new DatabaseBuilder(TestConst.DB_NAME);
		builder.addClass(User.class);
		builder.addClass(UserData.class);
		builder.addClass(Showplace.class);
		builder.addClass(ShowplaceDetail.class);

		try {
			String actual = builder.getSQLCreate("USER_DATA");
			String expected = "CREATE TABLE USER_DATA (_id integer primary key, DESCRIPTION text, PURCHASE_DATE int, PURCHASE_ID int, USER_ID int)";
			assertEquals("Got string: " + actual, expected.toLowerCase(), actual
					.toLowerCase());
		} catch (ActiveRecordException e) {
			fail(e.getLocalizedMessage());
		}
	}

}
