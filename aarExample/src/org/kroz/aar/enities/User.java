package org.kroz.aar.enities;

import java.sql.Timestamp;

import org.kroz.activerecord.ActiveRecordBase;

/**
 * User entity. Represents record stored in SQLite database
 * 
 * @author Vladimir Kroz
 * 
 */
public class User extends ActiveRecordBase {
	public String name;
	public String location;
	public String status;
	public String address;
	public String phone;
	public String workHours;
	public Timestamp created;
	public Timestamp modified;

	public User() {
	}
}
