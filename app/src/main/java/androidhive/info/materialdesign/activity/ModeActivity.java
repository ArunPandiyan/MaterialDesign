package androidhive.info.materialdesign.activity;



import java.util.UUID;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import androidhive.info.materialdesign.R;

public class ModeActivity extends Activity{
	Button study;
	Button exam;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mode_activity);
		
	
		
		study = (Button)findViewById(R.id.study);
		exam = (Button)findViewById(R.id.exam);
		study.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(ModeActivity.this, MainActivity_as.class);
				intent.putExtra("mode", "study");
				startActivity(intent);
			}
		});
		exam.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(ModeActivity.this, MainActivity_as.class);
				intent.putExtra("mode", "exam");
				startActivity(intent);
			}
		});
		
	}

}
