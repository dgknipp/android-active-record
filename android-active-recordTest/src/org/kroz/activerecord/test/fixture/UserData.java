package org.kroz.activerecord.test.fixture;

import java.sql.Timestamp;

import org.kroz.activerecord.ActiveRecordBase;

public class UserData extends ActiveRecordBase {
	int userId;
	int purchaseId;
	Timestamp purchaseDate;
	String description;
}
