package technibits.com.pme.adapter;

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


public class PreviewAdapter extends BaseAdapter {
    private Context context;
    ReviewFragment activity;

    Quizdata data;
    String formID;
    EditText dateText;
    public ViewHolderA viewHolder;
    int queNo;
    int size;
    String select;
    public ResultData resData;
    String urlMark = "http://jmbok.techtestbox.com/and/mark-for-review.php";
    String urlRemove = "http://jmbok.techtestbox.com/and/mark-for-review-delete.php";
    public RadioButton rButton = null;
    HashMap<String, String> editTextvalue = new HashMap<String, String>();
    MasterDownload httpRequest;
    public String ansOption;
    PreviewActivity previewActivity;

    public PreviewAdapter(Context conte, Quizdata form, int qNO, int device, String strSelect) {
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
        LayoutInflater mInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (size == 7) {
            row = mInflater.inflate(R.layout.quiz_layout_seven, parent, false);
        } else {
            row = mInflater.inflate(R.layout.quiz_layout, parent, false);
        }
        if (row != null) {

            viewHolder = new ViewHolderA();

            viewHolder.reviewLayout = (LinearLayout) row.findViewById(R.id.review);

            viewHolder.questionLayout = (LinearLayout) row.findViewById(R.id.question);

            viewHolder.answerLayout = (LinearLayout) row.findViewById(R.id.answer);

            viewHolder.questionView = (TextView) viewHolder.questionLayout.findViewById(R.id.questionView);

            viewHolder.textQno = (TextView) row.findViewById(R.id.qNo);

            viewHolder.radioGroup = (RadioGroup) row.findViewById(R.id.radioGroup);

            viewHolder.rButton1 = (RadioButton) row.findViewById(R.id.RadioButton01);
            viewHolder.rButton2 = (RadioButton) row.findViewById(R.id.RadioButton02);
            viewHolder.rButton3 = (RadioButton) row.findViewById(R.id.RadioButton03);
            viewHolder.rButton4 = (RadioButton) row.findViewById(R.id.RadioButton04);

            viewHolder.reviewBox = (CheckBox) row.findViewById(R.id.rCheckBox);

            viewHolder.showReview = (Button) row.findViewById(R.id.showReview);
//            viewHolder.infoButton = (Button) row.findViewById(R.id.info);

        } else {
            viewHolder = (ViewHolderA) row.getTag();
        }


        if (position == 0) {
            viewHolder.reviewLayout.setVisibility(View.VISIBLE);
            viewHolder.questionLayout.setVisibility(View.GONE);
            viewHolder.answerLayout.setVisibility(View.GONE);
            int check = data.getISchecked();
            if (check == 1) {
                viewHolder.reviewBox.setChecked(true);
            } else {
                viewHolder.reviewBox.setChecked(false);
            }

//	            	String review = data.getStatus();
//	            	if (review != null) {
//	            		if (review.equals("A")) {
            viewHolder.reviewBox.setEnabled(false);
//						}
//					}
            if (select != null) {
                viewHolder.reviewLayout.setVisibility(View.GONE);
            }

        } else if (position == 1) {
            viewHolder.questionLayout.setVisibility(View.VISIBLE);
            viewHolder.reviewLayout.setVisibility(View.GONE);
            viewHolder.answerLayout.setVisibility(View.GONE);
            viewHolder.questionView.setText(data.getQuestion());
            viewHolder.textQno.setText("Q." + String.valueOf(queNo));
        } else if (position == 2) {
            viewHolder.answerLayout.setVisibility(View.VISIBLE);
            viewHolder.questionLayout.setVisibility(View.GONE);
            viewHolder.reviewLayout.setVisibility(View.GONE);
            viewHolder.radioGroup.setEnabled(true);
            viewHolder.rButton1.setText(data.getOptionA());
            viewHolder.rButton2.setText(data.getOptionB());
            viewHolder.rButton3.setText(data.getOptionC());
            viewHolder.rButton4.setText(data.getOptionD());
//					System.out.println("Wrong " + data.getWrongAnswer());
//					System.out.println("ans " + data.getIsAnswer());
            final View uiview = row;
//            viewHolder.rButton1.setBackgroundColor(Color.YELLOW);

            if (select != null) {
                viewHolder.radioGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(RadioGroup group, int checkedId) {
                        viewHolder.radioGroup = (RadioGroup) uiview.findViewById(R.id.radioGroup);
                        viewHolder.rButton1 = (RadioButton) uiview.findViewById(R.id.RadioButton01);
                        viewHolder.rButton2 = (RadioButton) uiview.findViewById(R.id.RadioButton02);
                        viewHolder.rButton3 = (RadioButton) uiview.findViewById(R.id.RadioButton03);
                        viewHolder.rButton4 = (RadioButton) uiview.findViewById(R.id.RadioButton04);
                        ansOption = "yes";
                        data.setIsAnswer(1);
                        int answer = Integer.valueOf(data.getAnswer());
                    int selected = -1;

                        if (checkedId == R.id.RadioButton01) {
                        selected = 1;
                        rButton = viewHolder.rButton1;
                            viewHolder.rButton1.setBackgroundColor(Color.YELLOW);

                        } else if (checkedId == R.id.RadioButton02) {
                        selected = 2;
                        rButton = viewHolder.rButton2;
                        } else if (checkedId == R.id.RadioButton03) {
                        selected = 3;
                        rButton = viewHolder.rButton3;
                        } else if (checkedId == R.id.RadioButton04) {
                        selected = 4;
                        rButton = viewHolder.rButton4;
                    }
                    if (rButton != null) {
                        if (answer + 1 == selected) {
                            rButton.setBackgroundColor(Color.parseColor("#00C853"));
                            String dd = rButton.getText().toString();
                            rButton.setText("Correct answer");
                        } else {

                            rButton.setBackgroundColor(Color.RED);
                            data.setWrongAnswer(selected);
                            String dd = rButton.getText().toString();
                            System.out.println(dd);
                        }
                    }

                    if (answer == 0) {
                        viewHolder.rButton1.setBackgroundColor(Color.parseColor("#00C853"));
                    } else if (answer == 1) {
                        viewHolder.rButton2.setBackgroundColor(Color.parseColor("#00C853"));
                    } else if (answer == 2) {
                        viewHolder.rButton3.setBackgroundColor(Color.parseColor("#00C853"));
                    } else if (answer == 3) {
                        viewHolder.rButton4.setBackgroundColor(Color.parseColor("#00C853"));
                    }

//                        for (int i = 0; i < viewHolder.radioGroup.getChildCount(); i++) {
//                            viewHolder.radioGroup.getChildAt(i).setEnabled(false);
//                        }
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
                        rButton = viewHolder.rButton1;
                    } else if (wrong == 2) {
                        selected = 2;
                        rButton = viewHolder.rButton2;
                    } else if (wrong == 3) {
                        selected = 3;
                        rButton = viewHolder.rButton3;
                    } else if (wrong == 4) {
                        selected = 4;
                        rButton = viewHolder.rButton4;
                    }

                    int answer = Integer.valueOf(data.getAnswer());
                    if (rButton != null) {
                        if (answer + 1 == selected) {

                            rButton.setBackgroundColor(Color.parseColor("#00C853"));
                        } else {
                            rButton.setBackgroundColor(Color.RED);
                    }
                    }

                    if (answer == 0) {
                        viewHolder.rButton1.setBackgroundColor(Color.parseColor("#00C853"));
                    } else if (answer == 1) {
                        viewHolder.rButton2.setBackgroundColor(Color.parseColor("#00C853"));
                    } else if (answer == 2) {
                        viewHolder.rButton3.setBackgroundColor(Color.parseColor("#00C853"));
                    } else if (answer == 3) {
                        viewHolder.rButton4.setBackgroundColor(Color.parseColor("#00C853"));
                    }


                    for (int i = 0; i < viewHolder.radioGroup.getChildCount(); i++) {
                        viewHolder.radioGroup.getChildAt(i).setEnabled(false);
                }


                }
        }


    } else

        {
            viewHolder.answerLayout.setVisibility(View.GONE);
            viewHolder.questionLayout.setVisibility(View.GONE);
            viewHolder.reviewLayout.setVisibility(View.GONE);
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
