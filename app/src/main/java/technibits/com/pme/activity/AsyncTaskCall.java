package technibits.com.pme.activity;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;

import technibits.com.pme.alarmactivity.RemindMe;

public class AsyncTaskCall extends AsyncTask<String, String, JSONObject> {

    MainActivity_as mainActivity;
    StudyModeFragment studyFragment;
    ChooseCategoryActivity chooseCategoryActivity;
    RemindMe remindMe;
    JSONObject jsonObj = null;
    JSONArray jsonArray = null;
    ExamFragment examFragment;
    //edit
    PerformanceFragment performanceFragment;
    ReviewFragment reviewFragment;
    BuyProFragment buyProFragment;

    String url;
    String requestType;
    Context context;
    List<NameValuePair> parmsValue;
    Activity activity;
    String jsons;

    public AsyncTaskCall(MainActivity_as activity, String urls, String request) {
        mainActivity = activity;
        url = urls;
        requestType = request;
    }

    public AsyncTaskCall(Activity context, StudyModeFragment studyFrag,
                         String urls, String request) {
        studyFragment = studyFrag;
        url = urls;
        requestType = request;
        activity = context;
    }

    public AsyncTaskCall(ChooseCategoryActivity activity, String urls, String request) {
        chooseCategoryActivity = activity;

        url = urls;
        requestType = request;
    }

    public AsyncTaskCall(Activity context, ExamFragment examFrag, String urls,
                         String request) {
        examFragment = examFrag;
        url = urls;
        requestType = request;
        activity = context;
    }

    public AsyncTaskCall(Context context, String request,
                         List<NameValuePair> params) {

        requestType = request;
        this.context = context;
        this.parmsValue = params;
    }

    public AsyncTaskCall(ChangepasswordActivity context, String request,
                         List<NameValuePair> params) {

        requestType = request;
        this.context = context;
        this.parmsValue = params;
        activity = context;
    }

    public AsyncTaskCall(CreateAccountActivity context, String request,
                         List<NameValuePair> params) {

        requestType = request;
        activity = context;
        this.parmsValue = params;
    }

    public AsyncTaskCall(LoginActivity context, String request,
                         List<NameValuePair> params) {

        requestType = request;
        activity = context;
        this.parmsValue = params;
    }

    public AsyncTaskCall(ReviewHisActivity context, String urls, String request) {

        requestType = request;
        activity = context;
        url = urls;
    }

    public AsyncTaskCall(PerformanceActivity context, String urls,
                         String request) {

        requestType = request;
        activity = context;
        url = urls;
    }

    //performance fragment ==> review test
    public AsyncTaskCall(Activity context, ReviewFragment rf,
                         String urls, String request) {

        requestType = request;
        reviewFragment = rf;
        activity = context;
        url = urls;
    }

    //for performance fragment
    public AsyncTaskCall(Activity context, PerformanceFragment pf,
                         String urls, String request) {

        requestType = request;
        performanceFragment = pf;
        activity = context;
        url = urls;
    }

    //for review fragment
    public AsyncTaskCall(Activity context, String urls,
                         String request) {

        requestType = request;
        activity = context;
        url = urls;
    }

    public AsyncTaskCall(Activity context, BuyProFragment buyFrag,
                         String urls, String request,List<NameValuePair> params) {
        buyProFragment = buyFrag;
        url = urls;
        requestType = request;
        activity = context;
        this.parmsValue = params;
    }


    ProgressDialog prog;
    String jsonStr = null;
    Handler innerHandler;

