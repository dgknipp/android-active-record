package org.kroz.aar;

import org.kroz.aar.enities.User;
import org.kroz.aar.enities.UserItems;
import org.kroz.activerecord.ActiveRecordBase;
import org.kroz.activerecord.ActiveRecordException;
import org.kroz.activerecord.Database;
import org.kroz.activerecord.DatabaseBuilder;

import android.app.Application;

/**
 * Encapsualtes application level resources
 * 
 * @author vkroz
 * 
 */
public class AarExample extends Application {

	public ActiveRecordBase mDatabase;

	public AarExample() {

	}

	@Override
	public void onCreate() {
		super.onCreate();

		// --------- Prepare mDatabase connection ----------
		DatabaseBuilder builder = new DatabaseBuilder(Const.DATABASE_NAME);
		builder.addClass(User.class);
		builder.addClass(UserItems.class);
		Database.setBuilder(builder);
		try {
			mDatabase = ActiveRecordBase.open(this, Const.DATABASE_NAME,
					Const.DATABASE_VERSION);
		} catch (ActiveRecordException e) {
			e.printStackTrace();
		}

	}
}
