package org.kroz.activerecord.test.fixtures;

import java.sql.Timestamp;

import org.kroz.activerecord.ActiveRecordBase;
import org.kroz.activerecord.Database;
import org.kroz.activerecord.DatabaseBuilder;

public class UserData extends ActiveRecordBase {
	public UserData() {
	}
	public UserData(Database db) {
		super(db);
	}
	String description;
	Timestamp purchaseDate;
	int purchaseId;
	int userId;

}
