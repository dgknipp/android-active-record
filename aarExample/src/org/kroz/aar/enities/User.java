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
	public int rank;
	public boolean active;
	public Timestamp birthday;

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		return sb.append(_id).append(" ").append(firstName).append(" ")
				.append(lastName).toString();
	}

	public User() {
	}

	public User(String firstName, String lastName, double balanse, int rank,
			boolean active, Timestamp birthday) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.balanse = balanse;
		this.rank = rank;
		this.active = active;
		this.birthday = birthday;
	}

	public User(long id, String firstName, String lastName, double balanse,
			int rank, boolean active, Timestamp birthday) {
		_id = id;
		this.firstName = firstName;
		this.lastName = lastName;
		this.balanse = balanse;
		this.rank = rank;
		this.active = active;
		this.birthday = birthday;
	}

}
