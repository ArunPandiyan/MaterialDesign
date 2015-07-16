package technibits.com.pme.activity;

/**
 * Created by Technibits-13 on 15-May-2015.
 */

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import technibits.com.pme.R;


public class VideoFragment extends Fragment {

    public VideoFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_video, container, false);
        Button click = (Button) rootView.findViewById(R.id.click);
        click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), WebviewActivity.class);
//						   Bundle bundleObject = new Bundle();
//						   bundleObject.putSerializable("data", data);
//						   intent.putExtras(bundleObject);

                startActivity(intent);
            }
        });

        // Inflate the layout for this fragment
        return rootView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}
