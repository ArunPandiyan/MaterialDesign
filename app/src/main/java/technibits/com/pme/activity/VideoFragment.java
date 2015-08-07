package technibits.com.pme.activity;

/**
 * Created by Technibits-13 on 15-May-2015.
 */

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import technibits.com.pme.R;
import technibits.com.pme.adapter.VideoListAdapter;
import technibits.com.pme.alarmactivity.RemindMe;
import technibits.com.pme.alarmactivity.Util;
import technibits.com.pme.data.ScheduleInfo;
import technibits.com.pme.data.VideoInfo;


public class VideoFragment extends Fragment {

    public View rootView;
    public RecyclerView video_list;
    public List<VideoInfo> result;
    private SQLiteDatabase db;
    public VideoListAdapter videoListAdapter;
    private LinearLayoutManager llm;
    private Activity activity;

    public VideoFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = RemindMe.db;
        activity=getActivity();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
         rootView = inflater.inflate(R.layout.fragment_video, container, false);
        video_list = (RecyclerView) rootView.findViewById(R.id.video_list);
        
//        Button click = (Button) rootView.findViewById(R.id.click);
//        click.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(getActivity(), WebviewActivity.class);
////						   Bundle bundleObject = new Bundle();
////						   bundleObject.putSerializable("data", data);
////						   intent.putExtras(bundleObject);
//
//                startActivity(intent);
//            }
//        });
        llm = new LinearLayoutManager(activity);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        video_list.setLayoutManager(llm);

        video_list.setHasFixedSize(true);
        video_list.setItemAnimator(new DefaultItemAnimator());
        result = createList();


                videoListAdapter = new VideoListAdapter(result);
        video_list.setAdapter(videoListAdapter);
        return rootView;
    }
//Todo: modify according to data in video list
    private List<VideoInfo> createList() {
        List<VideoInfo> result = new ArrayList();
        for (int i = 0; i < 12; i++) {


            VideoInfo ci = new VideoInfo();
            ci.setTitle("First Video "+i);
            ci.setAbout_video("this is about video");
            ci.setLength("02:07");
            ci.setThumbnail("http://i.imgur.com/hgQam2A.png?1");
            ci.setVideo_url("http://jmbok.techtestbox.com/jwtest.html");

            result.add(ci);
        }
//        Cursor c = RemindMe.dbHelper.getNotifications(db);
//        getActivity().startManagingCursor(c);
//        c.moveToFirst();
//        while (c.isAfterLast() == false) {
//            VideoInfo ci = new VideoInfo();
//            ci.setTitle(c.getInt(1));
//            ci.setName(c.getString(0));
//
//            long datetime_fromdb = c.getLong(2);
//            Date date = new Date();
//            date.setTime(datetime_fromdb);
//
//            ci.setYear(String.valueOf(date.getYear() + 1900));
////            ci.setMonth(String.valueOf(monthArr[date.getMonth() + 1]));
//            ci.setDate(String.valueOf(date.getDate()));
//            long now = System.currentTimeMillis();
//
//            String txt = RemindMe.showRemainingTime() ? Util.getRemainingTime(datetime_fromdb, now) : Util.getActualTime(date.getHours(), date.getMinutes());
//
//            if (TextUtils.isEmpty(txt))
//                txt = Util.getActualTime(date.getHours(), date.getMinutes());
//            ci.setTime(txt);
//            String status = c.getString(3);
//            int notification_sound = c.getInt(4);
//            if (notification_sound == 0) {
//                ci.setNotification_state(false);
//            } else ci.setNotification_state(true);
//
//            result.add(ci);
//            c.moveToNext();
//        }
        return result;
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
