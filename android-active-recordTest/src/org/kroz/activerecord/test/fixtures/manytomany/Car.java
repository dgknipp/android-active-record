package org.kroz.activerecord.test.fixtures.manytomany;

import java.util.List;

import org.kroz.activerecord.ActiveRecordBase;
import org.kroz.activerecord.Database;
import org.kroz.activerecord.annotations.ActiveRecordIgnoreAttribute;

public class Car extends ActiveRecordBase {
	
	public Car() {}
	public Car(Database db) { super(db); }
	
	public String manufacturer;
	public String model;
	@ActiveRecordIgnoreAttribute
	public List<Driver> drivers;
}
