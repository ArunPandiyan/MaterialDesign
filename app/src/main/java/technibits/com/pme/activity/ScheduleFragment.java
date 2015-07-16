package technibits.com.pme.activity;

/**
 * Created by Technibits-13 on 15-May-2015.
 */

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.app.Fragment;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import technibits.com.pme.R;
import technibits.com.pme.adapter.ScheduleListAdapter;
import technibits.com.pme.alarmactivity.AddAlarmActivity;
import technibits.com.pme.alarmactivity.AlarmService;
import technibits.com.pme.alarmactivity.RemindMe;
import technibits.com.pme.alarmactivity.Util;
import technibits.com.pme.alarmmodel.Alarm;
import technibits.com.pme.alarmmodel.AlarmMsg;
import technibits.com.pme.data.ScheduleInfo;
import technibits.com.pme.model.MyRecyclerScroll;
import technibits.com.pme.model.SwipeableRecyclerViewTouchListener;


public class ScheduleFragment extends Fragment {
    private Activity activity;
    public List<ScheduleInfo> result;
    public ScheduleListAdapter scheduleListAdapter;
    public RecyclerView scheduled_list;
    int fabMargin;
    FrameLayout fab;
    ImageButton fabBtn;
    View fabShadow;
    private SQLiteDatabase db;
    private Alarm alarm = new Alarm();
    private AlarmMsg alarmMsg = new AlarmMsg();
    public final Calendar cal = Calendar.getInstance();
    private Cursor fromdb;
    private LinearLayoutManager llm;
    private String[] monthArr;
    private ImageView overflow;
    private CoordinatorLayout coordinatorLayout;
    private boolean isLayoutcreated = false;

    public ScheduleFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = getActivity();
//        GoogleAnalytics analytics = GoogleAnalytics.getInstance(getActivity());
//        Tracker tracker = analytics.newTracker("UA-XXXX-Y"); // Send hits to tracker id UA-XXXX-Y
//
//// All subsequent hits will be send with screen name = "main screen"
//        tracker.setScreenName("Schedulers");
//
//        tracker.send(new HitBuilders.EventBuilder()
//                .setCategory("UX")
//                .setAction("click")
//                .setLabel("submit")
//                .build());
//
//// Builder parameters can overwrite the screen name set on the tracker.
//        tracker.send(new HitBuilders.EventBuilder()
//                .setCategory("UX")
//                .setAction("click")
//                .setLabel("help popup")
////                .setScreenName("help popup dialog")
//                .build());
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_schedule, container, false);
        coordinatorLayout = (CoordinatorLayout) rootView.findViewById(R.id.coordi);
        Animation animation = AnimationUtils.loadAnimation(activity, R.anim.simple_grow);
        db = RemindMe.db;
        monthArr = getResources().getStringArray(R.array.spinner3_arr);
        int r = RemindMe.getDateRange();
        switch (r) {
            case 3: // Yearly
                cal.set(Calendar.MONTH, 0);

            case 2: // Monthly
                cal.set(Calendar.DATE, 1);

            case 1: // Weekly
                if (r == 1) cal.set(Calendar.DATE, cal.getFirstDayOfWeek());

            case 0: // Daily
                cal.set(Calendar.HOUR_OF_DAY, 0);
                cal.set(Calendar.MINUTE, 0);
                cal.set(Calendar.SECOND, 0);
                cal.set(Calendar.MILLISECOND, 0);
        }

//        overflow=(ImageView)rootView.findViewById(R.id.overflow);
//        overflow.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//            }
//        });
        PopupMenu popup = new PopupMenu(activity, overflow);
        MenuInflater menuinflater = popup.getMenuInflater();
//        inflater.inflate(menuinflater);
        scheduled_list = (RecyclerView) rootView.findViewById(R.id.scheduled_list);
        scheduled_list.setHasFixedSize(true);
        scheduled_list.setItemAnimator(new DefaultItemAnimator());

        llm = new LinearLayoutManager(activity);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        scheduled_list.setLayoutManager(llm);



        SwipeableRecyclerViewTouchListener swipeTouchListener =
                new SwipeableRecyclerViewTouchListener(scheduled_list,
                        new SwipeableRecyclerViewTouchListener.SwipeListener() {
                            @Override
                            public boolean canSwipe(int position) {
                                return true;
                            }

                            @Override
                            public void onDismissedBySwipeLeft(RecyclerView recyclerView, int[] reverseSortedPositions) {
                                for (int position : reverseSortedPositions) {
                                    dissmisslistByswipe(position);
                                }

                            }

                            @Override
                            public void onDismissedBySwipeRight(RecyclerView recyclerView, int[] reverseSortedPositions) {
                                for (int position : reverseSortedPositions) {
                                    dissmisslistByswipe(position);
                                }
                            }
                        });

        scheduled_list.addOnItemTouchListener(swipeTouchListener);
        result = createList();
        scheduleListAdapter = new ScheduleListAdapter(result);


