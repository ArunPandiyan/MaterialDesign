package androidhive.info.materialdesign.adapter;


import android.content.Context;
import android.graphics.Color;

import android.view.View;

import android.view.ViewGroup;
import android.view.LayoutInflater;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import android.widget.EditText;
import android.widget.RadioGroup.OnCheckedChangeListener;


import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.message.BasicNameValuePair;

import androidhive.info.materialdesign.activity.AsyncTaskCall;
import androidhive.info.materialdesign.activity.ExamFragment;
import androidhive.info.materialdesign.activity.MasterDownload;
import androidhive.info.materialdesign.R;
import androidhive.info.materialdesign.activity.StudyModeFragment;
import androidhive.info.materialdesign.data.Quizdata;
import androidhive.info.materialdesign.data.ResultData;


public class StudyAdapter extends BaseAdapter {
	 private Context context;
	 StudyModeFragment activity;
	 
	 Quizdata data;
	 String formID;
	 EditText dateText;
	 public ViewHolderA viewHolder;
	 int queNo;
	 int size;
	 public ResultData resData;
	 String urlMark ="http://jmbok.avantgoutrestaurant.com/and/mark-for-review.php";
	 String urlRemove ="http://jmbok.avantgoutrestaurant.com/and/mark-for-review-delete.php";

