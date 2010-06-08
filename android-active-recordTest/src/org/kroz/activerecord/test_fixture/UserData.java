package org.kroz.activerecord.test_fixture;

import java.sql.Timestamp;

import org.kroz.activerecord.ActiveRecordBase;
import org.kroz.activerecord.Database;

public class UserData extends ActiveRecordBase {
	protected UserData(Database db) {
		super(db);
	}
	int userId;
	int purchaseId;
	Timestamp purchaseDate;
	String description;
}
