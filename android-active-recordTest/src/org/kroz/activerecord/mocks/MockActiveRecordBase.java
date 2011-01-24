package org.kroz.activerecord.mocks;

import org.kroz.activerecord.ActiveRecordBase;
import org.kroz.activerecord.ActiveRecordException;
import org.kroz.activerecord.Database;

public class MockActiveRecordBase extends ActiveRecordBase {
	
	@Override
	public void setDatabase(Database database) {}
	
	@Override
	public long insert() throws ActiveRecordException {
		return 1;
	}
	
	@Override
	public int update() throws ActiveRecordException {
		return 0;
	}
	
	@Override
	public boolean delete() throws ActiveRecordException {
		return true;
	}
	
	@Override 
	public long save() throws ActiveRecordException {
		return this.getColumns().length;
	}

}
