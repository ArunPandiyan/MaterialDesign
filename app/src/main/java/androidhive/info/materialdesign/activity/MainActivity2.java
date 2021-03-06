//package androidhive.info.materialdesign.activity;
//
///**
// * Created by Technibits-13 on 15-May-2015.
// */
//
//import android.app.Fragment;
//import android.app.FragmentActivity;
//import android.support.v4.app.FragmentManager;
//import android.support.v4.app.FragmentTransaction;
//import android.support.v7.app.ActionBarActivity;
//
//
//        import android.support.v4.widget.DrawerLayout;
//        import android.support.v7.app.ActionBarActivity;
//        import android.os.Bundle;
//        import android.support.v7.app.ActionBarDrawerToggle;
//        import android.support.v7.widget.LinearLayoutManager;
//        import android.support.v7.widget.RecyclerView;
//        import android.support.v7.widget.Toolbar;
//        import android.view.Menu;
//        import android.view.MenuItem;
//        import android.view.View;
//import android.widget.Toast;
//
//import androidhive.info.materialdesign.R;
//
//
//public class MainActivity2 extends ActionBarActivity implements FragmentDrawer.FragmentDrawerListener{
//
//    //First We Declare Titles And Icons For Our Navigation Drawer List View
//    //This Icons And Titles Are holded in an Array as you can see
//
//    String TITLES[] = {"Home","Events","Mail","Shop","Travel"};
//    int ICONS[] = {R.drawable.ic_home,
//            R.drawable.ic_home,
//            R.drawable.ic_home,
//            R.drawable.ic_home,
//            R.drawable.ic_home};
//
//    //Similarly we Create a String Resource for the name and email in the header view
//    //And we also create a int resource for profile picture in the header view
//
//    String NAME = "Akash Bangad";
//    String EMAIL = "akash.bangad@android4devs.com";
//    int PROFILE = R.drawable.ic_profile;
//
//    private Toolbar toolbar;                              // Declaring the Toolbar Object
//
//    RecyclerView mRecyclerView;                           // Declaring RecyclerView
//    RecyclerView.Adapter mAdapter;                        // Declaring Adapter For Recycler View
//    RecyclerView.LayoutManager mLayoutManager;            // Declaring Layout Manager as a linear layout manager
//    DrawerLayout Drawer;                                  // Declaring DrawerLayout
//
//    ActionBarDrawerToggle mDrawerToggle;                  // Declaring Action Bar Drawer Toggle
//
//
//
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main2);
//
//    /* Assinging the toolbar object ot the view
//    and setting the the Action bar to our toolbar
//     */
//        toolbar = (Toolbar) findViewById(R.id.tool_bar);
//        setSupportActionBar(toolbar);
//        mRecyclerView = (RecyclerView) findViewById(R.id.RecyclerView); // Assigning the RecyclerView Object to the xml View
//        mRecyclerView.setHasFixedSize(true);                            // Letting the system know that the list objects are of fixed size
//        mAdapter = new MyAdapter(TITLES,ICONS,NAME,EMAIL,PROFILE);       // Creating the Adapter of MyAdapter class(which we are going to see in a bit)
//        // And passing the titles,icons,header view name, header view email,
//        // and header view profile picture
//
//        mRecyclerView.setAdapter(mAdapter);                              // Setting the adapter to RecyclerView
//        mLayoutManager = new LinearLayoutManager(this);                 // Creating a layout Manager
//        mRecyclerView.setLayoutManager(mLayoutManager);                 // Setting the layout Manager
//        Drawer = (DrawerLayout) findViewById(R.id.DrawerLayout);        // Drawer object Assigned to the view
//        mDrawerToggle = new ActionBarDrawerToggle(this,Drawer,toolbar,R.string.drawer_open ,R.string.drawer_close){
//
//            @Override
//            public void onDrawerOpened(View drawerView) {
//                super.onDrawerOpened(drawerView);
//                // code here will execute once the drawer is opened( As I dont want anything happened whe drawer is
//                // open I am not going to put anything here)
//            }
//
//            @Override
//            public void onDrawerClosed(View drawerView) {
//                super.onDrawerClosed(drawerView);
//                // Code here will execute once drawer is closed
//
//            }
//
//
//
//        }; // Drawer Toggle Object Made
//        Drawer.setDrawerListener(mDrawerToggle); // Drawer Listener set to the Drawer toggle
//        mDrawerToggle.syncState();               // Finally we set the drawer toggle sync State
//
//    }
//
//
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_main, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//
//        /*
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//        }*/
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            Toast.makeText(getApplicationContext(), "Setting Can be invoked!", Toast.LENGTH_SHORT).show();
//            return true;
//        }
//
//        if(id == R.id.action_search){
//            Toast.makeText(getApplicationContext(), "Search action is selected!", Toast.LENGTH_SHORT).show();
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//}
//
//    @Override
//    public void onDrawerItemSelected(View view, int position) {
//        displayView(position);
//    }
//    private void displayView(int position) {
//        Fragment fragment = null;
//        FragmentActivity frag=null;
//        String title = getString(R.string.app_name);
//        switch (position) {
//            case 0:
//                fragment = new ExxamFragment();
//                title = getString(R.string.title_take_exam);
//                break;
//            case 1:
//                fragment = new PerformanceFragment();
//                title = getString(R.string.title_performance_history);
//                break;
//            case 2:
//                fragment = new ReviewFragment();
//                title = getString(R.string.title_review);
//                break;
//            case 3:
//                fragment = new ScheduleFragment();
//                title = getString(R.string.title_schedules);
//                break;
//            case 4:
//                fragment = new VideoFragment();
//                title = getString(R.string.title_videos);
//                break;
//            case 5:
//                fragment = new AboutFragment();
//                title = getString(R.string.title_about);
//                break;
//            default:
//                break;
//        }
//
//        if (fragment != null) {
//            FragmentManager fragmentManager = getSupportFragmentManager();
//            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//            fragmentTransaction.replace(R.id.container_body, fragment);
//            fragmentTransaction.commit();
//
//            // set the toolbar title
//            getSupportActionBar().setTitle(title);
//        }
//    }
//}