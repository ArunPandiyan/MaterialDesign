package technibits.com.pme.activity;

/**
 * Created by Technibits-13 on 15-May-2015.
 */

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.app.Fragment;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import technibits.com.pme.R;
import technibits.com.pme.adapter.ScheduleListAdapter;
import technibits.com.pme.data.ScheduleInfo;
import technibits.com.pme.model.MyRecyclerScroll;
import technibits.com.pme.model.SwipeableRecyclerViewTouchListener;


public class ScheduleFragment extends Fragment {
    private Activity activity;
    public List<ScheduleInfo> result;
    public ScheduleListAdapter scheduleListAdapter;
    public RecyclerView scheduled_list;
    public CollapsingToolbarLayout collapsingToolbar;
    int mutedColor = R.attr.colorPrimary;
    int fabMargin;
    FrameLayout fab;
    ImageButton fabBtn;
    View fabShadow;
    boolean fadeToolbar = false;

    public ScheduleFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_schedule, container, false);
        Animation animation = AnimationUtils.loadAnimation(activity, R.anim.simple_grow);


        scheduled_list = (RecyclerView) rootView.findViewById(R.id.scheduled_list);
        scheduled_list.setHasFixedSize(true);
        scheduled_list.setItemAnimator(new DefaultItemAnimator());

        LinearLayoutManager llm = new LinearLayoutManager(activity);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        scheduled_list.setLayoutManager(llm);

        result = new ArrayList<ScheduleInfo>();
        ScheduleInfo ci = new ScheduleInfo();
        ScheduleInfo ci1 = new ScheduleInfo();
        ScheduleInfo ci2 = new ScheduleInfo();
        ScheduleInfo ci3 = new ScheduleInfo();
        ScheduleInfo ci4 = new ScheduleInfo();
        ScheduleInfo ci5 = new ScheduleInfo();
        ScheduleInfo ci6 = new ScheduleInfo();
        ScheduleInfo ci7 = new ScheduleInfo();
        ScheduleInfo ci8 = new ScheduleInfo();
        ScheduleInfo ci9 = new ScheduleInfo();

        ci.setName("My Test111");
        ci.setNotification_state(true);
        ci.setDate("16-06-2015");
        result.add(ci);

        ci1.setName("My Test222");
        ci1.setNotification_state(false);
        ci1.setDate("17-06-2015");
        result.add(ci1);

        ci2.setName("My Test333");
        ci2.setNotification_state(true);
        ci2.setDate("17-06-2015");
        result.add(ci2);


        ci3.setName("My Test444");
        ci3.setNotification_state(false);
        ci3.setDate("17-06-2015");
        result.add(ci3);

        ci4.setName("My Test555");
        ci4.setNotification_state(true);
        ci4.setDate("13-06-2015");
        result.add(ci4);

        ci5.setName("My Test666");
        ci5.setNotification_state(true);
        ci5.setDate("17-06-2015");
        result.add(ci5);

        ci6.setName("My Test777");
        ci6.setNotification_state(false);
        ci6.setDate("27-06-2015");
        result.add(ci6);

        ci7.setName("My Test888");
        ci7.setNotification_state(false);
        ci7.setDate("27-06-2015");
        result.add(ci7);

        ci8.setName("My Test999");
        ci8.setNotification_state(true);
        ci8.setDate("27-06-2015");
        result.add(ci8);

        ci9.setName("My Test1010");
        ci9.setNotification_state(false);
        ci9.setDate("27-06-2015");
        result.add(ci9);




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
                                    result.remove(position);
                                    scheduleListAdapter.notifyItemRemoved(position);
                                    scheduleListAdapter.notifyDataSetChanged();
                                    Snackbar.make(getView(), "Schedule removed successfully !", Snackbar.LENGTH_SHORT).show();
//                                    scheduleListAdapter.removeAt(position);
                                }

                            }

                            @Override
                            public void onDismissedBySwipeRight(RecyclerView recyclerView, int[] reverseSortedPositions) {
                                for (int position : reverseSortedPositions) {
                                    result.remove(position);
                                    scheduleListAdapter.notifyItemRemoved(position);
                                    scheduleListAdapter.notifyDataSetChanged();
                                    Snackbar.make(getView(), "Schedule removed successfully !", Snackbar.LENGTH_SHORT).show();
                                }
                            }
                        });

        scheduled_list.addOnItemTouchListener(swipeTouchListener);
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
                Log.d("CLICK", "FAB CLICK");
                Toast.makeText(activity, "FAB Clicked", Toast.LENGTH_SHORT).show();

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

    private List<ScheduleInfo> createList(int size) {


        List result = new ArrayList();
        for (int i = 1; i <= size; i++) {
            ScheduleInfo ci = new ScheduleInfo();
            ci.setName("name is :" + i);
            ci.setDate("16/06/2015");
            if (i % 2 == 0) {
                ci.setNotification_state(true);
            } else ci.setNotification_state(false);

            result.add(ci);

        }


        return result;
    }
}
