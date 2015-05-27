package androidhive.info.materialdesign.activity;


import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import androidhive.info.materialdesign.R;
import androidhive.info.materialdesign.adapter.ResultAdapter;
import androidhive.info.materialdesign.adapter.ShowReviewAdapter;
import androidhive.info.materialdesign.adapter.StudyAdapter;
import androidhive.info.materialdesign.data.Quizdata;
import androidhive.info.materialdesign.data.ResultData;
import androidhive.info.materialdesign.parser.QuizJSONParser;

import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.LightingColorFilter;
import android.graphics.Point;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.RadioGroup.OnCheckedChangeListener;

public class StudyModeFragment extends Fragment {
    int check = 0;
    Context context;
    ArrayList<Quizdata> data;
    ArrayList<Quizdata> retakedata;
    ListView list;
    int count;
    int iNext = 1;
    RadioGroup radioGroup;
    int device;
    Quizdata dataSource;
    View seakBarlayout;
    TextView textEnd, textStart, textGoalStart, textGoalend;
    ListView reviewListview;
    ListView resultListview;
    Button btnreTake, btnreTest, review;
    View reviewView;
    View resultView;
    SeekBar seekBar, seekGoal;
    boolean pCheck;
    boolean nCheck;
    String url;
    StudyModeFragment frag;
    AlertDialog alertDialog;
    RelativeLayout linearLayout;
    Button priv;
    Button next, exp;
    String urlFinish = "http://jmbok.avantgoutrestaurant.com/and/study_result.php";
    public int answer;
    ResultData resData;
    private Toolbar mToolbar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        frag = this;

    }

    public StudyModeFragment(String url) {
        this.url = url;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        context = activity;

    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        resData = new ResultData();

        DisplayMetrics metrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);

        int widthPixels = metrics.widthPixels;
        int heightPixels = metrics.heightPixels;
        float scaleFactor = metrics.density;
        float widthDp = widthPixels / scaleFactor;
        float heightDp = heightPixels / scaleFactor;

        float smallestWidth = Math.min(widthDp, heightDp);
        float smallestHeight = Math.min(widthDp, heightDp);
        View rootView = null;
        if (smallestWidth > 720) {
            device = 10;
        } else if (smallestWidth >= 600) {
            device = 7; //Device is a 7" tablet
            rootView = inflater.inflate(R.layout.study_mode_seven, container,
                    false);
        } else {
            rootView = inflater.inflate(R.layout.study_mode, container,
                    false);
        }
        mToolbar = (Toolbar) rootView.findViewById(R.id.toolbar);
        mToolbar.setTitle("Study Mode");
//		 .getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        LayoutInflater mInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        reviewView = mInflater.inflate(R.layout.show_review, container, false);

        reviewListview = (ListView) reviewView.findViewById(R.id.reviewListview);

        resultView = mInflater.inflate(R.layout.finish_layout, container, false);

        resultListview = (ListView) resultView.findViewById(R.id.resultListview);

        btnreTake = (Button) resultView.findViewById(R.id.retake);
        btnreTest = (Button) resultView.findViewById(R.id.reTest);
        review = (Button) resultView.findViewById(R.id.review);
        review.setVisibility(View.GONE);

        btnreTake.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                reTake();
            }
        });

        btnreTest.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                reTest();
            }
        });

        LayoutInflater seakLayout = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
