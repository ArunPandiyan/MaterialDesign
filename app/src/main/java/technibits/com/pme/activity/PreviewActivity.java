package technibits.com.pme.activity;

import java.util.ArrayList;

import technibits.com.pme.R;
import technibits.com.pme.adapter.ExammodeAdapter;
import technibits.com.pme.adapter.PreviewAdapter;
import technibits.com.pme.adapter.ShowReviewAdapter;
import technibits.com.pme.adapter.TestAdapter;
import technibits.com.pme.data.Quizdata;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class PreviewActivity extends AppCompatActivity {
    int device;
    Button priv;
    Button next;
    boolean pCheck;
    boolean nCheck;
    Quizdata dataSource;
    int count, position;
    String select;
    int iNext = 0;
    ListView list;
    ArrayList<Quizdata> data;
    Context context;
    PreviewAdapter adapter;
    Toolbar mToolbar;
    String title = null;
    AlertDialog alertDialog;
    PreviewActivity previewActivity;
    AlertDialog.Builder alertDialogBuilder;

    @SuppressWarnings("unchecked")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        previewActivity = this;

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
        } else if (smallestWidth >= 600) {
            device = 7; //Device is a 7" tablet
            setContentView(R.layout.study_mode_seven);
        } else {
            setContentView(R.layout.study_mode);
        }
        mToolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


//		 Bundle bundle = getIntent().getExtras();
        list = (ListView) findViewById(R.id.listView1);
        data = (ArrayList<Quizdata>) getIntent().getSerializableExtra("data");

        count = getIntent().getExtras().getInt("count");
        select = getIntent().getExtras().getString("review");
        position = getIntent().getExtras().getInt("question_no");
        title = getIntent().getExtras().getString("src_activity");
        getSupportActionBar().setTitle(title);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
