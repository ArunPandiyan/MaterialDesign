package androidhive.info.materialdesign.activity;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.IntentCompat;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

import androidhive.info.materialdesign.R;

public class SettingActivity extends Activity {
	ListView listView;
	DBConnection db;
//	private NavigationDrawerFragment mNavigationDrawerFragment;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.settings);
	db = new DBConnection(this);
	db.open();
	listView = (ListView)findViewById(R.id.dashList);
	ArrayList<String> list = new ArrayList<String>();
	list.add("Edit Profile");
	list.add("Logout");

	
	   ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
	        android.R.layout.simple_list_item_1, list);
		listView.setAdapter(adapter);
		
		listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				if (position == 0) {
					 Intent intent = new Intent(SettingActivity.this, CreateAccountActivity.class);
		             startActivity(intent);
		               
				}else if (position == 1) {
		               db.dropTable();
//		               ActivityCompat.finishAffinity(null);
					   Intent intent = new Intent(SettingActivity.this, LoginActivity.class);
					   intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | IntentCompat.FLAG_ACTIVITY_CLEAR_TASK);
		               startActivity(intent);
		               finish();
				}else if (position == 2) {
//					   Intent intent = new Intent(SettingActivity.this, ReviewHisActivity.class);
//		               startActivity(intent);
				}
			 
				
			}
        });
	
	}
	
}
