package androidhive.info.materialdesign.activity;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import androidhive.info.materialdesign.R;
import androidhive.info.materialdesign.data.Quizdata;
import androidhive.info.materialdesign.parser.QuizJSONParser;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class ReviewHisActivity extends Activity{
	ListView listView;
	ArrayList<Quizdata> data;
	ArrayList<String> list;
	int posRemove;
	ArrayAdapter<String> adapter;
	Activity activity;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.reviewhis_activity);
		activity = this;
		listView = (ListView)findViewById(R.id.listView1);
		
		  LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver,
			      new IntentFilter("custom-event-name"));
		
		list = new ArrayList<String>();
	
		data = new ArrayList<Quizdata>();
 
			String urls = "http://www.jmbok.avantgoutrestaurant.com/and/mark-for-view.php?userid=android@gmail.com";
			AsyncTaskCall ask = new AsyncTaskCall(this, urls,"reviewhis");
			ask.execute(urls);
			
			listView.setOnItemClickListener(new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					posRemove = position;
//					if (position == 0) {
						   Intent intent = new Intent(ReviewHisActivity.this, PreviewActivity.class);
//						   Bundle bundleObject = new Bundle();
//						   bundleObject.putSerializable("data", data);
//						   intent.putExtras(bundleObject);
						   intent.putExtra("data", data);
						   intent.putExtra("review", "select");
						   intent.putExtra("count", 1);
			               startActivity(intent);
//					}
				 
					
				}
	        });


}
	public void review(JSONObject strJson)throws JSONException {
		try {
//			JSONObject json = new JSONObject(strJson);
//			String error = json.getString("error");
//			String mes = json.getString("message");
			System.out.println("  " + strJson );
			
			 QuizJSONParser jsonParser = new QuizJSONParser();
//			 jsonParser.jsonArrayName =  "markforviewlist";
			  data = jsonParser.reviewJsonParsing(strJson);
			  
			  for (int i = 0; i < data.size(); i++) {
				  Quizdata value = data.get(i);
				  list.add(value.getQuestion());
			}
			  
			   adapter = new ArrayAdapter<String>(this,
				        android.R.layout.simple_list_item_1, list);
					listView.setAdapter(adapter);
			  
//			if (error.equals("true")) {
//				Toast.makeText(getApplicationContext(), mes,
//						   Toast.LENGTH_LONG).show();
////				finish();
//			}else {
//				Toast.makeText(getApplicationContext(), mes,
//						   Toast.LENGTH_LONG).show();
//				
//				Intent intent = new Intent(LoginActivity.this, DashboardActivity.class);
//	            startActivity(intent);
//				
//				finish();
//				
//			}
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	
	// Our handler for received Intents. This will be called whenever an Intent
	// with an action named "custom-event-name" is broadcasted.
	private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
	  @Override
	  public void onReceive(Context context, Intent intent) {
	    // Get extra data included in the Intent
	    String message = intent.getStringExtra("message");
	    if (message.equals("yes")) {
	    	data.remove(posRemove);
	    	listView.invalidateViews();
//	    	adapter.notifyDataSetChanged();
		}
	       list.clear();
		  for (int i = 0; i < data.size(); i++) {
			  
			  Quizdata value = data.get(i);
//			  Log.d("receiver", "Got message: " + value.getQuestion());
			  list.add(value.getQuestion());
		}
		  
		   adapter = new ArrayAdapter<String>(activity,
			        android.R.layout.simple_list_item_1, list);
				listView.setAdapter(adapter);
				
//	    Log.d("receiver", "Got message: " + message);
//	    Log.d("receiver", "Got message: " + posRemove);
	    
	  }
	};

	@Override
	protected void onDestroy() {
	  // Unregister since the activity is about to be closed.
	  // This is somewhat like [[NSNotificationCenter defaultCenter] removeObserver:name:object:] 
	  LocalBroadcastManager.getInstance(this).unregisterReceiver(mMessageReceiver);
	  super.onDestroy();
	}
}