	 HashMap<String, String> editTextvalue = new HashMap<String,String>();
     MasterDownload httpRequest;
	 public StudyAdapter(Context conte,Quizdata form,int qNO,int device,StudyModeFragment studuyFrg,ResultData resD) {
	        super();
	        context = conte;
	        data = form;
	        queNo = qNO+1;
	        size = device;
	        activity = studuyFrg;
	        resData = resD;
	     }

	    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return 3;
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return 0;
    }

	     @Override
	     public View getView(int position, View convertView, ViewGroup parent) {
	    	 View row = convertView;
	    	 LayoutInflater mInflater = (LayoutInflater) context
	                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	    	 if (size == 7 ) {
	    		 row = mInflater.inflate(R.layout.quiz_layout_seven,parent, false);
			}else{
	        	row = mInflater.inflate(R.layout.quiz_layout,parent, false);
			}
	            if (row != null) {
	           
	        	viewHolder = new ViewHolderA();

	        	viewHolder.reviewLayout = (LinearLayout)row.findViewById( R.id.review);
	        	
	        	viewHolder.questionLayout = (LinearLayout)row.findViewById( R.id.question);
	  
	            viewHolder.answerLayout = (LinearLayout)row.findViewById( R.id.answer);
	            
	            viewHolder.questionView = (TextView)viewHolder.questionLayout.findViewById( R.id.questionView);
	            
	            viewHolder.textQno = (TextView)row.findViewById(R.id.qNo);
	            
	            viewHolder.radioGroup = (RadioGroup)row.findViewById(R.id.radioGroup);
	            
	            viewHolder.rButton1 = (RadioButton)row.findViewById(R.id.RadioButton01);
	            viewHolder.rButton2 = (RadioButton)row.findViewById(R.id.RadioButton02);
	            viewHolder.rButton3 = (RadioButton)row.findViewById(R.id.RadioButton03);
	            viewHolder.rButton4 = (RadioButton)row.findViewById(R.id.RadioButton04);
	            
	            viewHolder.reviewBox = (CheckBox)row.findViewById(R.id.rCheckBox);
	            
	            viewHolder.showReview = (Button)row.findViewById(R.id.showReview);
	            
	            viewHolder.infoButton = (Button)row.findViewById(R.id.info);
	            
	            }else{
	            	viewHolder = (ViewHolderA) row.getTag();
	            }

	            
	            if (position == 0) {
	            	viewHolder.reviewLayout.setVisibility(View.VISIBLE);
	            	viewHolder.questionLayout.setVisibility(View.GONE);
	            	viewHolder.answerLayout.setVisibility(View.GONE);
	            	int check = data.getISchecked();
	            	if (check == 1 ) {
	            		viewHolder.reviewBox.setChecked(true);
					}else{
						viewHolder.reviewBox.setChecked(false);
					}
	            	
	            	String review = data.getStatus();
	            	if (review != null) {
	            		if (review.equals("A")) {
		            		viewHolder.reviewBox.setEnabled(false);
						}
					}
	            	
	            	viewHolder.reviewBox.setOnClickListener(new View.OnClickListener() {
	            	      public void onClick(View v) {
	            	        if (((CheckBox) v).isChecked()){
	            	        	data.setISchecked(1);
	            	        	data.setStatus("R");
	            	    	List<NameValuePair> params = new ArrayList<NameValuePair>();
	            	    	params.add(new BasicNameValuePair("userid", "android@gmail.com"));
	            			params.add(new BasicNameValuePair("qid", data.getQuestionID()));
	            		
	            			AsyncTaskCall ask = new AsyncTaskCall(context, "review",params);
	            			ask.execute(urlMark);
	            			int mrCount = resData.getMarkedReview()+1;
	            			resData.setMarkedReview(mrCount);
	            			
	            	        }
	            	        else{
	            	        	data.setISchecked(0);
	            	        	List<NameValuePair> params = new ArrayList<NameValuePair>();
		            	    	params.add(new BasicNameValuePair("userid", "android@gmail.com"));
		            			params.add(new BasicNameValuePair("qid", data.getQuestionID()));
		            			AsyncTaskCall ask = new AsyncTaskCall(context, "review",params);
		            			ask.execute(urlRemove);
		            			int mrCount = resData.getMarkedReview()-1;
		            			resData.setMarkedReview(mrCount);
	            	        }
	            	      }
	            	    });

	            	viewHolder.showReview.setOnClickListener(new View.OnClickListener() {
	      	            public void onClick(View v) {
	      	            	
	      	            		activity.showReview();

	      	            }
	      	          });
            	
	            	 
				}else if (position == 1) {
					viewHolder.questionLayout.setVisibility(View.VISIBLE);
					viewHolder.reviewLayout.setVisibility(View.GONE);
					viewHolder.answerLayout.setVisibility(View.GONE);
					viewHolder.questionView.setText(data.getQuestion());
					viewHolder.textQno.setText("Q."+String.valueOf(queNo));
					
					viewHolder.infoButton.setVisibility(View.GONE);
				}else if (position == 2) {
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
					final View uiview  = row;
					
					if (data.getIsAnswer() == 1) {
						int wrong = data.getWrongAnswer();
						int selected = -1;
		        		RadioButton rButton = null;
		        		if(wrong == 1){
		        			selected = 1;
		        			rButton = viewHolder.rButton1;
		        		}
		        		else if(wrong == 2){
		        			selected = 2;
		        			rButton = viewHolder.rButton2;
		        		}
		        		else if(wrong == 3){
		        			selected = 3;
		        			rButton = viewHolder.rButton3;
		        		}
		        		else if(wrong == 4){
		        			selected = 4;
		        			rButton = viewHolder.rButton4;
		        		}
		        		
		        		int answer = Integer.valueOf(data.getAnswer());
		        		if (rButton != null) {
		        		if(answer+1 == selected){
		        			
		        			rButton .setBackgroundColor(Color.GREEN);		        			
		        		}else{
		        			rButton .setBackgroundColor(Color.RED);
		        		}
		        		}
		        			
		        			if (answer == 0) {
		        				viewHolder.rButton1.setBackgroundColor(Color.GREEN);
							}else if (answer == 1) {
		        				viewHolder.rButton2.setBackgroundColor(Color.GREEN);
							}else if (answer == 2) {
		        				viewHolder.rButton3.setBackgroundColor(Color.GREEN);
							}else if (answer == 3) {
		        				viewHolder.rButton4.setBackgroundColor(Color.GREEN);
							}
		        			
		        		
		        		for (int i = 0; i < viewHolder.radioGroup.getChildCount(); i++) {
		        			viewHolder.radioGroup.getChildAt(i).setEnabled(false);
		        			}
		        		
		        		
		            }else{
						
					viewHolder.radioGroup.setOnCheckedChangeListener(new OnCheckedChangeListener(){
				        	@Override
				            public void onCheckedChanged(RadioGroup group, int checkedId) {
				        		data.setIsAnswer(1);
				        		int answer = Integer.valueOf(data.getAnswer());
				        		int selected = -1;
				        		RadioButton rButton = null;
				        		if(checkedId == R.id.RadioButton01){
				        			selected = 1;
				        			rButton = viewHolder.rButton1;
				        		}
				        		else if(checkedId == R.id.RadioButton02){
				        			selected = 2;
				        			rButton = viewHolder.rButton2;
				        		}
				        		else if(checkedId == R.id.RadioButton03){
				        			selected = 3;
				        			rButton = viewHolder.rButton3;
				        		}
				        		else if(checkedId == R.id.RadioButton04){
				        			selected = 4;
				        			rButton = viewHolder.rButton4;
				        		}
				        		if (rButton != null) {
				        		if(answer+1 == selected){
				        			int caCount = resData.getCorrectAnswers() + 1;
				        			resData.setCorrectAnswers(caCount);
				        			rButton .setBackgroundColor(Color.GREEN);
				        		}else{
				        			rButton .setBackgroundColor(Color.RED);
				        			data.setWrongAnswer(selected);
				        		}
				        		}
				        			
				        			if (answer == 0) {
				        				viewHolder.rButton1.setBackgroundColor(Color.GREEN);
									}else if (answer == 1) {
				        				viewHolder.rButton2.setBackgroundColor(Color.GREEN);
									}else if (answer == 2) {
				        				viewHolder.rButton3.setBackgroundColor(Color.GREEN);
									}else if (answer == 3) {
				        				viewHolder.rButton4.setBackgroundColor(Color.GREEN);
									}
				        			
				        		
				        	
				        		for (int i = 0; i < viewHolder.radioGroup.getChildCount(); i++) {
				        			viewHolder.radioGroup.getChildAt(i).setEnabled(false);
				        			}
				        		
				        		if (data.getISchecked() == 1) {
				        			viewHolder.reviewBox.setChecked(false);
				        		  	data.setISchecked(0);
		            	        	List<NameValuePair> params = new ArrayList<NameValuePair>();
			            	    	params.add(new BasicNameValuePair("userid", "android@gmail.com"));
			            			params.add(new BasicNameValuePair("qid", data.getQuestionID()));
			            			AsyncTaskCall ask = new AsyncTaskCall(context, "review",params);
			            			ask.execute(urlRemove);
			            			int mrCount = resData.getMarkedReview()-1;
			            			resData.setMarkedReview(mrCount);
								}
				        		
				        		data.setStatus("A");
				        		
				        		int atCount = resData.getAttemptQuestions()+1;
				        		resData.setAttemptQuestions(atCount);
				        		viewHolder.reviewBox.setEnabled(false);
				        		activity.refersh();
				            }
				        });
		            }

				}else{
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
	    	 
	    	 
	    	 RadioButton rButton1;
	    	 RadioButton rButton2;
	    	 RadioButton rButton3;
	    	 RadioButton rButton4;
	    	 
	    	 CheckBox reviewBox;
	    	 
	    	 Button infoButton;
	     
	     }

}