    @Override
    protected void onPreExecute() {
        if (requestType.equals("selection")) {
            prog = new ProgressDialog(mainActivity);
        } else if (requestType.equals("study") || requestType.equals("exam")) {
            prog = new ProgressDialog(activity);
        } else if (requestType.equals("review") || requestType.equals("result")|| requestType.equals("changepass")) {
            prog = new ProgressDialog(context);
        } else if (requestType.equals("signup") || requestType.equals("signin") || requestType.equals("Gplussignup")
                || requestType.equals("reviewhis_frag") || requestType.equals("reviewhis")
                || requestType.equals("performhis") || requestType.equals("perreview")||requestType.equals("buypro")) {
            prog = new ProgressDialog(activity);
        } else if (requestType.equals("performhis_frag")) {
            prog = new ProgressDialog(activity);
        }else if(requestType.equals("selection_choose")){
            prog=new ProgressDialog(chooseCategoryActivity);
            }

        prog.setCancelable(false);
        prog.setCanceledOnTouchOutside(false);
        prog.setMessage("Loading....");
        prog.show();
    }

    @Override
    protected JSONObject doInBackground(String... params) {

        if (requestType.equals("review") || requestType.equals("result")
                || requestType.equals("signup") || requestType.equals("signin") || requestType.equals("Gplussignup") ||requestType.equals("changepass")||requestType.equals("buypro")) {
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

        if (requestType.equals("selection")) {

            try {
                mainActivity.jsonParsing(json);
            } catch (JSONException e) {

                e.printStackTrace();
            }

        } else if (requestType.equals("study")) {
            try {
                studyFragment.Json(json);
            } catch (JSONException e) {

                e.printStackTrace();
            }
        } else if (requestType.equals("exam")) {
            try {
                examFragment.Json(json);
            } catch (JSONException e) {

                e.printStackTrace();
            }
        } else if (requestType.equals("review")) {
            //TODO: add delete function--forgotten
        } else if (requestType.equals("result")) {
            if (studyFragment != null) {
                studyFragment.showResult();
            } else {
                examFragment.showResult();
            }

        } else if (requestType.equals("signup")) {
            if (activity != null) {
                try {
                    ((CreateAccountActivity) activity).finish(jsons);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }else if (requestType.equals("changepass")) {
            if (activity != null) {
                try {
                    ((ChangepasswordActivity) activity).changedPassword(jsons);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        } else if (requestType.equals("signin")) {
            if (activity != null) {
                try {
                    ((LoginActivity) activity).login(jsons);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        } else if (requestType.equals("reviewhis")) {
            if (activity != null) {
                try {
                    ((ReviewHisActivity) activity).review(jsonObj);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        } else if (requestType.equals("reviewhis_frag")) {
            if (activity != null) {
                try {
                    ((ReviewFragment) reviewFragment).review(jsonObj);
                } catch (JSONException e) {

                    e.printStackTrace();
                }
            }
        } else if (requestType.equals("performhis")) {
            if (activity != null) {
                try {
                    ((PerformanceActivity) activity).performHis(jsonObj);
                } catch (JSONException e) {

                    e.printStackTrace();
                }
            }

        } else if (requestType.equals("performhis_frag")) {
            if (activity != null) {
                try {
                    ((PerformanceFragment) performanceFragment).performHis_frag(jsonObj);
                } catch (JSONException e) {

                    e.printStackTrace();
                }
            }
        } else if (requestType.equals("perreview")) {
            if (activity != null) {
                try {
                    ((PerformanceActivity) activity).performReviewJSON(jsonObj);
                } catch (JSONException e) {

                    e.printStackTrace();
                }
            }
        } else if (requestType.equals("Gplussignup")) {
            if (activity != null) {
                try {
                    ((LoginActivity) activity).finishGplus(jsons);
                } catch (JSONException e) {

                    e.printStackTrace();
                }
            }
        }else if (requestType.equals("buypro")) {
            if (activity != null) {
                try {
                    ((BuyProFragment) buyProFragment).afterBuy(jsons);
                } catch (JSONException e) {

                    e.printStackTrace();
                }
            }
        }else if (requestType.equals("selection_choose")) {

            try {
                ((ChooseCategoryActivity)chooseCategoryActivity).jsonParsing(json);
            } catch (JSONException e) {

                e.printStackTrace();
            }

        }

            prog.dismiss();
    }

}
