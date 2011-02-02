package org.kroz.activerecord;

import org.kroz.activerecord.mocks.MockActiveRecordBase;
import org.kroz.activerecord.test.fixtures.TestConst;

import android.content.Context;
import android.test.AndroidTestCase;

public class ActiveRecordTestCase extends AndroidTestCase {
	
	protected String _dbName = TestConst.DB_NAME;
	protected int _dbVersion = TestConst.DB_VERSION1;
	protected Context _ctx;
	protected DatabaseBuilder _builder;
	protected ActiveRecordBase connection;
	
	public void setUp() throws Exception {
		this.setUp(new Class[0], true);
	}
	
	public void setUp(Class[] builderClasses, boolean establishConnection) throws Exception {
		super.setUp();
		_ctx = getContext();
		_builder = new DatabaseBuilder(_dbName);
		for(Class<ActiveRecordBase> klass : builderClasses) {
			_builder.addClass(klass);
		}
		Database.setBuilder(_builder);
		
		if(establishConnection) {
			try {
				connection = ActiveRecordBase.open(_ctx, _dbName, _dbVersion);
			} catch (ActiveRecordException e) {
				fail(e.getLocalizedMessage());
			}
		}
	}
	
	public void setUp(Class[] builderClasses) throws Exception {
		this.setUp(builderClasses, true);
	}
	
	public void tearDown() throws Exception {
		super.tearDown();
		DatabaseHelper.dropDatabase(_ctx, _dbName);
	}
	
	public ActiveRecordBase getConnection(boolean mockConnection) {
		ActiveRecordBase conn = null;
		if(mockConnection) {
			conn = new MockActiveRecordBase();
		} else {
			try {
				conn = ActiveRecordBase.open(_ctx, _dbName, _dbVersion);
			} catch (ActiveRecordException e) {
				fail(e.getLocalizedMessage());
			}
		}
		return conn;
	}

}
