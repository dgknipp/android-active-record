package org.kroz.activerecord.test.fixtures;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.kroz.activerecord.ActiveRecordBase;
import org.kroz.activerecord.Database;
import org.kroz.activerecord.DatabaseBuilder;

/**
 * Value object representing Showplace. Populated with data received from
 * server.
 * 
 * @author Vladimir Kroz (vkroz)
 * 
 */
public class Showplace extends ActiveRecordBase {
	public Showplace() {
	}
	public Showplace(Database db) {
		super(db);
	}
	public Timestamp created;
	public Timestamp modified;
	public String name;
	public String location;
	public int latitude;
	public int longitude;
	public String short_desc;
	
}
