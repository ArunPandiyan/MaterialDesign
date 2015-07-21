package technibits.com.pme.activity;

import android.app.Fragment;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import technibits.com.pme.R;
import technibits.com.pme.data.NetworkUtil;
import technibits.com.pme.data.Quizdata;
import technibits.com.pme.parser.QuizJSONParser;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

import technibits.com.pme.R;


public class ReviewFragment extends Fragment {

    // Logcat tag
//    private static final String TAG = "changepassword_layout";


    ListView listView;
    ArrayList<Quizdata> data;
    ArrayList<String> list;
    int posRemove;
    ArrayAdapter<String> adapter;
    Activity activity;
    RelativeLayout notest;
    RelativeLayout yestest;

    private Button btnSignOut, btnRevokeAccess, buttonact;
    private ImageView imgProfilePic;
    private TextView txtName, txtEmail, emptylist;
    private LinearLayout llProfileLayout;

    public ReviewFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.reviewhis_activity, container, false);
        listView = (ListView) rootView.findViewById(R.id.listView1);
        notest = (RelativeLayout) rootView.findViewById(R.id.notest);
        yestest = (RelativeLayout) rootView.findViewById(R.id.yestest);
        LocalBroadcastManager.getInstance(activity).registerReceiver(mMessageReceiver,
                new IntentFilter("custom-event-name"));

        list = new ArrayList<String>();

        data = new ArrayList<Quizdata>();
        DBConnection dbConnection = new DBConnection(getActivity());
//String email=      dbConnection.getuserEmail();
        String urls = "http://www.jmbok.techtestbox.com/and/mark-for-view.php?userid=" + dbConnection.getuserEmail().trim();
        boolean status = NetworkUtil.isOnline();
        if(status) {
            AsyncTaskCall ask = new AsyncTaskCall(activity, this, urls, "reviewhis_frag");
            ask.execute(urls);//getActivity(), this, urls, "performhis_frag"
        }else{
            NetworkUtil.showNetworkstatus(getActivity());
        }


        listView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                posRemove = position;
//					if (position == 0) {
                Intent intent = new Intent(getActivity(), PreviewActivity.class);
//						   Bundle bundleObject = new Bundle();
//						   bundleObject.putSerializable("data", data);
//						   intent.putExtras(bundleObject);
                intent.putExtra("data", data);
                intent.putExtra("review", "select");
                intent.putExtra("count", 2);
                intent.putExtra("question_no", position);
                intent.putExtra("src_activity", "Review Questions");
                startActivity(intent);
//					}


            }
        });


        return rootView;

    }

    public void review(JSONObject strJson) throws JSONException {
        try {
//			JSONObject json = new JSONObject(strJson);
//			String error = json.getString("error");
//			String mes = json.getString("message");
            System.out.println("  " + strJson);
            String quizArray = strJson.getString("success");
            if (quizArray.equalsIgnoreCase("0")) {
                yestest.setVisibility(View.GONE);
                notest.setVisibility(View.VISIBLE);
            } else {
                QuizJSONParser jsonParser = new QuizJSONParser();
//			 jsonParser.jsonArrayName =  "markforviewlist";

                data = jsonParser.reviewJsonParsing(strJson);

                for (int i = 0; i < data.size(); i++) {
                    Quizdata value = data.get(i);
                    String question_cut=value.getQuestion();
                    if(question_cut.length()<30){
                        list.add(question_cut);
                    }else{ list.add(question_cut.substring(0,36)+"..."); }
                }//android.R.layout.simple_list_item_1

                adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, list);
                listView.setAdapter(adapter);
            }
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
            Snackbar.make(getView(), "Oops Some error Occured!", Snackbar.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    // Our handler for received Intents. This will be called whenever an Intent
    // with an action named "custom-event-name" is broadcasted.
    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // Get extra data included in the Intent
            String message = intent.getStringExtra("message");
            if (message != null) {
                if (message.equals("yes")) {
                    data.remove(posRemove);

                    listView.invalidateViews();
                    adapter.notifyDataSetChanged();
                }
                list.clear();
                for (int i = 0; i < data.size(); i++) {

                    Quizdata value = data.get(i);
//			  Log.d("receiver", "Got message: " + value.getQuestion());
                    list.add(value.getQuestion());
                }
//android.R.layout.simple_list_item_1
                adapter = new ArrayAdapter<String>(activity,android.R.layout.simple_list_item_1, list);
                listView.setAdapter(adapter);

//	    Log.d("receiver", "Got message: " + message);
//	    Log.d("receiver", "Got message: " + posRemove);
                if (data.size() == 0) {
                    yestest.setVisibility(View.GONE);
                    notest.setVisibility(View.VISIBLE);
                }

            } else {
                Snackbar.make(getView(), "No Change Occured", Snackbar.LENGTH_LONG).show();
                //.setAction(R.string.snackbar_action, myOnClickListener)
                // Don’t forget to show!
            }
        }

    };

    @Override
    public void onDestroy() {
        // Unregister since the activity is about to be closed.
        // This is somewhat like [[NSNotificationCenter defaultCenter] removeObserver:name:object:]
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(mMessageReceiver);
        super.onDestroy();
    }

}