package technibits.com.pme.activity;


import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import technibits.com.pme.R;
import technibits.com.pme.adapter.NavigationDrawerAdapter;
import technibits.com.pme.alarmactivity.RemindMe;
import technibits.com.pme.model.NavDrawerItem;
import technibits.com.pme.model.CircleTransform;


public class FragmentDrawer extends Fragment {

    private static String TAG = FragmentDrawer.class.getSimpleName();
    private TypedArray navMenuIcons;
    private RecyclerView recyclerView;
    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerLayout mDrawerLayout;
    private NavigationDrawerAdapter adapter;
    private View containerView;
    private static String[] titles = null;
    private FragmentDrawerListener drawerListener;
    private String userName = null;
    private String userEmail_str = null;
    private String userImageurl = null;
    //    private UserBean usr=new UserBean();
    private DBConnection db;

    public FragmentDrawer() {

    }

    public void setDrawerListener(FragmentDrawerListener listener) {
        this.drawerListener = listener;
    }

    public static List<NavDrawerItem> getData() {
        List<NavDrawerItem> data = new ArrayList<>();
        boolean ispre=RemindMe.mIsPremium;

        // preparing navigation drawer items
        for (int i = 0; i < titles.length; i++) {
            NavDrawerItem navItem = new NavDrawerItem();
            if(titles[i].equalsIgnoreCase("Buy Pro Version") && RemindMe.mIsPremium ){


            }else{
                navItem.setTitle(titles[i]);
                navItem.setIcon(i);
                data.add(navItem);
            }



        }
        return data;
    }
//    public UserBean getUserBean() {
//        UserBean userBean=new UserBean();
//        String name = "";
//        Cursor cur = db.executeQuery("select * from user");
//        cur.moveToFirst();
//        if (cur.isAfterLast() == false) {
//            userBean.setName(cur.getString(0));
//            userBean.setLastname(cur.getString(1));
//            userBean.setEmail(cur.getString(2));// string 3 is password we dont need this
//            userBean.setCountry(cur.getString(4));
//            userBean.setMobile(cur.getString(5));
//            userBean.setImage_url(cur.getString(6));
//
//            cur.moveToNext();
//        }
//        cur.close();
//        return userBean;
//    } --------------------------no need----------------------------


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = new DBConnection(getActivity());
        db.open();
        userName = db.getuserName();
        userEmail_str = db.getuserEmail();
        userImageurl = db.getuserImageurl();

//        usr=getUserBean();---no need
        // drawer labels
        titles = getActivity().getResources().getStringArray(R.array.nav_drawer_labels);
        ImageView imgIcon = (ImageView) getActivity().findViewById(R.id.icon);
//        imgIcon.setImageResource(R.array.nav_drawer_icons);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflating view layout

        View layout = inflater.inflate(R.layout.fragment_navigation_drawer, container, false);


        recyclerView = (RecyclerView) layout.findViewById(R.id.drawerList);
        ImageView profile_img = (ImageView) layout.findViewById(R.id.imageView);
        TextView username = (TextView) layout.findViewById(R.id.user_name);
        TextView userEmail = (TextView) layout.findViewById(R.id.userEmail);
        username.setText(userName);
        userEmail.setText(userEmail_str);
        username.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getActivity(),CreateAccountActivity.class);
                intent.putExtra("mode", "update");
                startActivity(intent);
            }
        });
        userEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getActivity(),CreateAccountActivity.class);
                intent.putExtra("mode", "update");
                startActivity(intent);
            }
        });

        Glide.with(getActivity()).load(userImageurl).override(70, 70).transform(new CircleTransform(getActivity())).into(profile_img);
        adapter = new NavigationDrawerAdapter(getActivity(), getData());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getActivity(), recyclerView, new ClickListener() {
            @Override
            public void onClick(View view, int position) {
                drawerListener.onDrawerItemSelected(view, position);
                mDrawerLayout.closeDrawer(containerView);
            }

            @Override
            public void onLongClick(View view, int position) {

            }

        }));

        return layout;
    }


    public void setUp(int fragmentId, DrawerLayout drawerLayout, final Toolbar toolbar) {
        containerView = getActivity().findViewById(fragmentId);
        mDrawerLayout = drawerLayout;
        mDrawerToggle = new ActionBarDrawerToggle(getActivity(), drawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                getActivity().invalidateOptionsMenu();
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                getActivity().invalidateOptionsMenu();
            }

            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                super.onDrawerSlide(drawerView, slideOffset);
                toolbar.setAlpha(1 - slideOffset / 2);
            }
        };

        mDrawerLayout.setDrawerListener(mDrawerToggle);
        mDrawerLayout.post(new Runnable() {
            @Override
            public void run() {
                mDrawerToggle.syncState();
            }
        });

    }

    public static interface ClickListener {
        public void onClick(View view, int position);

        public void onLongClick(View view, int position);
    }

    static class RecyclerTouchListener implements RecyclerView.OnItemTouchListener {

        private GestureDetector gestureDetector;
        private ClickListener clickListener;

        public RecyclerTouchListener(Context context, final RecyclerView recyclerView, final ClickListener clickListener) {
            this.clickListener = clickListener;
            gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }

                @Override
                public void onLongPress(MotionEvent e) {
                    View child = recyclerView.findChildViewUnder(e.getX(), e.getY());
                    if (child != null && clickListener != null) {
                        clickListener.onLongClick(child, recyclerView.getChildPosition(child));
                    }
                }
            });
        }

        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {

            View child = rv.findChildViewUnder(e.getX(), e.getY());
            if (child != null && clickListener != null && gestureDetector.onTouchEvent(e)) {
                clickListener.onClick(child, rv.getChildPosition(child));
            }
            return false;
        }

        @Override
        public void onTouchEvent(RecyclerView rv, MotionEvent e) {
        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean b) {

        }
    }

    public interface FragmentDrawerListener {
        public void onDrawerItemSelected(View view, int position);
    }
}
