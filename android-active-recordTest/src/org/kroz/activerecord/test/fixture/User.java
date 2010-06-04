package org.kroz.activerecord.test.fixture;

import java.sql.Timestamp;

import org.kroz.activerecord.ActiveRecordBase;

public class User extends ActiveRecordBase {
	String firstName;
	String lastName;
	Timestamp registrationDate;
	long ssn;
}