//		 View layout = inflater.inflate(R.layout.seakbar_dialog, (ViewGroup)findViewById(R.id.your_dialog_root_element));
        seakBarlayout = seakLayout.inflate(R.layout.seakbar_dialog_layout, container, false);
        textEnd = (TextView) seakBarlayout.findViewById(R.id.countend);
        textStart = (TextView) seakBarlayout.findViewById(R.id.countstart);

        textGoalStart = (TextView) seakBarlayout.findViewById(R.id.goalstart);
        textGoalend = (TextView) seakBarlayout.findViewById(R.id.goalend);

        seekBar = (SeekBar) seakBarlayout.findViewById(R.id.que_seekbar);
        seekGoal = (SeekBar) seakBarlayout.findViewById(R.id.goal_seekbar);
        seekGoal.setMax(100);


        seekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

            public void onStopTrackingTouch(SeekBar arg0) {
                // TODO Auto-generated method stub
                System.out.println(".....111.......");

            }

            public void onStartTrackingTouch(SeekBar arg0) {
                // TODO Auto-generated method stub
                System.out.println(".....222.......");
            }

            public void onProgressChanged(SeekBar arg0, int arg1, boolean arg2) {
                // TODO Auto-generated method stub
//	            	String progressString = String.valueOf(arg1 * 10);
                System.out.println(".....333......." + arg1);
                textStart.setText(String.valueOf(arg1));
                count = arg1;
            }
        });

        seekGoal.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

            public void onStopTrackingTouch(SeekBar arg0) {
                // TODO Auto-generated method stub

            }

            public void onStartTrackingTouch(SeekBar arg0) {
                // TODO Auto-generated method stub
            }

            public void onProgressChanged(SeekBar arg0, int arg1, boolean arg2) {
                // TODO Auto-generated method stub
//	            	String progressString = String.valueOf(arg1 * 10);
                textGoalStart.setText(String.valueOf(arg1) + "%");

            }
        });


        context = container.getContext();
        String urls = url;//"http://www.jmbok.avantgoutrestaurant.com/and/all.php?knowledgearea=Projectriskmanagement&group=SelectAll&processname=SelectAll&difficulty=SelectAll";
        AsyncTaskCall ask = new AsyncTaskCall(getActivity(), this, urls, "study");
        ask.execute(urls);

        priv = (Button) rootView.findViewById(R.id.prev);
        next = (Button) rootView.findViewById(R.id.next);
        exp = (Button) rootView.findViewById(R.id.exp);
        list = (ListView) rootView.findViewById(R.id.listView1);

        list.setVisibility(View.GONE);
        priv.setVisibility(View.GONE);
        next.setVisibility(View.GONE);
        exp.setVisibility(View.GONE);
        priv.setEnabled(false);
        exp.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
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
                        StudyAdapter adapter = new StudyAdapter(context, dataSource, iNext, device, frag, resData);

                        list.setAdapter(adapter);
                        priv.setEnabled(true);
                        iNext++;
                        pCheck = true;
                        nCheck = false;

                    }
                    if (iNext == count) {
//						next.setEnabled(false);
                        next.setText("Finish");
//	            		next.setBackgroundColor(Color.RED);
                        iNext = count - 1;
                        pCheck = false;


                    }

                } else {
                    float proportionCorrect = ((float) resData.getCorrectAnswers()) / ((float) count);
//	        	  double perInt =  resData.getCorrectAnswers() / count;
                    int percentage = (int) (proportionCorrect * 100);
                    resData.setPercentage(percentage);
                    if (percentage >= 50) {
                        resData.setResult("Pass");
                    } else {
                        resData.setResult("Fail");
                    }
                    resData.setTotalQuestion(count);


                    getresult();
                    System.out.println("count  " + count + "perInt   " + proportionCorrect + "  percentage  " + percentage + "  answer  " + resData.getCorrectAnswers());
                }


            }


        });

        priv.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click
                if (iNext == count) {
                    iNext = count - 1;
                }
                if (pCheck) {
                    iNext--;
                }
                if (iNext > 0) {
                    iNext--;
                    dataSource = data.get(iNext);
                    StudyAdapter adapter = new StudyAdapter(context, dataSource, iNext, device, frag, resData);
//	            		adapter.resData = resData;
                    list.setAdapter(adapter);
                    next.setEnabled(true);
                    if (next.getText().toString().equals("Finish")) {
                        next.setText("Next");
//			    		    next.setBackgroundDrawable(priv.getBackground());
                    }
                    nCheck = true;
                    pCheck = false;

                }
                if (iNext == 0) {
                    priv.setEnabled(false);
                    iNext = 0;
                    nCheck = false;

                }


            }
        });


        return rootView;

    }

