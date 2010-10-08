package org.kroz.aar.enities;

import java.sql.Timestamp;

import org.kroz.activerecord.ActiveRecordBase;

/**
 * User entity. Example entity. Class name corresponds to a database table;
 * class attributes correspond to table fields.
 * 
 * @author Vladimir Kroz
 * 
 */
public class User extends ActiveRecordBase {
	public String firstName;
	public String lastName;
	public double balanse;
	public int age;
	public boolean active;
	public Timestamp birthday;

	public User() {
	}
}
