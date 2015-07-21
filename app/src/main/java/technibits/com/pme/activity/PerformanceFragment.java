package technibits.com.pme.activity;
/**
 * Created by Technibits-13 on 15-May-2015.
 */

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import technibits.com.pme.R;

//Added from PerformanceActivity

import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.json.JSONException;
import org.json.JSONObject;

import technibits.com.pme.adapter.PerformanceFragmentAdapter;
import technibits.com.pme.data.NetworkUtil;
import technibits.com.pme.data.Quizdata;
import technibits.com.pme.data.ResultData;
import technibits.com.pme.parser.QuizJSONParser;

import android.content.Intent;
import android.util.DisplayMetrics;
import android.widget.ListView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
        DBConnection dbConnection = new DBConnection(getActivity());
//        dbConnection.getuserEmail();
        String urls = "http://jmbok.techtestbox.com/and/performance-history.php?userid=" + dbConnection.getuserEmail().trim();
//        boolean status = NetworkUtil.isOnline();
        boolean status = NetworkUtil.isOnline();
        if (status) {
            AsyncTaskCall ask = new AsyncTaskCall(getActivity(), this, urls, "performhis_frag");
            ask.execute(urls);
        }else{
            NetworkUtil.showNetworkstatus(getActivity());
        }

        return rootView;
    }

    public void performReview_frag(int id) {

        String urls = "http://jmbok.techtestbox.com/and/performance_review1.php?testid=" + id;
        AsyncTaskCall_frag ask = new AsyncTaskCall_frag(getActivity(), urls, "perreview_frag", this);
        ask.execute(urls);
    }

    public void performReviewJSON_frag(JSONObject strJson) throws JSONException {
        try {
//			JSONObject json = new JSONObject(strJson);
//			String error = json.getString("error");
//			String mes = json.getString("message");


            QuizJSONParser jsonParser = new QuizJSONParser();
            jsonParser.jsonArrayName = "markforviewlist";
            if (strJson != null) {
                data = jsonParser.performReviewJsonParsing(strJson);
                int count = data.size();
                Intent intent = new Intent(getActivity(), PreviewActivity.class);
                intent.putExtra("data", data);
                intent.putExtra("count", count);
                intent.putExtra("src_activity", "Performance History");
                startActivity(intent);
            } else {
                quit();
            }
//
//			  for (int i = 0; i < data.size(); i++) {
//				  Quizdata value = data.get(i);
//				  list.add(value.getQuestion());
//			}


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /*
    Confirming the empty JSON Object
     */
    public void quit() {
        new AlertDialog.Builder(getActivity())
                .setTitle("Warning")
                .setMessage("Ooops!! Information you're looking for is not Available.")

                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // continue with delete
//                        finish();
                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // do nothing
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    /*
    @params JSONObject strJson
     */
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

    public class AsyncTaskCall_frag extends AsyncTask<String, String, JSONObject> {
        JSONObject jsonObj = null;

        //edit
        PerformanceFragment performanceFragment;
        ProgressDialog prog;
        String url;
        String requestType;
        Context context;
        List<NameValuePair> parmsValue;
        Activity activity;
        String jsons;

        public AsyncTaskCall_frag(Activity context, String urls,
                                  String request, PerformanceFragment pf) {

            requestType = request;
            activity = context;
            url = urls;
            performanceFragment = pf;
        }

        @Override
        protected void onPreExecute() {
            if (requestType.equals("perreview_frag")) {
                prog = new ProgressDialog(getActivity());
            }

            prog.setCancelable(false);
            prog.setCanceledOnTouchOutside(false);
            prog.setMessage("Loading....");
            prog.show();
        }

        @Override
        protected JSONObject doInBackground(String... params) {

            if (requestType.equals("review") || requestType.equals("result")
                    || requestType.equals("signup") || requestType.equals("signin")) {
                MasterDownload httpPost = new MasterDownload();
                try {
                    jsons = httpPost.post(params[0], parmsValue);
                } catch (ClientProtocolException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {

                MasterDownload downloader = new MasterDownload();
                try {

                    if (requestType.equals("perreview") || requestType.equals("perreview_frag")) {
                        String json = downloader.queryRESTurl(url);
                        jsonObj = new JSONObject(json);
                    } else {
                        jsonObj = new JSONObject(downloader.queryRESTurl(url));
                        System.out.println("JSON for Selection" + jsonObj);
                    }
                } catch (JSONException e) {

                    e.printStackTrace();
                }
            }

            return jsonObj;
        }

        @Override
        protected void onPostExecute(JSONObject json) {

            if (requestType.equals("perreview_frag")) {
                if (activity != null) {
                    try {
                        ((PerformanceFragment) performanceFragment).performReviewJSON_frag(jsonObj);
                    } catch (JSONException e) {

                        e.printStackTrace();
                    }
                }
            }


            prog.dismiss();
        }
    }
}
