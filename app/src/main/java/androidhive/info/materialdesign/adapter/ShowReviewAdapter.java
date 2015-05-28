package androidhive.info.materialdesign.adapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

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
import android.widget.RadioGroup.OnCheckedChangeListener;

import androidhive.info.materialdesign.activity.ExamFragment;
import androidhive.info.materialdesign.R;
import androidhive.info.materialdesign.activity.StudyModeFragment;
import androidhive.info.materialdesign.data.Quizdata;

public class ShowReviewAdapter extends BaseAdapter {
	 private Context context;
 
	 ArrayList<Quizdata> data;
	 String formID;
	 EditText dateText;
	 public ViewHolderA viewHolder;
	 int counts;
	 int size;
	 StudyModeFragment studyfreg;
	 ExamFragment examfreg;
 
	 public ShowReviewAdapter(Context conte,ArrayList<Quizdata> form,int count,int device,StudyModeFragment studyfeg) {
	        super();
	        context = conte;
	        data = form;
	        counts = count;
	        size = device;
	        studyfreg = studyfeg;
	     }
	 
	 public ShowReviewAdapter(Context conte,ArrayList<Quizdata> form,int count,int device,ExamFragment studyfeg) {
	        super();
	        context = conte;
	        data = form;
	        counts = count;
	        size = device;
	        examfreg = studyfeg;
	     }
	 

	    @Override
   public int getCount() {

			return counts;
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
//	    	 if (size == 7 ) {
//	    		 row = mInflater.inflate(R.layout.quiz_layout_seven,parent, false);
//			}else{
	        	row = mInflater.inflate(R.layout.show_review_study_mode,parent, false);
//			}
	        	 if (row != null) {
	        		 viewHolder = new ViewHolderA();
	        		 
	        		 viewHolder.showAns = (Button)row.findViewById( R.id.butAns);
//	        		 viewHolder.showunAnswer = (Button)row.findViewById( R.id.butunAns);
//	        		 viewHolder.showReview = (Button)row.findViewById( R.id.butReview);
	        		 
	        	 }else{
	        		 viewHolder = (ViewHolderA) row.getTag();
	        	 }
	        	 int qNo =  position+1;
	        	 Quizdata quzData = data.get(position);
	        	 String status = quzData.getStatus();
	        	 if (status == null) {
	 					viewHolder.showAns.setText("Unanswered (Q) " +qNo );
	 					viewHolder.showAns.setTextColor(Color.RED);
	 					viewHolder.showAns.setId(position);
//	 					viewHolder.showAns.setEnabled(true);
//	 					viewHolder.showAns.setEnabled(false);
//	 					viewHolder.showReview.setEnabled(false);
				}else{
					if (status.equals("R") || status.equals("A")) {
						if (status.equals("R")) {
							viewHolder.showAns.setText("Review (Q) " +qNo);
							viewHolder.showAns.setId(position);
//							viewHolder.showAns.setEnabled(true);
							
//							viewHolder.showAns.setEnabled(false);
//							viewHolder.showunAnswer.setEnabled(false);
						}else if (status.equals("A")) {
							viewHolder.showAns.setText("Answerd(Q) " +qNo);
							viewHolder.showAns.setId(position);
							viewHolder.showAns.setTextColor(Color.GREEN);
//							viewHolder.showAns.setEnabled(true);
//							viewHolder.showReview.setEnabled(false);
//							viewHolder.showunAnswer.setEnabled(false);
						}
					}
				}
	        	 
	        	 
//	        	 viewHolder.showReview.setOnClickListener(new View.OnClickListener() {
//	 	            public void onClick(View v) {
//	 	            	studyfreg.reviewNavication(v.getId());
////	 	            	showReview();
//	 	            }
//	 	          });
	        	 viewHolder.showAns.setOnClickListener(new View.OnClickListener() {
		 	            public void onClick(View v) {
//		 	            	showReview();
		 	            	if (studyfreg != null) {
		 	            		studyfreg.reviewNavication(v.getId());
							}else {
								examfreg.reviewNavication(v.getId());
							}
		 	            	
		 	            }
		 	          });
//	        	 viewHolder.showunAnswer.setOnClickListener(new View.OnClickListener() {
//		 	            public void onClick(View v) {
////		 	            	showReview();
//		 	            	studyfreg.reviewNavication(v.getId());
//		 	            }
//		 	          });
	        	 
	        return row;
	        
	    }
	     
	     
	     static class ViewHolderA {

	    	 Button showAns;
	    	 Button showunAnswer;
	    	 Button showReview;

	     }

}