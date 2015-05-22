package androidhive.info.materialdesign.activity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import androidhive.info.materialdesign.activity.MainActivity_as;

public class AsyncTaskCall extends AsyncTask<String, String, JSONObject> {

	MainActivity_as mainActivity;
	StudyModeFragment studyFragment;
	JSONObject jsonObj = null;
	JSONArray jsonArray = null;
	ExamFragment examFragment;
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

	ProgressDialog prog;
	String jsonStr = null;
	Handler innerHandler;

	@Override
	protected void onPreExecute() {
		if (requestType.equals("selection")) {
			prog = new ProgressDialog(mainActivity);
		} else if (requestType.equals("study") || requestType.equals("exam")) {
			prog = new ProgressDialog(activity);
		} else if (requestType.equals("review") || requestType.equals("result")) {
			prog = new ProgressDialog(context);
		} else if (requestType.equals("signup") || requestType.equals("signin")
				|| requestType.equals("reviewhis")
				|| requestType.equals("performhis") || requestType.equals("perreview")) {
			prog = new ProgressDialog(activity);
		}

		prog.setCancelable(false);
		prog.setCanceledOnTouchOutside(false);
		prog.setMessage("Loading....");
		prog.show();
	}

	@Override
	protected JSONObject doInBackground(String... params) {

		// TODO Auto-generated method stub
		if (requestType.equals("review") || requestType.equals("result")
				|| requestType.equals("signup") || requestType.equals("signin")) {
			MasterDownload httpPost = new MasterDownload();
			try {
				jsons = httpPost.post(params[0], parmsValue);
			} catch (ClientProtocolException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {

			MasterDownload downloader = new MasterDownload();
			try {
				
				if (requestType.equals("perreview")) {
					String json = downloader.queryRESTurl(url);
					jsonObj = new JSONObject(json);
				}else{
				jsonObj = new JSONObject(downloader.queryRESTurl(url));
				System.out.println("JSON for Selection" + jsonObj);
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
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
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		} else if (requestType.equals("study")) {
			try {
				studyFragment.Json(json);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else if (requestType.equals("exam")) {
			try {
				examFragment.Json(json);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else if (requestType.equals("review")) {
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
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		} else if (requestType.equals("signin")) {
			if (activity != null) {
				try {
					((LoginActivity) activity).login(jsons);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		} else if (requestType.equals("reviewhis")) {
			if (activity != null) {
				try {
					((ReviewHisActivity) activity).review(jsonObj);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		} else if (requestType.equals("performhis")) {
			if (activity != null) {
				try {
					((PerformanceActivity)activity).performHis(jsonObj);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}else if (requestType.equals("perreview")) {
			if (activity != null) {
				try {
					((PerformanceActivity)activity).performReviewJSON(jsonObj);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		
		
		

		prog.dismiss();
	}

}
