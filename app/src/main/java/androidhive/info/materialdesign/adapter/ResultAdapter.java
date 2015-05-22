package androidhive.info.materialdesign.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidhive.info.materialdesign.activity.ExamFragment;
import androidhive.info.materialdesign.R;
import androidhive.info.materialdesign.activity.StudyModeFragment;
import androidhive.info.materialdesign.data.Quizdata;
import androidhive.info.materialdesign.data.ResultData;

public class ResultAdapter extends BaseAdapter {

private Context context;

ResultData data;
String formID;
EditText dateText;
public ViewHolderA viewHolder;
int counts;
int size;
StudyModeFragment studyfreg;
ExamFragment examfreg;

public ResultAdapter(Context conte,ResultData form,int count,int device,StudyModeFragment studyfeg) {
       super();
       context = conte;
       data = form;
       counts = count;
       size = device;
       studyfreg = studyfeg;
    }
public ResultAdapter(Context conte,ResultData form,int count,int device,ExamFragment examfeg) {
    super();
    context = conte;
    data = form;
    counts = count;
    size = device;
    examfreg = examfeg;
 }


   @Override
public int getCount() {
  // TODO Auto-generated method stub
  return 6;
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
//   	 if (size == 7 ) {
//   		 row = mInflater.inflate(R.layout.quiz_layout_seven,parent, false);
//		}else{
       	row = mInflater.inflate(R.layout.result_adapter,parent, false);
//		}
       	 if (row != null) {
       		 viewHolder = new ViewHolderA();
       		 
       		 viewHolder.values = (TextView)row.findViewById( R.id.value);
       		 viewHolder.results =  (TextView)row.findViewById( R.id.result);

       		 
       	 }else{
       		 viewHolder = (ViewHolderA) row.getTag();
       	 }
       	
      if (position == 0) {
    	  viewHolder.values.setText("Total Question");
    	  viewHolder.results.setText(String.valueOf(data.getTotalQuestion()));
      }else if (position == 1) {
    	  viewHolder.values.setText("Attempt Question");
    	  viewHolder.results.setText(String.valueOf(data.getAttemptQuestions()));
      }else if (position == 2) {
    	  viewHolder.values.setText("Correct Answers");
    	  viewHolder.results.setText(String.valueOf(data.getCorrectAnswers()));
      }else if (position == 3) {
    	  viewHolder.values.setText("Marked for Review");
    	  viewHolder.results.setText(String.valueOf(data.getMarkedReview()));
      }else if (position == 4) {
    	  viewHolder.values.setText("Percentage");
    	  viewHolder.results.setText(String.valueOf(data.getPercentage())+"%");
      }else if (position == 5) {
    	  viewHolder.values.setText("Result");
    	  viewHolder.results.setText(data.getResult());
      }

       return row;
       
   }
    
    
    static class ViewHolderA {

   	 TextView values;
   	 TextView results;

    }

}
