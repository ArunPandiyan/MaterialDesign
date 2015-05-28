package androidhive.info.materialdesign.adapter;

import java.util.ArrayList;

import androidhive.info.materialdesign.adapter.ResultAdapter.ViewHolderA;
import androidhive.info.materialdesign.activity.PerformanceActivity;
import androidhive.info.materialdesign.R;
import androidhive.info.materialdesign.activity.StudyModeFragment;
import androidhive.info.materialdesign.data.Quizdata;
import androidhive.info.materialdesign.data.ResultData;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

public class PerformanceAdapter extends BaseAdapter {

	private Context context;
	ArrayList<ResultData> resData;
	int size;
	public ViewHolderA viewHolder;

	public PerformanceAdapter(Context conte, ArrayList<ResultData> rData,
			int device) {
		super();
		context = conte;
		resData = rData;
		size = device;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return resData.size();
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

		if (size == 7) {
			row = mInflater.inflate(R.layout.performancehis_item_seven, parent,
					false);
		} else {
			row = mInflater
					.inflate(R.layout.performancehis_item, parent, false);
		}

		if (row != null) {
      		 viewHolder = new ViewHolderA();
      		viewHolder.quizname = (TextView)row.findViewById( R.id.quizname);
      		 viewHolder.totalNoque = (TextView)row.findViewById( R.id.totalNoque);
      		 viewHolder.queAttemnt =  (TextView)row.findViewById( R.id.queAttemnt);
      		 viewHolder.queMarked = (TextView)row.findViewById( R.id.queMarked);
      		 viewHolder.currectAns =  (TextView)row.findViewById( R.id.currectAns);
      		 viewHolder.percentace = (TextView)row.findViewById( R.id.percentace);
      		 viewHolder.result =  (TextView)row.findViewById( R.id.result);
      		viewHolder.nextReview = (Button)row.findViewById(R.id.nextReview);


		}else{
      		 viewHolder = (ViewHolderA) row.getTag();
      	 }
		viewHolder.quizname.setText(resData.get(position).getQuizName());
		viewHolder.totalNoque.setText(String.valueOf(resData.get(position).getTotalQuestion()));
		viewHolder.queAttemnt.setText(String.valueOf(resData.get(position).getAttemptQuestions()));
		viewHolder.currectAns.setText(String.valueOf(resData.get(position).getCorrectAnswers()));
		viewHolder.percentace.setText(String.valueOf(resData.get(position).getPercentage()));
		viewHolder.result.setText(String.valueOf(resData.get(position).getResult()));

		viewHolder.nextReview.setId(resData.get(position).getTestID());
		viewHolder.nextReview.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
				int d = v.getId();
				((PerformanceActivity)context).performReview(v.getId());
            }
  		});
		// TODO Auto-generated method stub
		return row;
	}

	static class ViewHolderA {

		TextView quizname;
		TextView totalNoque;
		TextView queAttemnt;
		TextView queMarked;
		TextView currectAns;
		TextView percentace;
		TextView result;
		
		Button nextReview;

	}

}
