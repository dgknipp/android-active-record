package org.kroz.activerecord.test_fixture;

import java.sql.Timestamp;

import org.kroz.activerecord.ActiveRecordBase;
import org.kroz.activerecord.Database;

public class User extends ActiveRecordBase {
	protected User(Database db) {
		super(db);
	}
	String firstName;
	String lastName;
	Timestamp registrationDate;
	long ssn;
}
