package org.kroz.activerecord.test.fixtures.manytomany;

import java.util.List;

import org.kroz.activerecord.ActiveRecordBase;
import org.kroz.activerecord.Database;
import org.kroz.activerecord.annotations.ActiveRecordIgnoreAttribute;

public class Driver extends ActiveRecordBase {
	
	public Driver() {}
	public Driver(Database db) { super(db); }
	
	public String firstName;
	public String lastName;
	@ActiveRecordIgnoreAttribute
	public List<Car> cars;
}
