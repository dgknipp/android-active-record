package org.kroz.aar;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import org.kroz.aar.enities.User;
import org.kroz.activerecord.ActiveRecordBase;
import org.kroz.activerecord.ActiveRecordException;
import org.kroz.activerecord.EntitiesHelper;
import org.kroz.activerecord.utils.Logg;

import android.app.ListActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Class under construction
 * 
 * @author VKROZ
 * 
 */
public class CrudExample1Activity extends ListActivity {
	// Convenience variables to use with logger
	static final String TAG = Const.TAG;
	static final String CNAME = CrudExample1Activity.class.getSimpleName();

	static final User[] DUMMY = new User[10];

	ActiveRecordBase _db;
	List<User> _users;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.example_activity);

		// Purge table and populate with test data
		initDb();

		setListAdapter(new ArrayAdapter<User>(this,
				android.R.layout.simple_list_item_1, _users));

		ListView lv = getListView();
		lv.setTextFilterEnabled(true);

		lv.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// When clicked, show a toast with the TextView text
				Toast.makeText(getApplicationContext(),
						((TextView) view).getText(), Toast.LENGTH_SHORT).show();
			}
		});
	}

	/**
	 * Purge table and populate with tets data
	 */
	private void initDb() {
		try {
			// Open database
			_db = ActiveRecordBase.open(this, Const.DATABASE_NAME,
					Const.DATABASE_VERSION);

			// purge Users table
			_db.delete(User.class, null, null);

			// Insert DUMMY array into Users table
			for (User user : DUMMY) {
				User u = _db.newEntity(User.class);
				EntitiesHelper.copyFieldsWithoutID(u, user);
				u.save();
			}

			_users = _db.find(User.class, null, null);

		} catch (ActiveRecordException e) {
			Logg.e(TAG, e, "(%t) %s.initDb(): Error=%s", CNAME, e.getMessage());
		}
	}

	public void addRecord(View v) {
		Toast.makeText(this, "Under construction", Toast.LENGTH_LONG).show();
	}

	public void deleteRecord(View v) {
		Toast.makeText(this, "Under construction", Toast.LENGTH_LONG).show();
	}

	public void populateDb(View v) {
		Toast.makeText(this, "Under construction", Toast.LENGTH_LONG).show();
	}

	public void finishActivity(View view) {
		finish();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (null != _db) {
			_db.close();
		}
	}

	static {
		Calendar cal = GregorianCalendar.getInstance();
		cal.set(1967, 4, 8);
		DUMMY[0] = new User("John", "Smith", 0.0, 30, true, new Timestamp(
				cal.getTimeInMillis()));
		cal.set(1970, 1, 1, 12, 01);
		DUMMY[1] = new User("Mary", "Smith", 100.0, 1, true, new Timestamp(
				cal.getTimeInMillis()));
		cal.set(1967, 4, 8, 1, 1, 1);
		DUMMY[2] = new User("Bill", "Gates", 123456.78, 2, true, new Timestamp(
				cal.getTimeInMillis()));
		cal.set(2000, 12, 12, 12, 12);
		DUMMY[3] = new User("Barak", "Obama", 0.99, 3, true, new Timestamp(
				cal.getTimeInMillis()));
		cal.set(1967, 7, 7);
		DUMMY[4] = new User("Pol", "McCartney", 1000000.0, 22, false,
				new Timestamp(cal.getTimeInMillis()));
		cal.set(1980, 8, 8, 8, 8);
		DUMMY[5] = new User("Linda", "Hamilton", 999999.99, 8, true,
				new Timestamp(cal.getTimeInMillis()));
		cal.set(1967, 4, 8);
		DUMMY[6] = new User("Mr", "Big", 0.0, 13, true, new Timestamp(
				cal.getTimeInMillis()));
		cal.set(1967, 4, 8);
		DUMMY[7] = new User("Captain", "Joe", -123.45, 99, false,
				new Timestamp(cal.getTimeInMillis()));
		cal.set(1967, 4, 8);
		DUMMY[8] = new User("Mary", "Poppins", 12.12, 21, true, new Timestamp(
				cal.getTimeInMillis()));
		cal.set(1967, 4, 8);
		DUMMY[9] = new User("Sir", "D'Artanyan", 333.222, 5, true,
				new Timestamp(cal.getTimeInMillis()));
	}
}