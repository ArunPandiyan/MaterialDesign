package technibits.com.pme.activity;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import technibits.com.pme.R;
import technibits.com.pme.adapter.PerformanceAdapter;
import technibits.com.pme.data.NetworkUtil;
import technibits.com.pme.data.Quizdata;
import technibits.com.pme.data.ResultData;
import technibits.com.pme.parser.QuizJSONParser;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ListView;

public class PerformanceActivity extends Activity {
    ListView listView;
    ArrayList<ResultData> rsuData;
    ArrayList<Quizdata> data;
    int device;
    DBConnection db;
    String useremail = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.performence_activity);
        listView = (ListView) findViewById(R.id.listView1);
        db = new DBConnection(getApplicationContext());
        useremail = db.getuserEmail().trim();
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);

        int widthPixels = metrics.widthPixels;
        int heightPixels = metrics.heightPixels;
        float scaleFactor = metrics.density;
        float widthDp = widthPixels / scaleFactor;
        float heightDp = heightPixels / scaleFactor;

        float smallestWidth = Math.min(widthDp, heightDp);
        View rootView = null;
        if (smallestWidth > 720) {
            device = 10;
        } else if (smallestWidth >= 600) {
            device = 7; //Device is a 7" tablet
        }


        String urls = "http://jmbok.techtestbox.com/and/performance-history.php?userid=" + useremail;
        boolean status = NetworkUtil.isOnline();
        if(status) {
            AsyncTaskCall ask = new AsyncTaskCall(this, urls, "performhis");
            ask.execute(urls);
        }else{
            NetworkUtil.showNetworkstatus(this);
        }


    }

    public void performReview(int id) {

        String urls = "http://jmbok.techtestbox.com/and/performance_review1.php?testid=" + id;
        boolean status = NetworkUtil.isOnline();
        if(status) {
            AsyncTaskCall ask = new AsyncTaskCall(this, urls, "perreview");
            ask.execute(urls);
        }else{
            NetworkUtil.showNetworkstatus(this);
        }

    }


    public void performReviewJSON(JSONObject strJson) throws JSONException {
        try {
//			JSONObject json = new JSONObject(strJson);
//			String error = json.getString("error");
//			String mes = json.getString("message");


            QuizJSONParser jsonParser = new QuizJSONParser();
            jsonParser.jsonArrayName = "markforviewlist";
            data = jsonParser.performReviewJsonParsing(strJson);
//			  
//			  for (int i = 0; i < data.size(); i++) {
//				  Quizdata value = data.get(i);
//				  list.add(value.getQuestion());
//			}
            int count = data.size();
            Intent intent = new Intent(PerformanceActivity.this, PreviewActivity.class);
            intent.putExtra("data", data);
            intent.putExtra("count", count);
            intent.putExtra("src_activity", "Performance History");
            startActivity(intent);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void performHis(JSONObject strJson) throws JSONException {
        try {
//			JSONObject json = new JSONObject(strJson);
//			String error = json.getString("error");
//			String mes = json.getString("message");
            System.out.println("  " + strJson);

            QuizJSONParser jsonParser = new QuizJSONParser();
            jsonParser.jsonArrayName = "markforviewlist";
            rsuData = jsonParser.performJsonParsing(strJson);
//			  
//			  for (int i = 0; i < data.size(); i++) {
//				  Quizdata value = data.get(i);
//				  list.add(value.getQuestion());
//			}
//			  
            PerformanceAdapter adapter = new PerformanceAdapter(this, rsuData, device);
            listView.setAdapter(adapter);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}