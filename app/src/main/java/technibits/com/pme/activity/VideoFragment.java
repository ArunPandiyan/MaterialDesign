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
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;
import com.google.api.services.youtube.model.Video;

import butterknife.Bind;
import butterknife.ButterKnife;
import technibits.com.pme.youtube.PlayerActivity;
import technibits.com.pme.youtube.YoutubeListView;

import java.util.ArrayList;
import java.util.List;
import technibits.com.pme.R;
import technibits.com.pme.data.DeveloperKey;


public class VideoFragment extends Fragment {
    @Bind(R.id.youtube_list_view) YoutubeListView mYoutubeListView;
    @Bind(R.id.progress_indicator_view) ProgressBar mProgressBar;



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
        ButterKnife.bind(getActivity(), rootView);

        List<String> ids = new ArrayList<String>();
        ids.add("iX-QaNzd-0Y");
        ids.add("nCkpzqqog4k");
        ids.add("pB-5XG-DbAA");
        ids.add("QD7qIthSdkA");
        ids.add("GtKnRFNffsI");
        ids.add("IIA1XQnAv5s");
        ids.add("6vopR3ys8Kw");
        ids.add("uJ_1HMAGb4k");
        ids.add("MYSVMgRr6pw");
        ids.add("oWYp1xRPH5g");
        ids.add("qlGQoxzdwP4");
        ids.add("4ZHwu0uut3k");
        ids.add("b6dD-I7kJmM");
        ids.add("NDH1bGnNMjw");
        ids.add("rnqUBmd5xRo");
        ids.add("fJ5LaPyzaj0");
        ids.add("6teOmBuMxw4");
        ids.add("RBumgq5yVrA");


        // Show loader here
        mProgressBar.setVisibility(View.VISIBLE);

        mYoutubeListView.init( DeveloperKey.DEVELOPER_KEY, ids, new YoutubeListView.OnListViewLoad() {
            @Override
            public void onLoad() {
                // Hide loader here.
                mProgressBar.setVisibility(View.GONE);
            }

            @Override
            public void onError(Throwable error) {
                // Hide loader
                mProgressBar.setVisibility(View.GONE);

                Toast.makeText(getActivity(), "There was an error " + error, Toast.LENGTH_LONG).show();
            }
        });

        mYoutubeListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final Video video = (Video) parent.getItemAtPosition(position);
                showVideo(video);
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
    private void showVideo(Video video) {
        PlayerActivity.showPlayer(getActivity(), DeveloperKey.DEVELOPER_KEY, video.getId());
    }
}
