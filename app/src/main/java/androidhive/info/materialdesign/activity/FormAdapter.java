package androidhive.info.materialdesign.activity;


import android.content.Context;

import android.view.View;

import android.view.ViewGroup;
import android.view.LayoutInflater;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import android.widget.EditText;


import java.util.ArrayList;
import java.util.HashMap;

import androidhive.info.materialdesign.R;
import androidhive.info.materialdesign.data.Quizdata;


public class FormAdapter extends BaseAdapter {
	 private Context context;
	 StudyModeFragment activity;
	 Quizdata data;
	 String formID;
	 EditText dateText;
	 public ViewHolderA viewHolder;
 
	 HashMap<String, String> editTextvalue = new HashMap<String,String>();

	 public FormAdapter(Context conte,Quizdata form) {
	        super();
	        context = conte;
	        data = form;
	       
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
	    	 final String rowId = Integer.valueOf(position).toString();
	    	 LayoutInflater mInflater = (LayoutInflater) context
	                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	        	row = mInflater.inflate(R.layout.quiz_layout,parent, false);
	        	
	            if (row != null) {
	           
	        	viewHolder = new ViewHolderA();

	        	viewHolder.reviewLayout = (LinearLayout)row.findViewById( R.id.review);
	        	
	        	viewHolder.questionLayout = (LinearLayout)row.findViewById( R.id.question);
	  
	            viewHolder.answerLayout = (LinearLayout)row.findViewById( R.id.answer);
	            
	            viewHolder.questionView = (TextView)viewHolder.questionLayout.findViewById( R.id.questionView);
	            }else{
	            	viewHolder = (ViewHolderA) row.getTag();
	            }

	            
	            if (position == 0) {
	            	viewHolder.reviewLayout.setVisibility(View.VISIBLE);
	            	viewHolder.questionLayout.setVisibility(View.GONE);
	            	viewHolder.answerLayout.setVisibility(View.GONE);
				}else if (position == 1) {
					viewHolder.questionLayout.setVisibility(View.VISIBLE);
					viewHolder.reviewLayout.setVisibility(View.GONE);
					viewHolder.answerLayout.setVisibility(View.GONE);
					viewHolder.questionView.setText(data.getQuestion());
				}else if (position == 2) {
					viewHolder.answerLayout.setVisibility(View.VISIBLE);
					viewHolder.questionLayout.setVisibility(View.GONE);
					viewHolder.reviewLayout.setVisibility(View.GONE);

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
	    	 
	    	 TextView questionView;
	     
	     }

}




