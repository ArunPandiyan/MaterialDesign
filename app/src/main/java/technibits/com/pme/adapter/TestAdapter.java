package technibits.com.pme.adapter;

/**
 * Created by technibitsuser on 6/10/2015.
 */

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import technibits.com.pme.R;
import technibits.com.pme.activity.AsyncTaskCall;
import technibits.com.pme.activity.MasterDownload;
import technibits.com.pme.activity.PreviewActivity;
import technibits.com.pme.activity.ReviewFragment;
import technibits.com.pme.data.Quizdata;
import technibits.com.pme.data.ResultData;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.content.Context;
import android.graphics.Color;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.Toast;

import technibits.com.pme.R;
import technibits.com.pme.activity.AsyncTaskCall;
import technibits.com.pme.activity.MasterDownload;
import technibits.com.pme.activity.PreviewActivity;
import technibits.com.pme.activity.ReviewFragment;
import technibits.com.pme.data.Quizdata;
import technibits.com.pme.data.ResultData;


public class TestAdapter extends BaseAdapter {
    private Context context;
    ReviewFragment activity;

    Quizdata data;
    String formID;
    EditText dateText;
    public ViewHolderA viewHolder;
    int queNo, count = 0;
    int size;
    String select;
    public ResultData resData;
    String urlMark = "http://jmbok.avantgoutrestaurant.com/and/mark-for-review.php";
    String urlRemove = "http://jmbok.avantgoutrestaurant.com/and/mark-for-review-delete.php";
    public RadioButton rButton = null;
    HashMap<String, String> editTextvalue = new HashMap<String, String>();
    MasterDownload httpRequest;
    public String ansOption;
    PreviewActivity previewActivity;


    LinearLayout reviewLayout;
    LinearLayout questionLayout;
    LinearLayout answerLayout;

    Button showReview;

    TextView questionView;
    TextView textQno;
    RadioGroup radioGroup;


    RadioButton rButton1;
    RadioButton rButton2;
    RadioButton rButton3;
    RadioButton rButton4;
    CheckBox reviewBox;


    public TestAdapter(Context conte, Quizdata form, int qNO, int device, String strSelect) {
        super();
        context = conte;
        data = form;
        queNo = qNO + 1;
        size = device;
        select = strSelect;
//        previewActivity=pv;
    }

    @Override
    public int getCount() {

        return 3;
    }

    @Override
    public Object getItem(int position) {

        return null;
    }

