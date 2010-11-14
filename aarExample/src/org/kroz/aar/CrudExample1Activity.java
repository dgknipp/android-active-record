package org.kroz.aar;

import android.app.ListActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Class under construction 
 * @author VKROZ
 *
 */
public class CrudExample1Activity extends ListActivity {
	
	static final String[] DUMMY = new String[] {
	    "Afghanistan", "Albania", "Algeria", "American Samoa", "Andorra",
	    "Angola", "Anguilla", "Antarctica", "Antigua and Barbuda", "Argentina",
	    "Armenia", "Aruba", "Australia", "Austria", "Azerbaijan",
	    "Bahrain", "Bangladesh", "Barbados", "Belarus", "Belgium"};
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.example_activity);
        
        setListAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, DUMMY));

        ListView lv = getListView();
        lv.setTextFilterEnabled(true);

        lv.setOnItemClickListener(new OnItemClickListener() {
          public void onItemClick(AdapterView<?> parent, View view,
              int position, long id) {
            // When clicked, show a toast with the TextView text
            Toast.makeText(getApplicationContext(), ((TextView) view).getText(),
                Toast.LENGTH_SHORT).show();
          }
        });
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
}