package technibits.com.pme.activity;

/**
 * Created by Technibits-13 on 15-May-2015.
 */

import android.app.Activity;
import android.os.Bundle;
import android.app.Fragment;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import technibits.com.pme.R;
import technibits.com.pme.adapter.ScheduleListAdapter;
import technibits.com.pme.data.ScheduleInfo;
import technibits.com.pme.model.SwipeableRecyclerViewTouchListener;


public class ScheduleFragment extends Fragment {
    private Activity activity;
    public List<ScheduleInfo> result;
    public ScheduleListAdapter scheduleListAdapter;
    public RecyclerView scheduled_list;


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

        scheduled_list = (RecyclerView) rootView.findViewById(R.id.scheduled_list);
        scheduled_list.setHasFixedSize(true);

        LinearLayoutManager llm = new LinearLayoutManager(activity);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        scheduled_list.setLayoutManager(llm);

        result = new ArrayList<ScheduleInfo>();
        ScheduleInfo ci = new ScheduleInfo();
        ScheduleInfo ci1 = new ScheduleInfo();
        ScheduleInfo ci2 = new ScheduleInfo();

        ci.setName("My Test111");
        ci.setNotification_state(true);
        ci.setDate("16-06-2015");
        result.add(ci);

        ci1.setName("My Test222");
        ci1.setNotification_state(false);
        ci1.setDate("17-06-2015");
        result.add(ci1);

        ci2.setName("My Test333");
        ci2.setNotification_state(false);
        ci2.setDate("17-06-2015");
        result.add(ci2);


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
        scheduled_list.setAdapter(scheduleListAdapter);
        scheduled_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "from fragment", Snackbar.LENGTH_SHORT).show();
            }
        });

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
