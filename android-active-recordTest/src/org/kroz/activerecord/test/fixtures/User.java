package org.kroz.activerecord.test.fixtures;

import java.sql.Timestamp;

import org.kroz.activerecord.ActiveRecordBase;
import org.kroz.activerecord.Database;
import org.kroz.activerecord.DatabaseBuilder;

public class User extends ActiveRecordBase {
	public User() {
	}
	public User(Database db) {
		super(db);
	}
	public String firstName;
	public String lastName;
	public Timestamp registrationDate;
	public long ssn;
	
}