//        getSupportActionBar().setDisplayShowHomeEnabled(true);
//		 count = data.size();

        if (data.size() > 0) {
            dataSource = data.get(position);
            adapter = new PreviewAdapter(previewActivity, context, dataSource, iNext, device, select);
//			 adapter.resData = resData;
            list.setAdapter(adapter);
        }

        priv = (Button) findViewById(R.id.prev);
        next = (Button) findViewById(R.id.next);
        Button exp = (Button) findViewById(R.id.exp);
        list = (ListView) findViewById(R.id.listView1);
        priv.setEnabled(false);

        if (select != null) {
            priv.setVisibility(View.GONE);
//            exp.setVisibility(View.GONE);
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
                        PreviewAdapter adapter = new PreviewAdapter(previewActivity, context, dataSource, iNext, device, select);
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
                    finish();
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
                    PreviewAdapter adapter = new PreviewAdapter(previewActivity, context, dataSource, iNext, device, select);
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


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        sendMessage();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                super.onBackPressed();
                sendMessage();
//                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void sendMessage() {
        Log.d("sender", "Broadcasting message");
        Intent intent = new Intent("custom-event-name");
        // You can also include some extra data.
        intent.putExtra("message", adapter.ansOption);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }


    public void showInfo() {
//        TextView proName, proGroup, knAria;
//
//        proName = (TextView) findViewById(R.id.proname);
//        proGroup = (TextView) findViewById(R.id.progroup);
//        knAria = (TextView) findViewById(R.id.knArea);

//        final Dialog dialog = new Dialog(context); // context, this etc.
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
//        alertDialogBuilder.se
//        alertDialogBuilder.setLsetView(R.layout.show_info);
        alertDialogBuilder.setTitle("Info");
//        alertDialogBuilder.setMessage("Process: \n"+dataSource.getProcessname()+"\n\nProcess Group:\n"+dataSource.getProcessgroup()+"\n\nKnowledge Area : \n"+dataSource.getKnowledgeArea());
        alertDialogBuilder.setMessage("Knowledge Area :\n" + dataSource.getKnowledgeArea() + "\n\nProcess Group:\n" + dataSource.getProcessgroup() + "\n\nProcess: \n" + dataSource.getProcessname());
//        alertDialogBuilder.setMessage("2ndProcess Group : \n"+dataSource.getProcessgroup());

//        proName.setText(dataSource.getProcessname());
//        proName.setTextColor(Color.parseColor("#000000"));
//        proGroup.setText(dataSource.getProcessgroup());
//        proGroup.setTextColor(Color.parseColor("#000000"));
//        knAria.setText(dataSource.getKnowledgeArea());
//        knAria.setTextColor(Color.parseColor("#000000"));

        alertDialogBuilder.setCancelable(false);
//        dialog.

//        alertDialogBuilder.show();


        alertDialogBuilder.setCancelable(false)
                .setPositiveButton("Ok",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.dismiss();
//                                linearLayout.removeView(showInfoView);
                            }

                        });
        alertDialog = alertDialogBuilder.create();
        alertDialogBuilder.show();
        alertDialog.getWindow().setLayout(600, 400);
    }

    /*
           Shows list of questions with status of 1. Answered,
                                                  2. Marked for review,
                                                  3. Not Answered.
        */

    public void showReview() {
        ShowReviewAdapter adapter = new ShowReviewAdapter(previewActivity, data, count, device, this);

//        TextView proName, proGroup, knAria;
        LayoutInflater inflater = this.getLayoutInflater();
        final Dialog dialog = new Dialog(this); // context, this etc.
         alertDialogBuilder = new AlertDialog.Builder(this);
//        alertDialogBuilder.setView(inflater.inflate(R.layout.show_info,null));
        View dialogView = inflater.inflate(R.layout.show_review, null);
        ListView reviewListview = (ListView) dialogView.findViewById(R.id.reviewListview);
        reviewListview.setAdapter(adapter);
        alertDialogBuilder.setView(dialogView);
        alertDialogBuilder.setTitle("Overview");

//        proName = (TextView) dialogView.findViewById(R.id.proname);
//        proGroup = (TextView)dialogView.findViewById(R.id.progroup);
//        knAria = (TextView)  dialogView.findViewById(R.id.knArea);

//

//        proName.setText(dataSource.getProcessname());
//        proName.setTextColor(Color.parseColor("#000000"));
//        proGroup.setText(dataSource.getProcessgroup());
//        proGroup.setTextColor(Color.parseColor("#000000"));
//        knAria.setText(dataSource.getKnowledgeArea());
//        knAria.setTextColor(Color.parseColor("#000000"));

        alertDialogBuilder.setCancelable(false);
//        dialog.

//        alertDialogBuilder.show();







        alertDialogBuilder.setCancelable(false)
                .setPositiveButton("Ok",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.dismiss();
//                                linearLayout.removeView(showInfoView);
                            }

                        });
        alertDialog = alertDialogBuilder.create();
        alertDialog.show();
//alertDialog.show();
//        ShowReviewAdapter adapter = new ShowReviewAdapter(context, data, count, device, this);
//        list.setAdapter(adapter);
//
//        linearLayout = new RelativeLayout(context);
//
//        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(width, height);
//        RelativeLayout.LayoutParams numPicerParams = new RelativeLayout.LayoutParams(width, height);
//        numPicerParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
//
//        linearLayout.setLayoutParams(params);
//
//        linearLayout.addView(reviewView, numPicerParams);
//
//        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
//        alertDialogBuilder.setView(linearLayout);
//        alertDialogBuilder
//                .setCancelable(false)
//                .setPositiveButton("Ok",
//                        new DialogInterface.OnClickListener() {
//                            public void onClick(DialogInterface dialog,
//                                                int id) {
//                                alertDialog.dismiss();
//                                linearLayout.removeView(reviewView);
//                            }
//
//                        });
//        alertDialog = alertDialogBuilder.create();
//        alertDialog.show();

    }
    /*
       Used by showReview() to navigate to selected question from showreviewAdapter
        */
    public void reviewNavication(int id) {

        if (data.size() > 0) {
            iNext = id;
            if (iNext == 0) {
                priv.setEnabled(false);
                iNext = 0;
                nCheck = false;

            } else{
                priv.setEnabled(true);
            }

            dataSource = data.get(id);
            PreviewAdapter adapter = new PreviewAdapter(previewActivity, context, dataSource, id, device, select);//(context, dataSource, id, device, frag, resData);
            list.setAdapter(adapter);
        }
        alertDialog.dismiss();
//        linearLayout.removeView(reviewView);
        next.setText("Next");
    }

}
