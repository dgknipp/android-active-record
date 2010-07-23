package org.kroz.activerecord;

import java.lang.reflect.Array;

import org.kroz.activerecord.test.fixtures.TestConst;
import org.kroz.activerecord.test.fixtures.User;
import org.kroz.activerecord.test.fixtures.UserData;

import android.content.Context;
import android.test.AndroidTestCase;

public class SchemaInitTest extends AndroidTestCase {

	String _dbName;
	Context _ctx = getContext();

	protected void setUp() throws Exception {
		super.setUp();
		_dbName = TestConst.DB_NAME;
		_ctx = getContext();
		DatabaseHelper.dropDatabase(_ctx, _dbName);
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}

	public void testPreconditions() {
	}

	public void test1SchemaCreateAndAlter() {

		DatabaseBuilder b1 = new DatabaseBuilder(_dbName);
		b1.addClass(User.class);
		b1.addClass(UserData.class);
		Database.setBuilder(b1);

		Database db;
		try {
			db = Database.createInstance(_ctx, _dbName, 1);
			db.open();
			String[] actual = db.getTables();
			String[] expected = { "USER", "USER_DATA", "android_metadata" };
			assertEquals(expected.length, actual.length);

			java.util.Arrays.sort(actual);
			java.util.Arrays.sort(expected);
			for (int i = 0; i < actual.length; i++) {
				assertEquals(expected[i], actual[i]);
			}
			db.close();
		} catch (ActiveRecordException e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}
}
