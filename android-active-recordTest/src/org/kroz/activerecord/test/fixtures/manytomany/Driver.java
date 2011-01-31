package org.kroz.activerecord.test.fixtures.manytomany;

import java.util.List;

import org.kroz.activerecord.ActiveRecordBase;
import org.kroz.activerecord.Database;

public class Driver extends ActiveRecordBase {
	
	public Driver() {}
	public Driver(Database db) { super(db); }
	
	public String firstName;
	public String lastName;
	public List<Car> cars;
}
