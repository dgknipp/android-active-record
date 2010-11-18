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

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("UserItems [_id=").append(_id).append(", userId=")
				.append(userId).append(", description=").append(description)
				.append(", price=").append(price).append(", purchaseDate=")
				.append(purchaseDate).append(", status=").append(status)
				.append(", created=").append(created).append(", modified=")
				.append(modified).append("]");
		return builder.toString();
	}

}
