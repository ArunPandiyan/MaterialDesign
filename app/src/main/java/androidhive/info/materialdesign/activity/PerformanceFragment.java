package androidhive.info.materialdesign.activity;
/**
 * Created by Technibits-13 on 15-May-2015.
 */

import android.content.Context;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidhive.info.materialdesign.R;

//Added from PerformanceActivity

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import androidhive.info.materialdesign.R;
import androidhive.info.materialdesign.adapter.PerformanceFragmentAdapter;
import androidhive.info.materialdesign.data.Quizdata;
import androidhive.info.materialdesign.data.ResultData;
import androidhive.info.materialdesign.parser.QuizJSONParser;

import android.content.Intent;
import android.util.DisplayMetrics;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;

public class PerformanceFragment extends Fragment {
    ListView listView;
    ArrayList<ResultData> rsuData;
    ArrayList<Quizdata> data;
    int device;
    View rootView;
    PerformanceFragment frag;
    Context context;

    public PerformanceFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        frag = this;
        context = getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.performence_activity, container, false);
        listView = (ListView) rootView.findViewById(R.id.listView1);


//		listView = (Button)findViewById(R.id.listView1);
//		listView = (Button)findViewById(R.id.listView1);


        DisplayMetrics metrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);

        int widthPixels = metrics.widthPixels;
        int heightPixels = metrics.heightPixels;
        float scaleFactor = metrics.density;
        float widthDp = widthPixels / scaleFactor;
        float heightDp = heightPixels / scaleFactor;

        float smallestWidth = Math.min(widthDp, heightDp);

        if (smallestWidth > 720) {
            device = 10;
        } else if (smallestWidth >= 600) {
            device = 7; //Device is a 7" tablet
        }


        String urls = "http://jmbok.avantgoutrestaurant.com/and/performance-history.php?userid=android@gmail.com";
        AsyncTaskCall ask = new AsyncTaskCall(getActivity(), this, urls, "performhis_frag");
        ask.execute(urls);
        return rootView;
    }

    public void performReview_frag(int id) {

        String urls = "http://jmbok.avantgoutrestaurant.com/and/performance_review1.php?testid=" + id;
        AsyncTaskCall ask = new AsyncTaskCall(getActivity(), urls, "perreview_frag");
        ask.execute(urls);
    }

    public void performReviewJSON_frag(JSONObject strJson) throws JSONException {
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
            Intent intent = new Intent(getActivity(), PreviewActivity.class);
            intent.putExtra("data", data);
            intent.putExtra("count", count);
            startActivity(intent);

        } catch (Exception e) {
            // TODO: handle exception
        }
    }

    public void performHis_frag(JSONObject strJson) throws JSONException {
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
            context = getActivity();
            PerformanceFragmentAdapter adapter = new PerformanceFragmentAdapter(context, rsuData, device, this);
            listView.setAdapter(adapter);

        } catch (Exception e) {
            Log.i("Errorin_", "");
            e.printStackTrace();
        }
    }
}
