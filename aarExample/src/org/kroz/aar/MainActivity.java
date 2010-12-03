package org.kroz.aar;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Entry point
 * 
 * @author VKROZ
 * 
 */
public class MainActivity extends ListActivity {

	static final String[] ITEMS = new String[] { "Basic CRUD test", "Expandable List" };

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_activity);

		setListAdapter(new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, ITEMS));

		ListView lv = getListView();
		lv.setTextFilterEnabled(true);

		lv.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				switch (position) {
				case 0:
					startActivity(new Intent(MainActivity.this
							.getApplicationContext(), CrudExample1Activity.class));
					break;
				case 1:
					startActivity(new Intent(MainActivity.this
							.getApplicationContext(), ExpandableList1Activity.class));
					break;
				}
//				// When clicked, show a toast with the TextView text
//				Toast.makeText(getApplicationContext(),
//						((TextView) view).getText(), Toast.LENGTH_SHORT).show();
			}
		});
	}

}