package org.kroz.activerecord.test_fixture;

import org.kroz.activerecord.DatabaseBuilder;

public class MyDatabaseBuilder extends DatabaseBuilder {

	public MyDatabaseBuilder(int currentVersion) {
		super();
		this.addClass(User.class);
		this.addClass(UserData.class);
		this.addClass(Showplace.class);
		this.addClass(ShowplaceDetail.class);
	}

}
