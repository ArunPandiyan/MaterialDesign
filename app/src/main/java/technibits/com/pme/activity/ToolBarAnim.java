package technibits.com.pme.activity;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

import technibits.com.pme.R;
import technibits.com.pme.adapter.ScheduleListAdapter;
import technibits.com.pme.data.ScheduleInfo;
import technibits.com.pme.model.SwipeableRecyclerViewTouchListener;

/**
 * Created by technibitsuser on 6/17/2015.
 */
public class ToolBarAnim extends AppCompatActivity {
    private Activity activity;
    public List<ScheduleInfo> result;
    public ScheduleListAdapter scheduleListAdapter;
    public RecyclerView scheduled_list;
    public CollapsingToolbarLayout collapsingToolbar;
    int mutedColor = R.attr.colorPrimary;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.toolbaranim_layout);
        final Toolbar toolbar = (Toolbar) findViewById(R.id.anim_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        collapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbar.setTitle("Add Schedule");
        ImageView header = (ImageView) findViewById(R.id.header);
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(),
                R.drawable.exam_schedule);

        Palette.from(bitmap).generate(new Palette.PaletteAsyncListener() {
            @Override
            public void onGenerated(Palette palette) {

                mutedColor = palette.getMutedColor(R.attr.colorPrimary);
                collapsingToolbar.setContentScrimColor(mutedColor);
            }
        });


        scheduled_list = (RecyclerView) findViewById(R.id.scheduled_list);
        scheduled_list.setHasFixedSize(true);

        LinearLayoutManager llm = new LinearLayoutManager(activity);
//        llm.setOrientation(LinearLayoutManager.VERTICAL);
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
                                    Snackbar.make(recyclerView, "Schedule removed successfully !", Snackbar.LENGTH_SHORT).show();
//                                    scheduleListAdapter.removeAt(position);
                                }

                            }

                            @Override
                            public void onDismissedBySwipeRight(RecyclerView recyclerView, int[] reverseSortedPositions) {
                                for (int position : reverseSortedPositions) {
                                    result.remove(position);
                                    scheduleListAdapter.notifyItemRemoved(position);
                                    scheduleListAdapter.notifyDataSetChanged();
                                    Snackbar.make(recyclerView, "Schedule removed successfully !", Snackbar.LENGTH_SHORT).show();
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

    }
}
