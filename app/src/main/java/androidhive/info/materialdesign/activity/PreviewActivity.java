package androidhive.info.materialdesign.activity;

import java.util.ArrayList;

import androidhive.info.materialdesign.R;
import androidhive.info.materialdesign.adapter.PreviewAdapter;
import androidhive.info.materialdesign.adapter.StudyAdapter;
import androidhive.info.materialdesign.data.Quizdata;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

public class PreviewActivity extends Activity {
	int device ;
	Button priv;
	Button next;
	boolean pCheck;
	boolean nCheck;
	Quizdata dataSource;
	int count;
	String select;
	int iNext = 0;
	ListView list;
	ArrayList<Quizdata> data;
	Context context;
	PreviewAdapter adapter;
	@SuppressWarnings("unchecked")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		context = this;
		 
		 DisplayMetrics metrics = new DisplayMetrics();
		 getWindowManager().getDefaultDisplay().getMetrics(metrics);
		 
		 int widthPixels = metrics.widthPixels;
		 int heightPixels = metrics.heightPixels;
		 float scaleFactor = metrics.density;
		 float widthDp = widthPixels / scaleFactor;
		 float heightDp = heightPixels / scaleFactor;
		 
		 float smallestWidth = Math.min(widthDp, heightDp);
		 if (smallestWidth > 720) {
			 device = 10;
		 } 
		 else if (smallestWidth >= 600) {
			 device = 7; //Device is a 7" tablet
			setContentView(R.layout.study_mode_seven);
		 }else {
			 setContentView(R.layout.study_mode);
		 }
		 
		 
//		 Bundle bundle = getIntent().getExtras();
		 list = (ListView)findViewById(R.id.listView1);
		 data = (ArrayList<Quizdata>)getIntent().getSerializableExtra("data");
		 
		 count = getIntent().getExtras().getInt("count");
		 select = getIntent().getExtras().getString("review");
		 
//		 count = data.size();
		 
		 if (data.size() > 0) {
			 dataSource = data.get(0);
			 adapter = new PreviewAdapter(context, dataSource,iNext,device,select);
//			 adapter.resData = resData;
 		    list.setAdapter(adapter);
		}
		 
		 priv = (Button)findViewById(R.id.prev);
		   next = (Button)findViewById(R.id.next);
		 Button exp = (Button)findViewById(R.id.exp);
		 list = (ListView)findViewById(R.id.listView1);
		 priv.setEnabled(false);
		 
		 if (select != null) {
			 priv.setVisibility(View.GONE);
			 exp.setVisibility(View.GONE);
			 next.setVisibility(View.GONE);
		 }
		 
		 exp.setOnClickListener(new View.OnClickListener() {
	            public void onClick(View v) {
		 AlertDialog.Builder alert = new AlertDialog.Builder(context);
		 alert.setTitle("Explanation");
		 alert.setMessage(dataSource.getDescription());
	

		 
		 
		 alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
		 public void onClick(DialogInterface dialog, int whichButton) {
		     
		     // Do something with value!
		   }
		 });
		 

		 alert.show();
	            }
	        });
		 	
	        next.setOnClickListener(new View.OnClickListener() {
	            public void onClick(View v) {
	          if (next.getText().toString().equals("Next")) {
	        	  priv.setEnabled(true);

	            	if (iNext == 0) {
	            		iNext = 1;
	            		pCheck = true;
	            		
					}
	            	if (nCheck) {
	            		iNext++;
					}
	            	if (iNext < count) {
	            		dataSource = data.get(iNext);
	            		PreviewAdapter adapter = new PreviewAdapter(context, dataSource,iNext,device,select);
	            		
		    		    list.setAdapter(adapter);
		    		    priv.setEnabled(true);
		    		    iNext++;
		    		    pCheck = true;
		    		    nCheck = false;
		    		    
					}
	            	if (iNext == count){
//						next.setEnabled(false);
	            		next.setText("Finish");
//	            		next.setBackgroundColor(Color.RED);
						iNext = count-1;
						pCheck = false;
						
						
					}

	          }else{
	        	  finish();
	          }
	           
	            
	            }


	        });
	        
	        priv.setOnClickListener(new View.OnClickListener() {
	            public void onClick(View v) {
	                // Perform action on click 
	            	if (iNext == count) {
	            		iNext = count-1;	
					}
	            	if (pCheck) {
	            		iNext--;
					}
	            	if (iNext > 0) {
	            		iNext--;
	            		dataSource = data.get(iNext);
	            		PreviewAdapter adapter = new PreviewAdapter(context, dataSource,iNext,device,select);
//	            		adapter.resData = resData;
		    		    list.setAdapter(adapter);	   
		    		    next.setEnabled(true);
		    		    if (next.getText().toString().equals("Finish")) {
		    		    	next.setText("Next");
//			    		    next.setBackgroundDrawable(priv.getBackground());
						}
		    		    nCheck =true;
		    		    pCheck = false;
		    		    
					}
	            	if (iNext == 0){
	            		priv.setEnabled(false);
	            		iNext = 0;
	            		nCheck =false;
	            		
					}
	            	
	            	
	            }
	        });


	 }
	
	@Override
	public void onBackPressed() {
		super.onBackPressed();
		sendMessage();
	}
	
	private void sendMessage() {
		  Log.d("sender", "Broadcasting message");
		  Intent intent = new Intent("custom-event-name");
		  // You can also include some extra data.
		  intent.putExtra("message", adapter.ansOption);
		  LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
		}

}