    @Override
    public long getItemId(int position) {

        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        final View uiview;
        LayoutInflater mInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (size == 7) {
            row = mInflater.inflate(R.layout.quiz_layout_seven, parent, false);
        } else {
            row = mInflater.inflate(R.layout.quiz_layout, parent, false);
        }
        if (row != null) {

            viewHolder = new ViewHolderA();


            reviewLayout = (LinearLayout) row.findViewById(R.id.review);

            questionLayout = (LinearLayout) row.findViewById(R.id.question);

            answerLayout = (LinearLayout) row.findViewById(R.id.answer);

            questionView = (TextView) questionLayout.findViewById(R.id.questionView);

            textQno = (TextView) row.findViewById(R.id.qNo);

            radioGroup = (RadioGroup) row.findViewById(R.id.radioGroup);

            rButton1 = (RadioButton) row.findViewById(R.id.RadioButton01);
            rButton2 = (RadioButton) row.findViewById(R.id.RadioButton02);
            rButton3 = (RadioButton) row.findViewById(R.id.RadioButton03);
            rButton4 = (RadioButton) row.findViewById(R.id.RadioButton04);

            reviewBox = (CheckBox) row.findViewById(R.id.rCheckBox);

            showReview = (Button) row.findViewById(R.id.showReview);
//            viewHolder.infoButton = (Button) row.findViewById(R.id.info);
            uiview = row;
        } else {
            viewHolder = (ViewHolderA) row.getTag();
            uiview = row;
        }


        if (position == 0) {
            reviewLayout.setVisibility(View.VISIBLE);
            questionLayout.setVisibility(View.GONE);
            answerLayout.setVisibility(View.GONE);
            int check = data.getISchecked();
            if (check == 1) {
                reviewBox.setChecked(true);
            } else {
                reviewBox.setChecked(false);
            }

//	            	String review = data.getStatus();
//	            	if (review != null) {
//	            		if (review.equals("A")) {
            reviewBox.setEnabled(false);
//						}
//					}
            if (select != null) {
                reviewLayout.setVisibility(View.GONE);
            }

        } else if (position == 1) {
            questionLayout.setVisibility(View.VISIBLE);
            reviewLayout.setVisibility(View.GONE);
            answerLayout.setVisibility(View.GONE);
            questionView.setText(data.getQuestion());
            textQno.setText("Q." + String.valueOf(queNo));
        } else if (position == 2) {
            answerLayout.setVisibility(View.VISIBLE);
            questionLayout.setVisibility(View.GONE);
            reviewLayout.setVisibility(View.GONE);
            radioGroup.setEnabled(true);
            rButton1.setText(data.getOptionA());
            rButton2.setText(data.getOptionB());
            rButton3.setText(data.getOptionC());
            rButton4.setText(data.getOptionD());
//					System.out.println("Wrong " + data.getWrongAnswer());
//					System.out.println("ans " + data.getIsAnswer());

//rButton1.setBackgroundColor(Color.YELLOW);

            if (select != null) {
                radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(RadioGroup group, int checkedId) {
                        count = 100;
                        RadioGroup RG = (RadioGroup) uiview.findViewById(R.id.radioGroup);
                        rButton1 = (RadioButton) uiview.findViewById(R.id.RadioButton01);
                        rButton2 = (RadioButton) uiview.findViewById(R.id.RadioButton02);
                        rButton3 = (RadioButton) uiview.findViewById(R.id.RadioButton03);
                        rButton4 = (RadioButton) uiview.findViewById(R.id.RadioButton04);
                        int id = group.getCheckedRadioButtonId();
//                        check(checkedId);
//                        ansOption = "yes";
                        data.setIsAnswer(1);
                        int answer = Integer.valueOf(data.getAnswer());
                        int selected = -1;

                        if (checkedId == R.id.RadioButton01) {
                            selected = 1;
                            rButton = rButton1;
                            rButton = (RadioButton) uiview.findViewById(checkedId);

//                            rButton1.setBackgroundColor(Color.YELLOW);

                        } else if (checkedId == R.id.RadioButton02) {
                            selected = 2;
//                            rButton = rButton2;
                            rButton = (RadioButton) uiview.findViewById(checkedId);
                        } else if (checkedId == R.id.RadioButton03) {
                            selected = 3;
//                            rButton = rButton3;
                            rButton = (RadioButton) uiview.findViewById(checkedId);
                        } else if (checkedId == R.id.RadioButton04) {
                            selected = 4;
//                            rButton = rButton4;
                            rButton = (RadioButton) uiview.findViewById(checkedId);
                        }
                        if (rButton != null) {
                            if (answer + 1 == selected) {
//                                rButton.setBackgroundColor(Color.GREEN);
                                String dd = rButton.getText().toString();
                                rButton1.setText("Correct answer");
                                rButton = (RadioButton) uiview.findViewById(id);
                                rButton.setBackgroundColor(Color.GREEN);
                            } else {

//                                rButton.setBackgroundColor(Color.RED);
                                data.setWrongAnswer(selected);
                                rButton = (RadioButton) uiview.findViewById(id);
//                                rButton.setBackgroundColor(Color.GREEN);
                                rButton.setBackgroundColor(Color.RED);
                            }
                        }

                        if (answer == 0) {
                            rButton1.setBackgroundColor(Color.GREEN);
                        } else if (answer == 1) {
                            rButton2.setBackgroundColor(Color.GREEN);
                        } else if (answer == 2) {
                            rButton3.setBackgroundColor(Color.GREEN);
                        } else if (answer == 3) {
                            rButton4.setBackgroundColor(Color.GREEN);
                        }

                        for (int i = 0; i < radioGroup.getChildCount(); i++) {

                            RG.getChildAt(i).setEnabled(false);
                        }
                        List<NameValuePair> params = new ArrayList<NameValuePair>();
                        params.add(new BasicNameValuePair("userid", "android@gmail.com"));
                        params.add(new BasicNameValuePair("qid", data.getQuestionID()));
                        AsyncTaskCall ask = new AsyncTaskCall(context, "review", params);
                        ask.execute(urlRemove);
                    }
                });
            } else {
                System.out.println("data.getIsAnswer()  " + data.getIsAnswer());
                if (data.getIsAnswer() == 1) {
                    int wrong = data.getWrongAnswer();
                    int selected = -1;

                    if (wrong == 1) {
                        selected = 1;
                        rButton = rButton1;

                    } else if (wrong == 2) {
                        selected = 2;
                        rButton = rButton2;
                    } else if (wrong == 3) {
                        selected = 3;
                        rButton = rButton3;
                    } else if (wrong == 4) {
                        selected = 4;
                        rButton = rButton4;
                    }

                    int answer = Integer.valueOf(data.getAnswer());
                    if (rButton != null) {
                        if (answer + 1 == selected) {

                            rButton.setBackgroundColor(Color.GREEN);
                        } else {
                            rButton.setBackgroundColor(Color.RED);
                        }
                    }

                    if (answer == 0) {
                        rButton1.setBackgroundColor(Color.GREEN);
                    } else if (answer == 1) {
                        rButton2.setBackgroundColor(Color.GREEN);
                    } else if (answer == 2) {
                        rButton3.setBackgroundColor(Color.GREEN);
                    } else if (answer == 3) {
                        rButton4.setBackgroundColor(Color.GREEN);
                    }


                    for (int i = 0; i < radioGroup.getChildCount(); i++) {
                        radioGroup.getChildAt(i).setEnabled(false);
                    }


                    count = 0;
                }
            }


        } else

        {
            answerLayout.setVisibility(View.GONE);
            questionLayout.setVisibility(View.GONE);
            reviewLayout.setVisibility(View.GONE);
        }

        return row;

    }


    static class ViewHolderA {

        LinearLayout reviewLayout;
        LinearLayout questionLayout;
        LinearLayout answerLayout;

        Button showReview;

        TextView questionView;
        TextView textQno;
        RadioGroup radioGroup;


        public RadioButton rButton1;
        public RadioButton rButton2;
        public RadioButton rButton3;
        public RadioButton rButton4;

        CheckBox reviewBox;

    }


}
