package org.kroz.activerecord;

import org.kroz.activerecord.test.fixtures.manytomany.Car;
import org.kroz.activerecord.test.fixtures.manytomany.Driver;

public class ManyToManyTest extends ActiveRecordTestCase {
	
	Class[] classes = {Car.class, Driver.class};
	
	public void setUp() throws Exception {
		super.setUp(classes);
	}
	
	public void tearDown() throws Exception {
		super.tearDown();
	}
	
	public void testCreateCarDrivers() throws Exception {
		ActiveRecordBase conn = getConnection(false);
	}

}
