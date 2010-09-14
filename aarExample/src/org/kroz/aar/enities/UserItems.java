package org.kroz.aar.enities;

import java.sql.Timestamp;

import org.kroz.activerecord.ActiveRecordBase;

/**
 * UserItems entity. Represents record stored in SQLite database
 * 
 * @author Vladimir Kroz
 * 
 */
public class UserItems extends ActiveRecordBase {
	public int userId;
	public String description;
	public String price;
	public Timestamp purchaseDate;
	public String status;
	public Timestamp created;
	public Timestamp modified;

}
