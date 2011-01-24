package org.kroz.activerecord.test.fixtures;

import org.kroz.activerecord.ActiveRecordBase;
import org.kroz.activerecord.Database;

public class NumberedUser extends ActiveRecordBase {
	
	public NumberedUser() {}
	public NumberedUser(Database db) { super(db); }
	
	public String firstName;
	public String lastName;
	public String phoneNumber1;
	public String phoneNumber2;

}
