package androidhive.info.materialdesign.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;




import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.DrawerLayout;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidhive.info.materialdesign.R;


public class DashboardActivity extends Activity {
	ListView listView;
	DBConnection db;
//	private NavigationDrawerFragment mNavigationDrawerFragment;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.dashboard_activity);
	db = new DBConnection(this);
	db.open();
	String userNamr = db.getuserName();
	TextView user = (TextView)findViewById(R.id.user);
	user.setText(userNamr);
	
	Button setting = (Button)findViewById(R.id.settings);
	setting.setOnClickListener(new View.OnClickListener() {
		public void onClick(View v) {
			Intent scheIntent = new Intent(DashboardActivity.this,
					SettingActivity.class);
			startActivity(scheIntent);
		}
	});
//	mNavigationDrawerFragment = (NavigationDrawerFragment) getFragmentManager()
//			.findFragmentById(R.id.navigation_drawer);
////	mTitle = getTitle();
//
//	// Set up the drawer.
//	mNavigationDrawerFragment.setUp(R.id.navigation_drawer,
//			(DrawerLayout) findViewById(R.id.drawer_layout));
	
	listView = (ListView)findViewById(R.id.dashList);
	ArrayList<String> list = new ArrayList<String>();
	list.add("Start Exam");
	list.add("Performance History");
	list.add("Review");
	list.add("My Scheduler");
	list.add("Videos");
	list.add("About");
	
	   ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
	        android.R.layout.simple_list_item_1, list);
		listView.setAdapter(adapter);
		
		listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				if (position == 0) {
					   Intent intent = new Intent(DashboardActivity.this, ModeActivity.class);
		               startActivity(intent);
				}else if (position == 1) {
					   Intent intent = new Intent(DashboardActivity.this, PerformanceActivity.class);
		               startActivity(intent);
				}else if (position == 2) {
					   Intent intent = new Intent(DashboardActivity.this, ReviewHisActivity.class);
		               startActivity(intent);
				}
			 
				
			}
        });
	
	}
	
}