//--
        scheduled_list.addOnScrollListener(new MyRecyclerScroll() {
            @Override
            public void show() {

                fab.animate().translationY(0).setInterpolator(new DecelerateInterpolator(2)).start();
            }

            @Override
            public void hide() {

                fab.animate().translationY(fab.getHeight() + fabMargin).setInterpolator(new AccelerateInterpolator(2)).start();
            }
        });
        isLayoutcreated = true;
        fab = (FrameLayout) rootView.findViewById(R.id.myfab_main);
        fabBtn = (ImageButton) rootView.findViewById(R.id.myfab_main_btn);
        fabShadow = rootView.findViewById(R.id.myfab_shadow);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            fabShadow.setVisibility(View.GONE);
            fabBtn.setBackgroundResource(R.drawable.ripple_accent);
        }

        fab.startAnimation(animation);

        fabBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(activity, AddAlarmActivity.class));

            }
        });

        scheduled_list.setAdapter(scheduleListAdapter);
//--
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

    @Override
    public void onResume() {
        super.onResume();
        if (isLayoutcreated) {
            result = null;
            result = createList();
            scheduleListAdapter = new ScheduleListAdapter(result);
            scheduled_list.setAdapter(scheduleListAdapter);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    private List<ScheduleInfo> createList() {


        List result = new ArrayList();
        Cursor c = RemindMe.dbHelper.getNotifications(db);
        getActivity().startManagingCursor(c);
        c.moveToFirst();
        while (c.isAfterLast() == false) {
            ScheduleInfo ci = new ScheduleInfo();
            ci.setId(c.getInt(1));
            ci.setName(c.getString(0));

            long datetime_fromdb = c.getLong(2);
            Date date = new Date();
            date.setTime(datetime_fromdb);

            ci.setYear(String.valueOf(date.getYear() + 1900));
            ci.setMonth(String.valueOf(monthArr[date.getMonth() + 1]));
            ci.setDate(String.valueOf(date.getDate()));
            long now = System.currentTimeMillis();

            String txt = RemindMe.showRemainingTime() ? Util.getRemainingTime(datetime_fromdb, now) : Util.getActualTime(date.getHours(), date.getMinutes());

            if (TextUtils.isEmpty(txt))
                txt = Util.getActualTime(date.getHours(), date.getMinutes());
            ci.setTime(txt);
            String status = c.getString(3);
            int notification_sound = c.getInt(4);
            if (notification_sound == 0) {
                ci.setNotification_state(false);
            } else ci.setNotification_state(true);

            result.add(ci);
            c.moveToNext();
        }
//        c.close();
//        for (int i = 1; i <= result.size(); i++) {
//            ScheduleInfo ci = new ScheduleInfo();
//            ci.setName("name is :" + i);
//            ci.setDate("16/06/2015");
//            if (i % 2 == 0) {
//                ci.setNotification_state(true);
//            } else ci.setNotification_state(false);
//
//            result.add(ci);
//
//        }


        return result;
    }

    private void dissmisslistByswipe(int position) {
        ScheduleInfo sd = result.get(position);
        int id_txt = sd.getId();
        result.remove(position);
        scheduleListAdapter.notifyItemRemoved(position);
        scheduleListAdapter.notifyDataSetChanged();
        long id = id_txt;
        RemindMe.dbHelper.cancelNotification(db, id, false);
        Intent cancelThis = new Intent(getActivity(), AlarmService.class);
        cancelThis.putExtra(AlarmMsg.COL_ID, id);
        cancelThis.setAction(AlarmService.CANCEL);
        getActivity().startService(cancelThis);
        Snackbar.make(coordinatorLayout, "Schedule removed successfully !", Snackbar.LENGTH_SHORT).show();
    }
}