//	 public static StudyModeFragment newInstance(int sectionNumber) {
//		 StudyModeFragment fragment = new StudyModeFragment();
//			Bundle args = new Bundle();
//			args.putInt("", sectionNumber);
//			fragment.setArguments(args);
//			return fragment;
//		}

    public void Json(JSONObject json) throws JSONException {
        try {
            QuizJSONParser jsonParser = new QuizJSONParser();
            jsonParser.jsonArrayName = "knowledgearea";
            data = jsonParser.testJsonParsing(json);
            retakedata = jsonParser.testJsonParsing(json);
            if (data.size() > 0) {

                count = data.size();
                for (int i = 0; i < data.size(); i++) {
                    Quizdata list = data.get(i);
                    System.out.println("" + list.getQuestion());
                }
                if (data.size() > 0) {
                    dataSource = data.get(0);
                    StudyAdapter adapter = new StudyAdapter(context, dataSource, 0, device, frag, resData);
//						 adapter.resData = resData;
                    list.setAdapter(adapter);
                }
                show();
            } else {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
                alertDialogBuilder.setTitle("No records found for your search !! ");

                alertDialogBuilder
                        .setCancelable(false)
                        .setPositiveButton("Ok",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,
                                                        int id) {
                                        getActivity().finish();
                                    }
                                });

                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
            }
        } catch (Exception e) {
            // TODO: handle exception
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
            alertDialogBuilder.setTitle("No records found for your search !! ");

            alertDialogBuilder
                    .setCancelable(false)
                    .setPositiveButton("Ok",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,
                                                    int id) {
                                    getActivity().finish();
                                }
                            });

            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();
        }

    }

    public void show() {

        RelativeLayout linearLayout = new RelativeLayout(context);

        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(550, 550);
        RelativeLayout.LayoutParams numPicerParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.FILL_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        numPicerParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
        seakBarlayout.setPadding(0, 15, 0, 0);
        linearLayout.setLayoutParams(params);
        linearLayout.addView(seakBarlayout, numPicerParams);
        textEnd.setText(String.valueOf(data.size()));

        seekBar.setMax(data.size());

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        alertDialogBuilder.setTitle("Select the number of Question out of " + String.valueOf(data.size()) + "\n ");
        alertDialogBuilder.setView(linearLayout);
        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton("Start Study",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int id) {
                                list.setVisibility(View.VISIBLE);
                                priv.setVisibility(View.VISIBLE);
                                next.setVisibility(View.VISIBLE);
                                exp.setVisibility(View.VISIBLE);
                            }
                        })
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int id) {
                                dialog.cancel();
                                getActivity().finish();
                            }
                        });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();


    }

    public void showReview() {

//		 for (int i = 0; i < count; i++) {
//			 Quizdata quzData = data.get(i);
//			 System.out.println("quzData  " + quzData.getStatus());
//		}

        Display display = getActivity().getWindowManager().getDefaultDisplay();
        int width = display.getWidth();
        int height = display.getHeight();
//		 reviewView.setVisibility(View.VISIBLE);

        ShowReviewAdapter adapter = new ShowReviewAdapter(context, data, count, device, frag);
        reviewListview.setAdapter(adapter);

        linearLayout = new RelativeLayout(context);

        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(width, height);
        RelativeLayout.LayoutParams numPicerParams = new RelativeLayout.LayoutParams(width, height);
        numPicerParams.addRule(RelativeLayout.CENTER_HORIZONTAL);

        linearLayout.setLayoutParams(params);
        linearLayout.setPadding(10, 5, 10, 5);
        linearLayout.addView(reviewView, numPicerParams);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
//	        alertDialogBuilder.setTitle("Select the number of Question out of " + String.valueOf(data.size()));
        alertDialogBuilder.setView(linearLayout);
        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton("Ok",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int id) {
                                alertDialog.dismiss();
                                linearLayout.removeView(reviewView);

//	                            	reviewView.setVisibility(View.VISIBLE);
                            }

                        });
        alertDialog = alertDialogBuilder.create();
        alertDialog.show();

    }

    public void reviewNavication(int id) {

        if (data.size() > 0) {
            iNext = id;
            dataSource = data.get(id);
            StudyAdapter adapter = new StudyAdapter(context, dataSource, id, device, frag, resData);
            list.setAdapter(adapter);
        }
        alertDialog.dismiss();
        linearLayout.removeView(reviewView);
        next.setText("Next");
    }

    public void showResult() {

//		 for (int i = 0; i < count; i++) {
//			 Quizdata quzData = data.get(i);
//			 System.out.println("quzData  " + quzData.getStatus());
//		}

        Display display = getActivity().getWindowManager().getDefaultDisplay();
        int width = display.getWidth();
        int height = display.getHeight();
//		 reviewView.setVisibility(View.VISIBLE);

        ResultAdapter adapter = new ResultAdapter(context, resData, count, device, frag);
        resultListview.setAdapter(adapter);

        linearLayout = new RelativeLayout(context);

        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(width, height);
        RelativeLayout.LayoutParams numPicerParams = new RelativeLayout.LayoutParams(width, height);
        numPicerParams.addRule(RelativeLayout.CENTER_HORIZONTAL);

        linearLayout.setLayoutParams(params);

        linearLayout.addView(resultView, numPicerParams);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
//	        alertDialogBuilder.setTitle("Select the number of Question out of " + String.valueOf(data.size()));
        alertDialogBuilder.setView(linearLayout);

        alertDialog = alertDialogBuilder.create();
        alertDialog.show();

        check = 0;
        alertDialog.setOnKeyListener(new DialogInterface.OnKeyListener() {

            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {

                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    if (check == 0) {
//	                		   ((QuizActivity)getActivity()).quit();
                        ((QuizActivity) getActivity()).onBackPressed();
                    }
                    check = 1;
                }
                return false;


            }
        });

    }

    public void getresult() {
        String testID = UUID.randomUUID().toString();
        System.out.println("resData.getTotalQuestion()  " + resData.getTotalQuestion() + "resData.getAttemptQuestions()   " + resData.getAttemptQuestions() + "  resData.getCorrectAnswers()  " + resData.getCorrectAnswers() + "  resData.getMarkedReview()  " + resData.getMarkedReview()
        );

        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("Totalquestion", String.valueOf(resData.getTotalQuestion())));
        params.add(new BasicNameValuePair("Attemptquestions", String.valueOf(resData.getAttemptQuestions())));
        params.add(new BasicNameValuePair("Correctanswers", String.valueOf(resData.getCorrectAnswers())));
        params.add(new BasicNameValuePair("Markedreview", String.valueOf(resData.getMarkedReview())));
        params.add(new BasicNameValuePair("Percentage", String.valueOf(resData.getPercentage())));
        params.add(new BasicNameValuePair("Result", resData.getResult()));
        params.add(new BasicNameValuePair("userid", "android@gmail.com"));
        params.add(new BasicNameValuePair("testid", testID));

        AsyncTaskCall ask = new AsyncTaskCall(context, "result", params);
        ask.studyFragment = this;
        ask.execute(urlFinish);
    }

    public void refersh() {
        list.invalidateViews();

    }

    public void reTake() {

        data.clear();
        data = new ArrayList<Quizdata>(retakedata);
//		data = retakedata;
        if (data.size() > 0) {
            dataSource = data.get(0);
            StudyAdapter adapter = new StudyAdapter(context, dataSource, 0, device, frag, resData);
//			 adapter.resData = resData;
            list.setAdapter(adapter);
            alertDialog.dismiss();
            linearLayout.removeView(resultView);
            next.setText("Next");
        }
    }

    public void reTest() {
        linearLayout.removeView(resultView);
        getActivity().finish();
        alertDialog.dismiss();
    }
}
