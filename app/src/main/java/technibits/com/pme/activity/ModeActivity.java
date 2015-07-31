package technibits.com.pme.activity;


import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;


import technibits.com.pme.R;

public class ModeActivity extends Fragment {
    ImageView study;
    ImageView exam, usr;
    View rootView;
    ModeActivity frag;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        frag = this;
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.mode_activity, container, false);
        study = (ImageView) rootView.findViewById(R.id.study);
        exam = (ImageView) rootView.findViewById(R.id.exam);
        study.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Intent intent = new Intent(getActivity(), ChooseCategoryActivity.class);
                intent.putExtra("mode", "study");
                startActivity(intent);
            }
        });
        exam.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Intent intent = new Intent(getActivity(), ChooseCategoryActivity.class);
                intent.putExtra("mode", "exam");
                startActivity(intent);
//                Intent intent = new Intent(getActivity(), PerformanceActivity.class);
//                startActivity(intent);
            }
        });
        return rootView;
    }
}
